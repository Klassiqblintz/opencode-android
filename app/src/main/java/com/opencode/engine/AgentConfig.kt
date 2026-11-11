package com.opencode.engine

import java.io.File

/**
 * Complete agent configuration — all settings for each agent type.
 * Each agent can run on-device (pRoot) or connect to a remote server,
 * with cloud API keys, local GGUF models, or hybrid mode.
 */
data class AgentConfig(
    val agentType: AgentType,
    val runMode: AgentRunMode = AgentRunMode.ON_DEVICE,

    // On-device settings
    val rootfsType: String? = null,            // "proot", "chroot", etc.
    val rootfsPath: String? = null,            // Path to pRoot Linux rootfs
    val linuxPackages: List<String> = emptyList(), // Pre-installed packages (gradle, flutter, cmake, etc.)

    // Local GGUF settings
    val ggufFilePath: File? = null,
    val ggufFileName: String? = null,
    val ggufQuantization: String = "Q4_K_M",
    val ggufContextLength: Int = 32768,
    val ggufThreads: Int = 4,
    val ggufGpuLayers: Int = 0,               // 0 = CPU only, > 0 = Vulkan GPU layers

    // Cloud API settings
    val apiKey: String? = null,
    val endpointUrl: String? = null,
    val headers: Map<String, String> = emptyMap(),

    // Remote server settings (for REMOTE_SERVER mode)
    val remoteServerUrl: String? = null,
    val remoteServerToken: String? = null,
    val remoteServerHeaders: Map<String, String> = emptyMap(),

    // Inference settings
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val topK: Int = 20,
    val maxTokens: Int = 4096,
    val maxContextTokens: Int = 128000,
    val maxConversationTurns: Int = 50,
    val compactThresholdTokens: Int = 60000,

    // Feature toggles
    val enableReasoning: Boolean = true,
    val enableToolCalls: Boolean = true,
    val enableVision: Boolean = false,
    val enableCodeMode: Boolean = true,
    val enableSubagents: Boolean = true,
    val enableSkills: Boolean = true,
    val enableMcpServers: Boolean = true,

    // Notifications
    val enableNotifications: Boolean = true,
    val notifyOnCompletion: Boolean = true,
    val notifyOnPermissions: Boolean = true,
    val notifyOnBuildComplete: Boolean = true,

    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
) {
    /**
     * Get the effective model source based on configuration
     */
    fun modelSource(): ModelSource {
        return when (runMode) {
            AgentRunMode.ON_DEVICE -> {
                if (ggufFilePath != null && ggufFilePath.exists()) {
                    ModelSource.LOCAL_GGUF
                } else {
                    ModelSource.HYBRID
                }
            }
            AgentRunMode.REMOTE_SERVER -> {
                if (apiKey != null || remoteServerUrl != null) {
                    ModelSource.CLOUD_API
                } else {
                    ModelSource.HYBRID
                }
            }
        }
    }

    /**
     * Validate that the configuration is complete for the selected mode
     */
    fun isValid(): Boolean {
        return when (runMode) {
            AgentRunMode.ON_DEVICE -> ggufFilePath != null || modelSource() == ModelSource.HYBRID
            AgentRunMode.REMOTE_SERVER -> apiKey != null || remoteServerUrl != null
        }
    }
}
