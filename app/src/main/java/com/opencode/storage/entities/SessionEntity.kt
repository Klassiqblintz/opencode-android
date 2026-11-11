package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Session entity - represents a conversation session with the coding agent.
 * Each session has a unique ID, a title, messages, and a status.
 */
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val projectId: Long,
    val title: String,
    val status: SessionStatus = SessionStatus.IDLE,
    val messageCount: Int = 0,
    val lastMessageAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
)

enum class SessionStatus {
    IDLE,
    RUNNING,
    THINKING,
    WAITING_INPUT,
    WAITING_PERMISSION,
    COMPLETED,
    ERRORED,
}
