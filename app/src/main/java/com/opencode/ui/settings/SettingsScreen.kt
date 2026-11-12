package com.opencode.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onAbout: () -> Unit = {},
    onAgentSelection: () -> Unit = {},
    onModelSelection: () -> Unit = {},
    onMcpServers: () -> Unit = {},
    onSkills: () -> Unit = {},
    onPermissions: () -> Unit = {},
    onStorage: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Notifications toggle
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Notifications", fontWeight = FontWeight.Medium)
                        Text("Enable/disable notifications", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                    }
                    Switch(
                        checked = true,
                        onCheckedChange = {}
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Configuration section
            Text(
                text = "Configuration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SettingsItem(
                icon = Icons.Default.Person,
                title = "Agent Selection",
                subtitle = "Select your coding agent",
                onClick = onAgentSelection
            )
            SettingsItem(
                icon = Icons.Default.Cloud,
                title = "Models & API Keys",
                subtitle = "Configure cloud and local models",
                onClick = onModelSelection
            )
            SettingsItem(
                icon = Icons.Default.Devices,
                title = "MCP Servers",
                subtitle = "Manage MCP servers",
                onClick = onMcpServers
            )
            SettingsItem(
                icon = Icons.Default.SmartButton,
                title = "Skills",
                subtitle = "Discover and manage skills",
                onClick = onSkills
            )
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Permissions",
                subtitle = "Configure tool and action permissions",
                onClick = onPermissions
            )
            SettingsItem(
                icon = Icons.Default.Storage,
                title = "Storage",
                subtitle = "Manage storage usage",
                onClick = onStorage
            )
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "Version and license information",
                onClick = onAbout
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Spacer(Modifier.height(8.dp))
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(0.3f)
            )
        }
    }
}
