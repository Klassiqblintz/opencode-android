package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Message entity - individual messages within a session.
 * Supports user, agent, tool call, tool result, error, and permission request messages.
 */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sessionId: Long,
    val role: MessageRole,
    val content: String,
    val status: String = "",           // "pending", "completed", etc. for tool calls
    val createdAt: Long = System.currentTimeMillis(),
)

enum class MessageRole {
    USER,
    AGENT,
    TOOL_CALL,
    TOOL,
    ERROR,
    PERMISSION,
}
