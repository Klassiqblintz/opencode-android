package com.opencode.ui.builds

package com.opencode.ui.builds

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencode.utils.ProjectType

/**
 * Builds screen - build and manage APKs
 * Supports all types:
 * - Flutter (pubspec.yaml)
 * - Kotlin/Compose (Gradle + Kotlin)
 * - Java/Gradle
 * - Native C/C++ (CMake/NDK)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildsScreen(
    onBack: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenPreview: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    var selectedProjectType by remember { mutableStateOf(ProjectType.FLUTTER) }
    var buildVariant by remember { mutableStateOf(BuildVariant.DEBUG) }
    var isBuilding by remember { mutableStateOf(false) }
    var buildOutput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Builds") },
                actions = {
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
            // Project type selection
            Text("Project Type", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProjectType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedProjectType == type,
                        onClick = { selectedProjectType = type },
                        label = { Text(type.displayName) },
                    )
                }
            }

            // Build variant
            Text("Build Variant", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = buildVariant == BuildVariant.DEBUG,
                    onClick = { buildVariant = BuildVariant.DEBUG },
                    label = { Text("Debug APK") },
                )
                FilterChip(
                    selected = buildVariant == BuildVariant.RELEASE,
                    onClick = { buildVariant = BuildVariant.RELEASE },
                    label = { Text("Release APK") },
                )
            }

            // Build button
            Button(
                onClick = { isBuilding = true },
                enabled = !isBuilding,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isBuilding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Building...")
                } else {
                    Icon(Icons.Default.Build, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Build ${buildVariant.name} APK")
                }
            }

            // Build output
            if (isBuilding || buildOutput.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text("Build Output", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        // Scrollable build output
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Black.copy(alpha = 0.1f))
                        ) {
                            Text(
                                text = buildOutput,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontSize = androidx.compose.ui.unit.sp(10),
                                modifier = Modifier.padding(4.dp)
                            )
                        }

                        // Install button (appears after successful build)
                        if (buildOutput.contains("BUILD SUCCESSFUL") || isBuilding) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { /* Install APK */ },
                                    modifier = Modifier.weight(1f),
                                    enabled = buildOutput.contains("BUILD SUCCESSFUL")
                                ) {
                                    Icon(Icons.Default.InstallMobile, null)
                                    Text("Install APK")
                                }
                                OutlinedButton(
                                    onClick = { /* Download APK as ZIP */ },
                                    modifier = Modifier.weight(1f),
                                    enabled = buildOutput.contains("BUILD SUCCESSFUL")
                                ) {
                                    Icon(Icons.Default.Download, null)
                                    Text("Download ZIP")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class BuildVariant {
    DEBUG,
    RELEASE,
}
