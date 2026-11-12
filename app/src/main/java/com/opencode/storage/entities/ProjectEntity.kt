package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val path: String = "",
    val projectType: String = "MIXED",
    val lastUsed: Long = 0,
    val lastModified: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val description: String = ""
)
