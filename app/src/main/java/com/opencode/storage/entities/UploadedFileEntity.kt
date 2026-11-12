package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploaded_files")
data class UploadedFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long = 0,
    val fileName: String = "",
    val filePath: String = "",
    val mimeType: String = "",
    val fileSize: Long = 0,
    val category: String = "OTHER",  // CODE, IMAGE, VIDEO, AUDIO, DOCUMENT, ARCHIVE, BINARY, MODEL, CONFIG, OTHER
    val uploadTimestamp: Long = System.currentTimeMillis()
)
