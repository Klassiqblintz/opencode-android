package com.opencode.engine

import com.opencode.engine.AgentType.DEEPSEEK

/**
 * Default model configurations for each built-in agent type.
 * Users can modify these or add their own.
 */
object DefaultModelConfigs {

    /**
     * Default config for OpenCode agent
     */
    fun defaultOpenCodeConfig(): ModelConfig {
        return ModelConfig(
            agentType = OPENCODE,
            cloudProvider = "anthropic",
            cloudModelName = "claude-sonnet-5-5",
            temperature = 0.7f,
            topP = 0.95f,
            maxTokens = 4096,
            maxContextTokens = 128000,
            enableReasoning = true,
            enableToolCalls = true,
            enableVision = false,
        )
    }

    /**
     * Default config for DeepSeek agent
     */
    fun defaultDeepSeekConfig(): ModelConfig {
        return ModelConfig(
            agentType = DEEPSEEK,
            cloudProvider = "deepseek",
            cloudModelName = "deepseek-coder",
            temperature = 0.6f,
            topP = 0.9f,
            maxTokens = 8192,
            maxContextTokens = 128000,
            enableReasoning = true,
            enableToolCalls = true,
            enableVision = false,
        )
    }

    /**
     * Default config for Claude Code agent
     */
    fun defaultClaudeConfig(): ModelConfig {
        return ModelConfig(
            agentType = CLAUDE,
            cloudProvider = "anthropic",
            cloudModelName = "claude-sonnet-5-5",
            temperature = 0.7f,
            topP = 0.95f,
            maxTokens = 4096,
            maxContextTokens = 128000,
            enableReasoning = true,
            enableToolCalls = true,
            enableVision = false,
        )
    }

    /**
     * Default config for Pi agent
     */
    fun defaultPiConfig(): ModelConfig {
        return ModelConfig(
            agentType = PI,
            cloudProvider = "pi",
            cloudModelName = "pi-2",
            temperature = 0.8f,
            topP = 0.95f,
            maxTokens = 4096,
            maxContextTokens = 32768,
            enableReasoning = true,
            enableToolCalls = true,
            enableVision = true,
        )
    }

    /**
     * Default config for custom agent (empty, user fills in)
     */
    fun defaultCustomConfig(): ModelConfig {
        return ModelConfig(
            agentType = CUSTOM,
            temperature = 0.7f,
            topP = 0.95f,
            maxTokens = 4096,
            maxContextTokens = 32768,
            enableReasoning = true,
            enableToolCalls = true,
            enableVision = false,
        )
    }

    /**
     * Get all default configs as a map (AgentType -> ModelConfig)
     */
    fun allDefaults(): Map<AgentType, ModelConfig> {
        return mapOf(
            OPENCODE to defaultOpenCodeConfig(),
            deepSEEK to defaultDeepSeekConfig(),
            CLAUDE to defaultClaudeConfig(),
            PI to defaultPiConfig(),
            CUSTOM to defaultCustomConfig(),
        )
    }
}
