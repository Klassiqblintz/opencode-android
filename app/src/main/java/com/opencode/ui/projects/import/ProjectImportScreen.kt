package com.opencode.ui.projects.import

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.utils.ProjectType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProjectImportScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onImported: (Long) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var importMode by remember { mutableStateOf<ImportMode?>(null) }
    var isImporting by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var detectedType by remember { mutableStateOf<ProjectType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import Project") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (detectedType != null && !isImporting) {
                        IconButton(onClick = {
                            // Confirm import
                            onImported(1L)
                            onDismiss()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm"
                            )
                        }
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
                text = "Import Mode",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Import mode selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    onClick = { importMode = ImportMode.ZIP },
                    colors = CardDefaults.cardColors(
                        containerColor = if (importMode == ImportMode.ZIP)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Compress,
                            contentDescription = "ZIP",
                            tint = if (importMode == ImportMode.ZIP)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("ZIP File", fontSize = 12.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    onClick = { importMode = ImportMode.FOLDER },
                    colors = CardDefaults.cardColors(
                        containerColor = if (importMode == ImportMode.FOLDER)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = "Folder",
                            tint = if (importMode == ImportMode.FOLDER)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                        Text("Folder", fontSize = 12.sp)
                    }
                }
            }

            // ZIP import card
            if (importMode == ImportMode.ZIP) {
                Spacer(Modifier.height(16.dp))
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Select a ZIP file to import",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {
                            scope.launch {
                                isImporting = true
                                // Simulate progress
                                delay(2500L)
                                progress = 1f
                                detectedType = ProjectType.KOTLIN_COMPOSE
                                isImporting = false
                            }
                        }) {
                            Text("Select ZIP File")
                        }
                    }
                }

                // Import progress
                if (isImporting || progress > 0f) {
                    Spacer(Modifier.height(16.dp))
                    Card {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Importing... ${"%.0f".format(progress * 100)}%")
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Detected type
                if (detectedType != null) {
                    Spacer(Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Project Type Detected",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = detectedType!!.displayName,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Success card
                if (progress >= 1f) {
                    Spacer(Modifier.height(16.dp))
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Imported successfully!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onImported(1L) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Open Project")
                                }
                                OutlinedButton(
                                    onClick = { onDismiss() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }

            // Folder import
            if (importMode == ImportMode.FOLDER) {
                Spacer(Modifier.height(16.dp))
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Select a folder to import",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {
                            scope.launch {
                                isImporting = true
                                delay(2000L)
                                progress = 1f
                                detectedType = ProjectType.KOTLIN_COMPOSE
                                isImporting = false
                            }
                        }) {
                            Text("Select Folder")
                        }
                    }
                }
            }
        }
    }
}

enum class ImportMode { ZIP, FOLDER }
