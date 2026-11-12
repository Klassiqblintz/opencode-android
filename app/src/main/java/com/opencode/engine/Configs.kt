package com.opencode.engine

import java.io.File

// AgentConfig: configuration for an agent run
data class AgentConfig(
    val agentType: AgentType,
    val runMode: AgentRunMode,
    val rootfsType: String = "",
    val rootfsPath: String = "",
    val linuxPackages: List<String> = emptyList(),
    val ggufFilePath: File? = null,
    val ggufFileName: String = "",
    val ggufQuantization: String = "Q4_K_M",
    val ggufContextLength: Int = 32768,
    val ggufThreads: Int = 4,
    val ggufGpuLayers: Int = 0,
    val apiKey: String = "",
    val endpointUrl: String = "",
    val headers: Map<String, String> = emptyMap(),
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val maxTokens: Int = 4096,
    val maxContextTokens: Int = 128000,
    val enableReasoning: Boolean = true,
    val enableToolCalls: Boolean = true,
    val enableVision: Boolean = false,
    val modelSource: ModelSource = ModelSource.HYBRID,
    val projectWorkspacePath: String = ""
)

// ModelConfig: configuration for a model (cloud or local)
data class ModelConfig(
    val agentType: AgentType,
    val cloudProvider: String = "",
    val cloudModelName: String = "",
    val apiKey: String = "",
    val endpointUrl: String = "",
    val headers: Map<String, String> = emptyMap(),
    val ggufFilePath: File? = null,
    val ggufFileName: String = "",
    val ggufFileSizeBytes: Long = 0,
    val ggufQuantization: String = "",
    val ggufContextLength: Int = 32768,
    val ggufParameters: Int = 0,
    val ggufThreads: Int = 4,
    val ggufGpuLayers: Int = 0,
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val maxTokens: Int = 4096,
    val maxContextTokens: Int = 128000,
    val enableReasoning: Boolean = true,
    val enableToolCalls: Boolean = true,
    val enableVision: Boolean = false,
    val modelSource: ModelSource = ModelSource.HYBRID,
    val projectWorkspacePath: String = ""
)

// Default model configurations
object DefaultModelConfigs {
    fun defaultOpenCodeConfig(): ModelConfig = ModelConfig(
        agentType = AgentType.OPENCODE,
        cloudProvider = "anthropic",
        cloudModelName = "claude-sonnet-5-5",
        temperature = 0.7f,
        topP = 0.95f,
        maxTokens = 4096,
        maxContextTokens = 128000,
        enableReasoning = true,
        enableToolCalls = true,
        enableVision = false,
        modelSource = ModelSource.CLOUD_API
    )

    fun defaultDeepSeekConfig(): ModelConfig = ModelConfig(
        agentType = AgentType.DEEPSEEK,
        cloudProvider = "deepseek",
        cloudModelName = "deepseek-coder",
        temperature = 0.7f,
        topP = 0.95f,
        maxTokens = 4096,
        maxContextTokens = 128000,
        enableReasoning = true,
        enableToolCalls = true,
        enableVision = false,
        modelSource = ModelSource.CLOUD_API
    )

    fun defaultClaudeConfig(): ModelConfig = ModelConfig(
        agentType = AgentType.CLAUDE,
        cloudProvider = "anthropic",
        cloudModelName = "claude-3.5-sonnet",
        temperature = 0.7f,
        topP = 0.95f,
        maxTokens = 4096,
        maxContextTokens = 128000,
        enableReasoning = true,
        enableToolCalls = true,
        enableVision = false,
        modelSource = ModelSource.CLOUD_API
    )

    fun defaultPiConfig(): ModelConfig = ModelConfig(
        agentType = AgentType.PI,
        cloudProvider = "pi",
        cloudModelName = "pi-1",
        temperature = 0.7f,
        topP = 0.95f,
        maxTokens = 4096,
        maxContextTokens = 128000,
        enableReasoning = true,
        enableToolCalls = true,
        enableVision = false,
        modelSource = ModelSource.CLOUD_API
    )

    fun defaultCustomConfig(): ModelConfig = ModelConfig(
        agentType = AgentType.CUSTOM,
        temperature = 0.7f,
        topP = 0.95f,
        maxTokens = 4096,
        maxContextTokens = 128000,
        enableReasoning = true,
        enableToolCalls = true,
        enableVision = false,
        modelSource = ModelSource.HYBRID
    )
}
