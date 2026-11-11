package com.opencode.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.rememberNavController
import com.opencode.engine.AgentType
import com.opencode.ui.projects.import.ProjectImportScreen
import com.opencode.ui.chat.ChatScreen
import com.opencode.ui.builds.BuildsScreen
import com.opencode.ui.settings.SettingsScreen
import com.opencode.ui.model.ModelSelectionScreen
import com.opencode.ui.agent.AgentSelectionScreen
import com.opencode.ui.projects.ProjectsScreen
import com.opencode.ui.upload.UploadScreen
import com.opencode.ui.terminal.TerminalScreen
import com.opencode.ui.preview.PreviewScreen
import com.opencode.ui.about.AboutScreen
import com.opencode.ui.components.common.LoadingDialog
import com.opencode.ui.skills.SkillsScreen
import com.opencode.ui.mcps.McpServersScreen
import com.opencode.ui.permissions.PermissionsScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - entry point for the entire app
 * Handles all permissions, navigation, and app lifecycle
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val snackbarHostState = SnackbarHostState()

    // Permission launchers
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.onPermissionsGranted()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onNotificationPermission(granted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge with dark status bar
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )

        // Handle window insets for edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            show(WindowInsetsCompat.Type.ime())
        }
        // Use light status bar icon color on dark backgrounds
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
            SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // Clear navigation bar brightness auto-switch
        window.navigationBarColor = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        // Request permissions on first launch
        requestPermissions()

        setContent {
            var showAgentSelection by remember { mutableStateOf(false) }
            var showModelSelection by remember { mutableStateOf(false) }
            var showProjectImport by remember { mutableStateOf(false) }
            var showUpload by remember { mutableStateOf(false) }
            var showTerminal by remember { mutableStateOf(false) }
            var showPreview by remember { mutableStateOf(false) }
            var showPermissions by remember { mutableStateOf(false) }
            var showSkills by remember { mutableStateOf(false) }
            var showMcpServers by remember { mutableStateOf(false) }
            var showAbout by remember { mutableStateOf(false) }
            var showSettings by remember { mutableStateOf(false) }
            var showSessions by remember { mutableStateOf(false) }
            var showBuilds by remember { mutableStateOf(false) }

            val navController = rememberNavController()

            // Main app navigation
            AppNavigation(
                navController = navController,
                onNavigateToAgentSelection = { showAgentSelection = true },
                onNavigateToModelSelection = { showModelSelection = true },
                onNavigateToProjectImport = { showProjectImport = true },
                onNavigateToUpload = { showUpload = true },
                onNavigateToTerminal = { showTerminal = true },
                onNavigateToPreview = { showPreview = true },
                onNavigateToPermissions = { showPermissions = true },
                onNavigateToSkills = { showSkills = true },
                onNavigateToMcpServers = { showMcpServers = true },
                onNavigateToAbout = { showAbout = true },
                onNavigateToSettings = { showSettings = true },
                onNavigateToSessions = { showSessions = true },
                onNavigateToBuilds = { showBuilds = true },
                snackbarHostState = snackbarHostState,
            )

            // Full-screen dialogs for sub-features
            if (showAgentSelection) {
                AgentSelectionScreen(
                    onSelected = { agentType ->
                        viewModel.selectedAgentType = agentType
                        showAgentSelection = false
                    },
                    onDismiss = { showAgentSelection = false }
                )
            }

            if (showModelSelection) {
                ModelSelectionScreen(
                    onSelected = { modelConfig ->
                        viewModel.selectedModelConfig = modelConfig
                        showModelSelection = false
                    },
                    onDismiss = { showModelSelection = false }
                )
            }

            if (showProjectImport) {
                ProjectImportScreen(
                    onImported = { projectId ->
                        viewModel.selectedProjectId = projectId
                        showProjectImport = false
                    },
                    onDismiss = { showProjectImport = false }
                )
            }

            if (showUpload) {
                UploadScreen(
                    onUploadComplete = {
                        showUpload = false
                    },
                    onDismiss = { showUpload = false }
                )
            }

            if (showTerminal) {
                TerminalScreen(
                    onBack = { showTerminal = false }
                )
            }

            if (showPreview) {
                PreviewScreen(
                    onBack = { showPreview = false }
                )
            }

            if (showPermissions) {
                PermissionsScreen(
                    onBack = { showPermissions = false }
                )
            }

            if (showSkills) {
                SkillsScreen(
                    onBack = { showSkills = false }
                )
            }

            if (showMcpServers) {
                McpServersScreen(
                    onBack = { showMcpServers = false }
                )
            }

            if (showAbout) {
                AboutScreen(
                    onBack = { showAbout = false }
                )
            }

            // Snackbar
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }

    /**
     * Request all necessary permissions
     */
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ needs separate notification permission
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Storage permissions
        val storagePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

        if (storagePermissions.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            storagePermissionLauncher.launch(storagePermissions)
        }

        // Always request these
        val alwaysNeeded = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.USE_BIOMETRIC,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE,
        )
        if (alwaysNeeded.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            storagePermissionLauncher.launch(alwaysNeeded)
        }
    }
}
