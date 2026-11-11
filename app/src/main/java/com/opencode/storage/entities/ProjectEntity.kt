package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project entity - represents a coding project in the workspace.
 * Projects can be Flutter, Kotlin/Compose, Java/Gradle, Native C/C++, or mixed.
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val path: String,
    val projectType: String,           // "FLUTTER", "KOTLIN_COMPOSE", "JAVA_GRADLE", "NATIVE_C_CPP", "MIXED"
    val lastUsed: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val description: String = "",
)
