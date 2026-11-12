package com.opencode.ui.chat

data class MessageEntity(
    val id: Long = 0,
    val sessionId: Long = 0,
    val role: String = "user",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
