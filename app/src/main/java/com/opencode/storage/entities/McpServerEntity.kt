package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mcp_servers")
data class McpServerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val url: String = "",
    val command: String = "",
    val headers: String = "{}",
    val envVars: String = "{}",
    val isEnabled: Int = 1,
    val projectId: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
