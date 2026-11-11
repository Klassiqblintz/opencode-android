package com.opencode.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Main ViewModel - manages app-wide state:
 * - Selected agent type (OpenCode, DeepSeek, Claude Code, Pi, Custom)
 * - Selected model config (API keys, GGUF models)
 * - Selected project
 * - Connection mode (local vs remote)
 * - Notification preferences
 */
class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    var selectedAgentType: AgentType? by mutableStateOf(null)
        private set

    // Selected model config
    var selectedModelConfig: ModelConfig? by mutableStateOf(null)
        private set

    // Selected project ID
    var selectedProjectId: Long? by mutableStateOf(null)
        private set

    // Connection mode for current session
    var currentRunMode: AgentRunMode = AgentRunMode.ON_DEVICE

    // Notification preferences
    var notificationsEnabled: Boolean = true
        private set

    // Per-project project settings
    var projectSettings = mutableMapOf<Long, ProjectSettings>()

    /**
     * Set the selected agent type
     */
    fun setAgentType(agentType: AgentType) {
        selectedAgentType = agentType
        _uiState.value = _uiState.value.copy(selectedAgentType = agentType)
        Log.d(TAG, "Agent selected: ${agentType.displayName}")
    }

    /**
     * Set the selected model config
     */
    fun setModelConfig(config: ModelConfig) {
        selectedModelConfig = config
        _uiState.value = _uiState.value.copy(selectedModelConfig = config)
        Log.d(TAG, "Model selected: ${config.cloudModelName ?: config.ggufFileName}")
    }

    /**
     * Set the selected project
     */
    fun setProjectId(projectId: Long) {
        selectedProjectId = projectId
        _uiState.value = _uiState.value.copy(selectedProjectId = projectId)
    }

    /**
     * Toggle notifications
     */
    fun toggleNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    /**
     * Set connection/run mode
     */
    fun setRunMode(mode: AgentRunMode) {
        currentRunMode = mode
        _uiState.value = _uiState.value.copy(runMode = mode)
    }

    /**
     * Configure a project's settings
     */
    fun configureProjectSettings(
        projectId: Long,
        settings: ProjectSettings
    ) {
        projectSettings[projectId] = settings
    }

    /**
     * Get or create project settings
     */
    fun getProjectSettings(projectId: Long): ProjectSettings {
        return projectSettings.getOrPut(projectId) {
            ProjectSettings(
                projectId = projectId,
                notificationsEnabled = true,
                autoSaveEnabled = true,
                compactThreshold = 60000,
            )
        }
    }

    /**
     * Handle permissions being granted
     */
    fun onPermissionsGranted() {
        _uiState.value = _uiState.value.copy(permissionsGranted = true)
    }

    /**
     * Handle notification permission
     */
    fun onNotificationPermission(granted: Boolean) {
        if (granted) {
            notificationsEnabled = true
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}

/**
 * UI state for the main screen
 */
data class AppUiState(
    val selectedAgentType: AgentType? = null,
    val selectedModelConfig: ModelConfig? = null,
    val selectedProjectId: Long? = null,
    val runMode: AgentRunMode = AgentRunMode.ON_DEVICE,
    val permissionsGranted: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * Per-project settings
 */
data class ProjectSettings(
    val projectId: Long,
    val notificationsEnabled: Boolean = true,
    val autoSaveEnabled: Boolean = true,
    val compactThreshold: Int = 60000, // tokens before context compaction
)
