package com.opencode.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "models")
data class ModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val agentType: String = "",
    val cloudProvider: String = "",
    val cloudModelName: String = "",
    val apiKeyHash: String = "",
    val endpointUrl: String = "",
    val ggufFilePath: String = "",
    val ggufFileName: String = "",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 4096,
    val maxContextTokens: Int = 128000,
    val createdAt: Long = System.currentTimeMillis()
)
