package com.opencode.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * File upload manager - handles uploading ANY file type into the workspace
 * Supports: images, videos, audio, documents, ZIPs, code, models, any binary
 */
class FileUploadManager(
    private val context: Context
) {
    companion object {
        const val TAG = "FileUploadManager"

        // Supported file categories
        private val IMAGE_EXTENSIONS = setOf(
            "jpg", "jpeg", "png", "gif", "webp", "bmp", "svg", "ico"
        )
        private val VIDEO_EXTENSIONS = setOf(
            "mp4", "mov", "avi", "mkv", "webm", "m4v"
        )
        private val AUDIO_EXTENSIONS = setOf(
            "mp3", "wav", "ogg", "flac", "aac", "m4a", "wma"
        )
        private val DOCUMENT_EXTENSIONS = setOf(
            "pdf", "doc", "docx", "txt", "md", "rtf", "html", "csv"
        )
        private val ARCHIVE_EXTENSIONS = setOf(
            "zip", "tar", "gz", "bz2", "7z", "rar", "xz"
        )
        private val MODEL_EXTENSIONS = setOf(
            "gguf", "pt", "pth", "onnx", "tflite", "pml", "h5"
        )
        private val CODE_EXTENSIONS = setOf(
            "kt", "kts", "dart", "java", "py", "js", "ts", "jsx", "tsx",
            "html", "css", "scss", "less", "xml", "json", "yaml", "yml",
            "toml", "gradle", "gradle.kts", "cmake", "sh", "bash", "zsh",
            "rb", "go", "rs", "c", "cpp", "h", "hpp", "swift", "php",
            "vue", "svelte", "sql", "graphql", "proto", "tf", "r", "lua"
        )
    }

    /**
     * Upload a file from a Uri (from Intent.ACTION_OPEN_DOCUMENT or similar)
     * Copies the file into the project workspace
     */
    suspend fun uploadFile(
        uri: Uri,
        projectWorkspacePath: String,
        subdirectory: String? = null
    ): UploadResult {
        return try {
            // Get file info from URI
            val fileName = extractFileNameFromUri(uri)
            val mimeType = extractMimeTypeFromUri(uri) ?: "application/octet-stream"
            val extension = fileName.substringAfterLast('.', "")
            val category = categorizeFile(extension, mimeType)

            // Determine destination path
            val destDir = subdirectory?.let {
                File(projectWorkspacePath, it)
            } ?: File(projectWorkspacePath)
            destDir.mkdirs()

            val destPath = File(destDir, fileName)
            val uniqueName = getUniqueFileName(destPath)
            destPath.setPath(File(destDir, uniqueName).absolutePath)

            // Copy file content
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destPath).use { output ->
                    input.copyTo(output)
                }
            }

            val fileSize = destPath.length()
            val isMedia = category == FileCategory.IMAGE ||
                category == FileCategory.VIDEO ||
                category == FileCategory.AUDIO
            val isArchive = category == FileCategory.ARCHIVE

            // If archive, extract it
            if (isArchive) {
                extractArchive(destPath, projectWorkspacePath)
            }

            UploadResult.Success(
                fileName = uniqueName,
                path = destPath.absolutePath,
                mimeType = mimeType,
                fileSize = fileSize,
                category = category,
                isMedia = isMedia,
                isArchive = isArchive,
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload file: ${e.message}", e)
            UploadResult.Error(e.message ?: "Unknown error")
        }
    }

    /**
     * Upload multiple files at once
     */
    suspend fun uploadMultipleFiles(
        uris: List<Uri>,
        projectWorkspacePath: String,
        subdirectory: String? = null
    ): List<UploadResult> {
        return uris.map { uploadFile(it, projectWorkspacePath, subdirectory) }
    }

    /**
     * Download a project as a ZIP file
     */
    fun downloadProjectAsZip(
        projectPath: String,
        outputDir: File
    ): File {
        val zipFile = File(outputDir, "${extractProjectName(projectPath)}.zip")
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            val projectDir = File(projectPath)
            projectDir.walkTopDown().forEach { file ->
                if (file.isDirectory) return@forEach

                val entryName = file.absolutePath
                    .substringAfter(projectPath).substringAfter('/')
                    .let { if (it.isEmpty()) file.name else it }

                zos.putNextEntry(ZipEntry(entryName))
                file.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }
        return zipFile
    }

    /**
     * Extract an uploaded archive into the project workspace
     */
    private fun extractArchive(archiveFile: File, projectPath: String) {
        ZipInputStream(FileInputStream(archiveFile)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val entryFile = File(projectPath, entry.name)
                if (entry.isDirectory) {
                    entryFile.mkdirs()
                } else {
                    entryFile.parentFile?.mkdirs()
                    zis.use { it.copyTo(FileOutputStream(entryFile)) }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }

    // --- Helper methods ---

    private fun extractFileNameFromUri(uri: Uri): String {
        // Try to get display name from content URI
        var name = uri.lastPathSegment
        return name ?: "uploaded_file_${System.currentTimeMillis()}"
    }

    private fun extractMimeTypeFromUri(uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }

    private fun categorizeFile(extension: String, mimeType: String): FileCategory {
        val ext = extension.lowercase()
        return when {
            ext in IMAGE_EXTENSIONS || mimeType.startsWith("image/") -> FileCategory.IMAGE
            ext in VIDEO_EXTENSIONS || mimeType.startsWith("video/") -> FileCategory.VIDEO
            ext in AUDIO_EXTENSIONS || mimeType.startsWith("audio/") -> FileCategory.AUDIO
            ext in DOCUMENT_EXTENSIONS || mimeType.startsWith("text/") || mimeType == "application/pdf" -> FileCategory.DOCUMENT
            ext in ARCHIVE_EXTENSIONS -> FileCategory.ARCHIVE
            ext in MODEL_EXTENSIONS -> FileCategory.MODEL
            ext in CODE_EXTENSIONS || mimeType.contains("text/") -> FileCategory.CODE
            else -> FileCategory.OTHER
        }
    }

    private fun getUniqueFileName(baseFile: File): String {
        if (!baseFile.exists()) return baseFile.name
        val baseName = baseFile.nameWithoutExtension
        val ext = baseFile.extension
        var counter = 1
        while (File("${baseFile.parent}/${baseName}(${counter}).${ext}").exists()) {
            counter++
        }
        return "${baseName}(${counter}).${ext}"
    }

    private fun File.setPath(newPath: String) {
        // Rename this file to the new path
        val target = File(newPath)
        if (this != target) {
            this.renameTo(target)
        }
    }

    private fun extractProjectName(projectPath: String): String {
        return File(projectPath).name
    }

    /**
     * List all uploaded files in a project
     */
    fun listUploadedFiles(projectPath: String): List<FileInfo> {
        val projectDir = File(projectPath)
        if (!projectDir.exists() || !projectDir.isDirectory) return emptyList()

        return projectDir.walkTopDown()
            .filter { it.isFile }
            .map { file ->
                FileInfo(
                    name = file.name,
                    path = file.absolutePath,
                    size = file.length(),
                    category = categorizeFile(
                        file.extension,
                        getMimeType(file)
                    ),
                    lastModified = file.lastModified(),
                )
            }
            .toList()
    }

    private fun getMimeType(file: File): String {
        val ext = file.extension.lowercase()
        return when (ext) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "mp4" -> "video/mp4"
            "mp3" -> "audio/mpeg"
            "pdf" -> "application/pdf"
            "zip" -> "application/zip"
            else -> "application/octet-stream"
        }
    }
}

// --- Result types ---

sealed class UploadResult {
    data class Success(
        val fileName: String,
        val path: String,
        val mimeType: String,
        val fileSize: Long,
        val category: FileCategory,
        val isMedia: Boolean,
        val isArchive: Boolean,
    ) : UploadResult()

    data class Error(
        val message: String,
    ) : UploadResult()
}

data class FileInfo(
    val name: String,
    val path: String,
    val size: Long,
    val category: FileCategory,
    val lastModified: Long,
)
