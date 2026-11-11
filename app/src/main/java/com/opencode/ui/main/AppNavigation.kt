package com.opencode.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.opencode.R
import com.opencode.ui.components.common.LoadingDialog
import com.opencode.ui.components.chat.ChatMessages
import com.opencode.ui.components.builds.BuildOutput
import com.opencode.ui.components.terminal.TerminalView
import com.opencode.ui.components.preview.WebPreview
import com.opencode.ui.projects.ProjectsScreen
import com.opencode.ui.sessions.SessionsScreen
import com.opencode.ui.chat.ChatScreen
import com.opencode.ui.builds.BuildsScreen
import com.opencode.ui.settings.SettingsScreen
import com.opencode.ui.model.ModelSelectionScreen
import com.opencode.ui.agent.AgentSelectionScreen
import com.opencode.ui.upload.UploadScreen
import com.opencode.ui.terminal.TerminalScreen
import com.opencode.ui.preview.PreviewScreen
import com.opencode.ui.permissions.PermissionsScreen
import com.opencode.ui.skills.SkillsScreen
import com.opencode.ui.mcps.McpServersScreen
import com.opencode.ui.about.AboutScreen

/**
 * Main app bottom navigation with 5 tabs:
 * 1. Home / Chat
 * 2. Sessions
 * 3. Projects
 * 4. Builds
 * 5. Settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    onNavigateToAgentSelection: () -> Unit,
    onNavigateToModelSelection: () -> Unit,
    onNavigateToProjectImport: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToTerminal: () -> Unit,
    onNavigateToPreview: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToMcpServers: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSessions: () -> Unit,
    onNavigateToBuilds: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem(
            label = stringResource(R.string.nav_home),
            icon = Icons.Default.Home,
            route = "home"
        ),
        TabItem(
            label = stringResource(R.string.nav_sessions),
            icon = Icons.Default.Message,
            route = "sessions"
        ),
        TabItem(
            label = stringResource(R.string.nav_projects),
            icon = Icons.Default.Devices,
            route = "projects"
        ),
        TabItem(
            label = stringResource(R.string.nav_builds),
            icon = Icons.Default.Build,
            route = "builds"
        ),
        TabItem(
            label = stringResource(R.string.nav_settings),
            icon = Icons.Default.Settings,
            route = "settings"
        ),
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, tab ->
                    BottomNavigationItem(
                        icon = { Icon(tab.icon, tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            // Navigate to the appropriate route based on tab
                            when (tab.route) {
                                "home" -> navController.navigate("home") { popUpTo("home") { inclusive = true } }
                                "sessions" -> navController.navigate("sessions") { popUpTo("sessions") { inclusive = true } }
                                "projects" -> navController.navigate("projects") { popUpTo("projects") { inclusive = true } }
                                "builds" -> navController.navigate("builds") { popUpTo("builds") { inclusive = true } }
                                "settings" -> navController.navigate("settings") { popUpTo("settings") { inclusive = true } }
                            }
                        },
                        alwaysShowLabel = true,
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            // HOME - Chat screen with embedded terminal + preview buttons
            composable("home") {
                ChatScreen(
                    onOpenTerminal = { onNavigateToTerminal() },
                    onOpenPreview = { onNavigateToPreview() },
                    onNewSession = { navController.navigate("sessions/new") },
                    onOpenSettings = { onNavigateToSettings() },
                )
            }

            // SESSIONS
            composable("sessions") {
                SessionsScreen(
                    onBack = { /* stays in sessions */ },
                    onNewSession = { navController.navigate("sessions/new") },
                    onOpenChat = { sessionId ->
                        navController.navigate("chat/$sessionId")
                    },
                )
            }
            composable("sessions/new") {
                // Create new session
                SessionsScreen(
                    onBack = { navController.popBackStack() },
                    onNewSession = { /* handled internally */ },
                    onOpenChat = { sessionId ->
                        navController.popBackStack()
                        navController.navigate("chat/$sessionId")
                    },
                )
            }

            // CHAT (from session detail)
            composable("chat/{sessionId}") { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId")?.toLongOrNull()
                if (sessionId != null) {
                    ChatScreen(
                        sessionId = sessionId,
                        onBack = { navController.popBackStack() },
                        onOpenTerminal = { onNavigateToTerminal() },
                        onOpenPreview = { onNavigateToPreview() },
                        onNewSession = { navController.navigate("sessions/new") },
                        onOpenSettings = { onNavigateToSettings() },
                    )
                }
            }

            // PROJECTS
            composable("projects") {
                ProjectsScreen(
                    onBack = { /* stays */ },
                    onImportProject = { onNavigateToProjectImport() },
                    onUploadFiles = { onNavigateToUpload() },
                    onOpenBuild = { onNavigateToBuilds() },
                    onOpenTerminal = { onNavigateToTerminal() },
                    onOpenPreview = { onNavigateToPreview() },
                    onOpenSettings = { onNavigateToSettings() },
                )
            }

            // BUILDS
            composable("builds") {
                BuildsScreen(
                    onBack = { /* stays */ },
                    onOpenTerminal = { onNavigateToTerminal() },
                    onOpenPreview = { onNavigateToPreview() },
                    onOpenSettings = { onNavigateToSettings() },
                )
            }

            // SETTINGS
            composable("settings") {
                SettingsScreen(
                    onBack = { /* stays */ },
                    onNavigateToAgentSelection = { onNavigateToAgentSelection() },
                    onNavigateToModelSelection = { onNavigateToModelSelection() },
                    onNavigateToMcpServers = { onNavigateToMcpServers() },
                    onNavigateToSkills = { onNavigateToSkills() },
                    onNavigateToPermissions = { onNavigateToPermissions() },
                    onNavigateToAbout = { onNavigateToAbout() },
                )
            }
        }
    }
}

/**
 * Tab item for bottom navigation
 */
data class TabItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)
