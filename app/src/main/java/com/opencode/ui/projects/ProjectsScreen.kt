package com.opencode.ui.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Projects screen - manage coding projects
 * Features:
 * - Import project from ZIP
 * - Open recent projects
 * - Download project as ZIP
 * - File browser
 * - Upload files to project
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    onBack: () -> Unit,
    onImportProject: () -> Unit,
    onUploadFiles: () -> Unit,
    onOpenBuild: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenPreview: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    var showImportDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Projects") },
                actions = {
                    IconButton(onClick = onUploadFiles) {
                        Icon(Icons.Default.UploadFile, "Upload Files")
                    }
                    IconButton(onClick = onImportProject) {
                        Icon(Icons.Default.Add, "Import Project")
                    }
                    IconButton(onClick = onOpenTerminal) {
                        Icon(Icons.Default.Terminal, "Terminal")
                    }
                    IconButton(onClick = onOpenPreview) {
                        Icon(Icons.Default.Web, "Preview")
                    }
                    IconButton(onClick = onOpenSettings) {
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
            Text("Your Projects", style = MaterialTheme.typography.titleLarge)
            Text("Import a project via ZIP or create a new one",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showImportDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.UploadFile, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Import ZIP")
                }
                OutlinedButton(
                    onClick = { onOpenBuild() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Build, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Build")
                }
            }

            // Recent projects list
            Text("Recent Projects", style = MaterialTheme.typography.titleMedium)

            // Placeholder for project list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Folder, null, tint = MaterialTheme.colorScheme.primary)
                            Column(modifier = Modifier.weight(1f)) {
                                Text("My Project", style = MaterialTheme.typography.titleMedium)
                                Text("Flutter • Last used: today",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            IconButton(onClick = { /* Download as ZIP */ }) {
                                Icon(Icons.Default.Download, "Download ZIP")
                            }
                            IconButton(onClick = { /* Delete */ }) {
                                Icon(Icons.Default.Delete, "Delete")
                            }
                        }
                    }
                }
            }

            // Import dialog
            if (showImportDialog) {
                ImportProjectDialog(
                    onDismiss = { showImportDialog = false },
                    onImported = { /* Handle imported project */ }
                )
            }
        }
    }
}

/**
 * Import project dialog - choose ZIP file or folder
 */
@Composable
private fun ImportProjectDialog(
    onDismiss: () -> Unit,
    onImported: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Import Project") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Choose how to import your project",
                    style = MaterialTheme.typography.bodyMedium)

                // ZIP file
                Text("From ZIP file:", style = MaterialTheme.typography.titleSmall)
                Button(onClick = { /* Open file picker for ZIP */ }) {
                    Text("Select ZIP File")
                }

                // Folder from device
                Text("From device folder:", style = MaterialTheme.typography.titleSmall)
                Button(onClick = { /* Open folder picker */ }) {
                    Text("Select Folder")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
