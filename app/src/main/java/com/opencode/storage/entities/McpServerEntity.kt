package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MCP Server entity - configured MCP servers.
 * Supports remote HTTP servers and local stdio servers.
 */
@Entity(tableName = "mcp_servers")
data class McpServerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val url: String?,           // For remote HTTP servers
    val command: String?,       // For local stdio servers
    val headers: String?,       // JSON string of headers
    val envVars: String?,       // JSON string of env vars
    val isEnabled: Int = 1,     // 1 = enabled, 0 = disabled
    val projectId: Long = 0,    // 0 = global
    val createdAt: Long = System.currentTimeMillis(),
)
