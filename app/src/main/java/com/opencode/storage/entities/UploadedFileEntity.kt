package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Uploaded file entity - tracks files uploaded to a project workspace.
 * Supports all file types: images, videos, audio, documents, archives, models, code.
 */
@Entity(tableName = "uploaded_files")
data class UploadedFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val projectId: Long,
    val fileName: String,
    val filePath: String,
    val mimeType: String,
    val fileSize: Long,
    val category: FileUploadCategory,
    val uploadTimestamp: Long = System.currentTimeMillis(),
)

enum class FileUploadCategory {
    CODE,
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    ARCHIVE,
    BINARY,
    MODEL,
    CONFIG,
    OTHER,
}
