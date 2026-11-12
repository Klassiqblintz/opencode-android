package com.opencode.engine

/**
 * Agent types supported by OpenCode Android.
 * Each agent has a unique accent color and capability flags.
 *
 * Agents:
 * - OPENCODE: OpenCode's own agent (blue)
 * - DEEPSEEK: DeepSeek Coder agent (purple)
 * - CLAUDE: Anthropic Claude Code agent (deep purple)
 * - PI: Pi Agent (teal)
 * - CUSTOM: Custom user-configured agent (blue-gray)
 */
enum class AgentType(
    val id: String,
    val displayName: String,
    val accentColor: Long,    // ARGB color as long
    val description: String,
    val supportsLocal: Boolean,
    val supportsRemote: Boolean,
    val supportsGguf: Boolean,
    val supportsCloudApi: Boolean,
    val supportsVision: Boolean,
    val supportsReasoning: Boolean,
    val supportsToolCalls: Boolean,
) {
    /**
     * OpenCode's own agent — the default
     * Blue accent: 0xFF1E88E5
     */
    OPENCODE(
        id = "opencode",
        displayName = "OpenCode",
        accentColor = 0xFF1E88E5,
        description = "OpenCode's native agent — full feature set with Code Mode, subagents, and all tools.",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true,
    ),

    /**
     * DeepSeek Coder agent
     * Purple accent: 0xFF7C4DFF
     */
    DEEPSEEK(
        id = "deepseek",
        displayName = "DeepSeek",
        accentColor = 0xFF7C4DFF,
        description = "DeepSeek Coder — specialized for code generation and understanding.",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = false,
        supportsCloudApi = true,
        supportsVision = false,
        supportsReasoning = true,
        supportsToolCalls = true,
    ),

    /**
     * Anthropic Claude Code agent
     * Deep purple accent: 0xFF7E57C2
     */
    CLAUDE(
        id = "claude",
        displayName = "Claude Code",
        accentColor = 0xFF7E57C2,
        description = "Anthropic Claude Code — advanced reasoning and multi-modal capabilities.",
        supportsLocal = false,
        supportsRemote = true,
        supportsGguf = false,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true,
    ),

    /**
     * Pi Agent
     * Teal accent: 0xFF00BFA5
     */
    PI(
        id = "pi",
        displayName = "Pi Agent",
        accentColor = 0xFF00BFA5,
        description = "Pi Agent — conversational coding assistant with multi-modal support.",
        supportsLocal = false,
        supportsRemote = true,
        supportsGguf = false,
        supportsCloudApi = true,
        supportsVision = true,
        supportsReasoning = true,
        supportsToolCalls = true,
    ),

    /**
     * Custom agent — user-configured
     * Blue-gray accent: 0xFF607D8B
     */
    CUSTOM(
        id = "custom",
        displayName = "Custom",
        accentColor = 0xFF607D8B,
        description = "Custom agent — configure your own endpoint, API key, and model.",
        supportsLocal = true,
        supportsRemote = true,
        supportsGguf = true,
        supportsCloudApi = true,
        supportsVision = false,
        supportsReasoning = true,
        supportsToolCalls = true,
    );

    /**
     * Get the hex color string for UI display
     */
    fun accentColorHex(): String {
        return String.format("#%08X", accentColor)
    }
}
