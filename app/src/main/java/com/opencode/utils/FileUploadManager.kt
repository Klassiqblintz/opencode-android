package com.opencode.utils

import android.content.Context
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

// Result of file upload
sealed class UploadResult {
    data class Success(
        val fileName: String,
        val path: String,
        val mimeType: String,
        val fileSize: Long,
        val category: FileCategory,
        val isMedia: Boolean,
        val isArchive: Boolean
    ) : UploadResult()

    data class Error(val message: String) : UploadResult()
}

// File info for listing uploaded files
data class FileInfo(
    val name: String,
    val path: String,
    val size: Long,
    val category: FileCategory,
    val lastModified: Long
)

// Manages file uploads and categorization
class FileUploadManager(private val context: Context) {

    private val extensionSets = mapOf(
        FileCategory.IMAGE to setOf("jpg", "jpeg", "png", "gif", "webp", "bmp", "svg"),
        FileCategory.VIDEO to setOf("mp4", "mov", "avi", "mkv", "webm"),
        FileCategory.AUDIO to setOf("mp3", "wav", "ogg", "flac", "aac"),
        FileCategory.DOCUMENT to setOf("pdf", "doc", "docx", "txt", "md", "html", "csv"),
        FileCategory.ARCHIVE to setOf("zip", "tar", "gz", "7z", "rar"),
        FileCategory.MODEL to setOf("gguf", "pt", "onnx", "tflite"),
        FileCategory.CODE to setOf(
            "kt", "dart", "java", "py", "js", "ts",
            "html", "css", "xml", "json", "yaml", "toml",
            "gradle", "cpp", "c", "h", "sh", "go", "rs",
            "swift", "php", "vue", "sql"
        ),
        FileCategory.CONFIG to setOf("cfg", "ini", "env", "properties")
    )

    fun categorizeFile(fileName: String): FileCategory {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        for ((category, extensions) in extensionSets) {
            if (extensions.contains(ext)) return category
        }
        return FileCategory.OTHER
    }

    fun isMedia(category: FileCategory): Boolean =
        category in listOf(FileCategory.IMAGE, FileCategory.VIDEO, FileCategory.AUDIO)

    fun isArchive(category: FileCategory): Boolean =
        category == FileCategory.ARCHIVE

    suspend fun uploadFile(
        uri: android.net.Uri?,
        path: String,
        subdirectory: String? = null
    ): UploadResult {
        if (uri == null) return UploadResult.Error("No URI provided")

        return try {
            val fileName = extractFileName(uri) ?: return UploadResult.Error("Cannot extract filename")
            val fileCategory = categorizeFile(fileName)
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
            val targetDir = File(path)
            if (subdirectory != null) {
                File(targetDir, subdirectory).mkdirs()
            }
            val uniqueName = getUniqueName(targetDir, subdirectory, fileName)
            val targetFile = File(targetDir, subdirectory).also { it.mkdirs() }.let {
                File(it, uniqueName)
            }

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            val fileSize = targetFile.length()
            val isMedia = isMedia(fileCategory)
            val isArchive = isArchive(fileCategory)

            UploadResult.Success(
                fileName = fileName,
                path = targetFile.absolutePath,
                mimeType = mimeType,
                fileSize = fileSize,
                category = fileCategory,
                isMedia = isMedia,
                isArchive = isArchive
            )
        } catch (e: Exception) {
            UploadResult.Error(e.message ?: "Upload failed")
        }
    }

    suspend fun uploadMultipleFiles(
        uris: List<android.net.Uri?>,
        path: String,
        subdirectory: String? = null
    ): List<UploadResult> {
        return uris.map { uri ->
            uploadFile(uri, path, subdirectory)
        }
    }

    fun downloadProjectAsZip(projectPath: String, outputDir: String): File {
        val output = java.io.File(outputDir)
        output.mkdirs()
        val zipFile = java.io.File(projectPath)
        val outputZip = java.io.File(output, "${zipFile.name}.zip")

        ZipOutputStream(FileOutputStream(outputZip)).use { zos ->
            zipFile.walkTopDown().forEach { file ->
                if (file.isFile) {
                    val entryName = file.relativeTo(java.io.File(projectPath)).path
                    zos.putNextEntry(ZipEntry(entryName))
                    file.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
        }
        return outputZip
    }

    fun listUploadedFiles(projectPath: String): List<FileInfo> {
        val dir = File(projectPath)
        if (!dir.isDirectory) return emptyList()

        return dir.walkTopDown()
            .filter { it.isFile }
            .map { file ->
                FileInfo(
                    name = file.name,
                    path = file.absolutePath,
                    size = file.length(),
                    category = categorizeFile(file.name),
                    lastModified = file.lastModified()
                )
            }
            .toList()
    }

    private fun extractFileName(uri: android.net.Uri): String? {
        val contentResolver = context.contentResolver
        val displayName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (idx >= 0 && cursor.moveToFirst()) cursor.getString(idx) else null
        }
        return displayName ?: uri.path?.substringAfterLast('/')
    }

    private fun getUniqueName(baseDir: File, subdirectory: String?, fileName: String): String {
        val dir = if (subdirectory != null) File(baseDir, subdirectory) else baseDir
        var candidate = fileName
        var counter = 1
        while (File(dir, candidate).exists()) {
            val nameWithoutExt = fileName.substringBeforeLast('.')
            val ext = fileName.substringAfterLast('.', "")
            candidate = if (ext.isNotEmpty()) {
                "$nameWithoutExt-$counter.$ext"
            } else {
                "$fileName-$counter"
            }
            counter++
        }
        return candidate
    }
}
