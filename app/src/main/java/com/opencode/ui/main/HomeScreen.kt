package com.opencode.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Home screen - main chat interface with quick actions
 * Shows:
 * - Active sessions
 * - Quick project access
 * - Quick build access
 * - Agent status
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSessions: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToBuilds: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OpenCode") },
                actions = {
                    IconButton(onClick = { onNavigateToSettings() }) {
                        Icon(Icons.Default.Settings, "Settings")
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
            // Quick actions
            Text("Quick Actions", style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sessions button
                OutlinedButton(
                    onClick = { onNavigateToSessions() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Message, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sessions")
                }

                // Projects button
                OutlinedButton(
                    onClick = { onNavigateToProjects() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Devices, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Projects")
                }

                // Builds button
                OutlinedButton(
                    onClick = { onNavigateToBuilds() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Build, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Builds")
                }
            }

            // Recent projects
            Spacer(Modifier.height(16.dp))
            Text("Recent Projects", style = MaterialTheme.typography.titleMedium)
            // Will show list of recent projects here

            // Active builds
            Spacer(Modifier.height(16.dp))
            Text("Active Builds", style = MaterialTheme.typography.titleMedium)
            // Will show active builds here

            // Agent status
            Spacer(Modifier.height(16.dp))
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Agent Status", style = MaterialTheme.typography.titleSmall)
                        Text("Ready — all systems operational", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary)
                    }
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }
        }
    }
}
