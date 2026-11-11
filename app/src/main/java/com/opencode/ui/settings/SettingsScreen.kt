package com.opencode.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Settings screen - all app settings
 * Sections:
 * - Agent selection & configuration
 * - Models & API keys
 * - MCP Servers
 * - Skills
 * - Permissions
 * - Storage
 * - About
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToAgentSelection: () -> Unit,
    onNavigateToModelSelection: () -> Unit,
    onNavigateToMcpServers: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.titleLarge)

            // Notification toggle
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { notificationsEnabled = !notificationsEnabled },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Push Notifications",
                            style = MaterialTheme.typography.titleMedium)
                        Text("Get notified when builds complete, permissions needed, etc.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }

            // Settings menu items
            Text("Configuration", style = MaterialTheme.typography.titleMedium)

            SettingsItem(
                icon = Icons.Default.Person,
                title = "Agent Selection",
                subtitle = "Choose: OpenCode, DeepSeek, Claude Code, Pi, or Custom",
                onClick = onNavigateToAgentSelection
            )

            SettingsItem(
                icon = Icons.Default.Memory,
                title = "Models & API Keys",
                subtitle = "Configure cloud APIs and local GGUF models",
                onClick = onNavigateToModelSelection
            )

            SettingsItem(
                icon = Icons.Default.Devices,
                title = "MCP Servers",
                subtitle = "Connect and manage MCP servers",
                onClick = onNavigateToMcpServers
            )

            SettingsItem(
                icon = Icons.Default.SmartButton,
                title = "Skills",
                subtitle = "Manage agent skills and workflows",
                onClick = onNavigateToSkills
            )

            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Permissions",
                subtitle = "Configure tool and action permissions",
                onClick = onNavigateToPermissions
            )

            SettingsItem(
                icon = Icons.Default.Storage,
                title = "Storage",
                subtitle = "Manage project storage and cleanup",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "Version, license, credits",
                onClick = onNavigateToAbout
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, title, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Icon(Icons.Default.ChevronRight, "Navigate to $title")
        }
    }
}
