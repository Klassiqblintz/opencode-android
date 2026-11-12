package com.opencode.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.opencode.ui.chat.ChatScreen
import com.opencode.ui.preview.PreviewScreen
import com.opencode.ui.projects.ProjectsScreen
import com.opencode.ui.builds.BuildsScreen
import com.opencode.ui.settings.SettingsScreen
import com.opencode.ui.terminal.TerminalScreen
import com.opencode.ui.sessions.SessionsScreen
import com.opencode.ui.agent.AgentSelectionScreen
import com.opencode.ui.model.ModelSelectionScreen
import com.opencode.ui.mcps.McpServersScreen
import com.opencode.ui.skills.SkillsScreen
import com.opencode.ui.permissions.PermissionsScreen
import com.opencode.ui.projects.import.ProjectImportScreen
import com.opencode.ui.upload.UploadScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    onNavigateToSettings: () -> Unit = {}
) {
    val navController = rememberNavController()

    // Dialog state flags
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showAgentSelectionDialog by remember { mutableStateOf(false) }
    var showModelSelectionDialog by remember { mutableStateOf(false) }
    var showMcpServersDialog by remember { mutableStateOf(false) }
    var showSkillsDialog by remember { mutableStateOf(false) }
    var showPermissionsDialog by remember { mutableStateOf(false) }
    var showStorageDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var showTerminalDialog by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        onNavigateToSettings = { showSettingsDialog = true },
                        onNewSession = { navController.navigate("sessions") },
                        onImportProject = { showImportDialog = true },
                        onBuild = { navController.navigate("builds") },
                        onUploadFiles = { showUploadDialog = true }
                    )
                }
                composable("sessions") {
                    SessionsScreen(
                        onBack = { navController.popBackStack() },
                        onNewSession = { navController.navigate("chat") }
                    )
                }
                composable("projects") {
                    ProjectsScreen(
                        onBack = { navController.popBackStack() },
                        onOpenBuild = { navController.navigate("builds") },
                        onOpenTerminal = { showTerminalDialog = true },
                        onOpenPreview = { showPreviewDialog = true },
                        onOpenSettings = { showSettingsDialog = true }
                    )
                }
                composable("builds") {
                    BuildsScreen(
                        onBack = { navController.popBackStack() },
                        onOpenTerminal = { showTerminalDialog = true },
                        onOpenPreview = { showPreviewDialog = true },
                        onOpenSettings = { showSettingsDialog = true }
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                        onAbout = { showAboutDialog = true },
                        onAgentSelection = { showAgentSelectionDialog = true },
                        onModelSelection = { showModelSelectionDialog = true },
                        onMcpServers = { showMcpServersDialog = true },
                        onSkills = { showSkillsDialog = true },
                        onPermissions = { showPermissionsDialog = true },
                        onStorage = { showStorageDialog = true }
                    )
                }
            }

            // Dialog screens
            if (showAboutDialog) {
                AboutScreen(onBack = { showAboutDialog = false })
            }
            if (showAgentSelectionDialog) {
                AgentSelectionScreen(
                    onBack = { showAgentSelectionDialog = false },
                    onSelected = { /* handle agent selection */ },
                    onDismiss = { showAgentSelectionDialog = false }
                )
            }
            if (showModelSelectionDialog) {
                ModelSelectionScreen(
                    onBack = { showModelSelectionDialog = false },
                    onSelected = { /* handle model selection */ },
                    onDismiss = { showModelSelectionDialog = false }
                )
            }
            if (showMcpServersDialog) {
                McpServersScreen(
                    onBack = { showMcpServersDialog = false },
                    onDismiss = { showMcpServersDialog = false }
                )
            }
            if (showSkillsDialog) {
                SkillsScreen(
                    onBack = { showSkillsDialog = false },
                    onDismiss = { showSkillsDialog = false }
                )
            }
            if (showPermissionsDialog) {
                PermissionsScreen(
                    onBack = { showPermissionsDialog = false },
                    onDismiss = { showPermissionsDialog = false }
                )
            }
            if (showStorageDialog) {
                StorageScreen(
                    onBack = { showStorageDialog = false },
                    onDismiss = { showStorageDialog = false }
                )
            }
            if (showImportDialog) {
                ProjectImportScreen(
                    onBack = { showImportDialog = false },
                    onDismiss = { showImportDialog = false },
                    onImported = { projectId ->
                        showImportDialog = false
                        navController.navigate("projects")
                    }
                )
            }
            if (showUploadDialog) {
                UploadScreen(
                    onBack = { showUploadDialog = false },
                    onDismiss = { showUploadDialog = false },
                    onUploadComplete = {}
                )
            }
            if (showTerminalDialog) {
                TerminalScreen(
                    onBack = { showTerminalDialog = false; navController.popBackStack() },
                    onDismiss = { showTerminalDialog = false }
                )
            }
            if (showPreviewDialog) {
                PreviewScreen(
                    onBack = { showPreviewDialog = false; navController.popBackStack() },
                    onDismiss = { showPreviewDialog = false }
                )
            }
        }
    }
}

// Simple placeholder screens for dialogs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                contentDescription = "About",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("OpenCode Android", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Universal Coding Agent Platform", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
            Spacer(Modifier.height(24.dp))
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Version: 1.0.0")
                    Text("License: MIT")
                    Text("Min SDK: 26")
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("© 2026 OpenCode", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StorageScreen(onBack: () -> Unit, onDismiss: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Storage") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Storage management coming soon", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
        }
    }
}
