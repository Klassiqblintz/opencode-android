package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long = 0,
    val role: String = "USER",  // USER, AGENT, TOOL_CALL, TOOL_RESULT, ERROR, PERMISSION
    val content: String = "",
    val status: String = "DELIVERED",
    val createdAt: Long = System.currentTimeMillis()
)
