package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long = 0,
    val title: String = "",
    val status: String = "IDLE",
    val messageCount: Int = 0,
    val lastMessageAt: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
