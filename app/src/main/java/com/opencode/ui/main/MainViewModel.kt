package com.opencode.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.engine.AgentType
import com.opencode.engine.AgentRunMode
import com.opencode.engine.ModelConfig
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AppUiState(
    val selectedAgentType: AgentType? = null,
    val selectedModelConfig: ModelConfig? = null,
    val selectedProjectId: Long? = null,
    val currentRunMode: AgentRunMode = AgentRunMode.ON_DEVICE,
    val notificationsEnabled: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val biometricLock: Boolean = false,
    val projectSettings: Map<Long, ProjectSettings> = emptyMap()
)

enum class ThemeMode { LIGHT, DARK, FOLLOW_SYSTEM }

data class ProjectSettings(
    val projectId: Long,
    val projectType: String = "MIXED",
    val buildVariant: String = "RELEASE",
    val terminalHistory: List<String> = emptyList(),
    val uploadPath: String = "",
    val mcpServerIds: List<Long> = emptyList(),
    val skillIds: List<Long> = emptyList(),
    val permissionOverrides: Map<String, String> = emptyMap()
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun selectAgentType(agentType: AgentType?) {
        _uiState.update { it.copy(selectedAgentType = agentType) }
    }

    fun selectModelConfig(config: ModelConfig?) {
        _uiState.update { it.copy(selectedModelConfig = config) }
    }

    fun selectProject(projectId: Long?) {
        _uiState.update { it.copy(selectedProjectId = projectId) }
        if (projectId != null) {
            viewModelScope.launch {
                // Touch the project's lastUsed
            }
        }
    }

    fun setRunMode(mode: AgentRunMode) {
        _uiState.update { it.copy(currentRunMode = mode) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setBiometricLock(enabled: Boolean) {
        _uiState.update { it.copy(biometricLock = enabled) }
    }

    fun saveProjectSettings(projectId: Long, settings: ProjectSettings) {
        _uiState.update {
            val updated = it.projectSettings.toMutableMap()
            updated[projectId] = settings
            it.copy(projectSettings = updated)
        }
    }

    fun getProjectSettings(projectId: Long): ProjectSettings? {
        return _uiState.value.projectSettings[projectId]
    }
}
