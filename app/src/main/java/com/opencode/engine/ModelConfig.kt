package com.opencode.engine

import java.io.File

/**
 * Model configuration - complete setup for each coding agent's model
 * Supports both cloud APIs and local GGUF models
 */
data class ModelConfig(
    val agentType: AgentType,

    // Cloud model settings
    val cloudProvider: String? = null,       // e.g. "anthropic", "openai", "deepseek"
    val cloudModelName: String? = null,       // e.g. "claude-sonnet-5-5", "gpt-4"
    val apiKey: String? = null,               // API key for cloud provider
    val endpointUrl: String? = null,          // Custom endpoint URL
    val headers: Map<String, String> = emptyMap(),

    // Local GGUF model settings
    val ggufFilePath: File? = null,
    val ggufFileName: String? = null,
    val ggufFileSizeBytes: Long = 0,
    val ggufQuantization: String = "",         // e.g. "Q4_K_M", "Q5_K_M"
    val ggufContextLength: Int = 32768,
    val ggufParameters: Int = 0,
    val ggufThreads: Int = 4,
    val ggufGpuLayers: Int = 0,               // 0 = CPU, > 0 = Vulkan layers

    // Inference settings (both cloud and local)
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val topK: Int = 20,
    val maxTokens: Int = 4096,
    val maxContextTokens: Int = 128000,

    // Enable/disable features
    val enableReasoning: Boolean = true,       // Show thinking/reasoning blocks
    val enableToolCalls: Boolean = true,       // Allow the model to call tools
    val enableVision: Boolean = false,         // Allow image input

    // Memory / context management
    val maxConversationTurns: Int = 50,
    val compactThresholdTokens: Int = 60000,

    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
) {
    /**
     * Get the current model source type
     */
    fun sourceType(): ModelSource {
        return when {
            ggufFilePath != null && ggufFilePath.exists() -> ModelSource.LOCAL_GGUF
            apiKey != null && (cloudModelName != null || cloudProvider != null) -> ModelSource.CLOUD_API
            else -> ModelSource.HYBRID
        }
    }

    /**
     * Validate that at least one model source is configured
     */
    fun isValid(): Boolean {
        return when (sourceType()) {
            ModelSource.LOCAL_GGUF -> ggufFilePath != null && ggufFilePath.exists() && ggufFileName != null
            ModelSource.CLOUD_API -> apiKey != null && (cloudModelName != null || cloudProvider != null)
            ModelSource.HYBRID -> true
        }
    }
}
