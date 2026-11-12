package com.opencode.engine

// AgentRunMode: how the engine runs
enum class AgentRunMode {
    ON_DEVICE,    // Root Linux on device
    REMOTE_SERVER // HTTP/WebSocket connection
}

// ModelSource: where the model lives
enum class ModelSource {
    CLOUD_API,     // Remote LLM provider
    LOCAL_GGUF,    // llama.cpp local model
    HYBRID         // Both, auto-switch
}

// AgentType: built-in coding agents
enum class AgentType(
    val id: String,
    val displayName: String,
    val accentColor: Long,  // ARGB as long
    val description: String,
    val supportsLocal: Boolean,
    val supportsRemote: Boolean,
    val supportsGguf: Boolean,
    val supportsCloudApi: Boolean,
    val supportsVision: Boolean,
    val supportsReasoning: Boolean,
    val supportsToolCalls: Boolean
) {
    OPENCODE(
        id = "opencode",
        displayName = "OpenCode",
        accentColor = 0xFF1E88E5,
        description = "All-purpose coding agent with full capabilities",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true
    ),
    DEEPSEEK(
        id = "deepseek",
        displayName = "DeepSeek",
        accentColor = 0xFF7C4DFF,
        description = "Specialized code reasoning agent",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true
    ),
    CLAUDE(
        id = "claude",
        displayName = "Claude",
        accentColor = 0xFF7E57C2,
        description = "Advanced reasoning and analysis",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true
    ),
    PI(
        id = "pi",
        displayName = "Pi",
        accentColor = 0xFF00BFA5,
        description = "Personal coding assistant",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = false,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true
    ),
    CUSTOM(
        id = "custom",
        displayName = "Custom",
        accentColor = 0xFF607D8B,
        description = "Bring your own model",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true
    );

    companion object {
        fun fromId(id: String): AgentType? = values().find { it.id == id }
    }
}

// SystemKey: keyboard keys for the bridge
enum class SystemKey {
    BACK, HOME, RECENT,
    VOLUME_UP, VOLUME_DOWN,
    POWER, ENTER, BACKSPACE, TAB, ESCAPE
}
