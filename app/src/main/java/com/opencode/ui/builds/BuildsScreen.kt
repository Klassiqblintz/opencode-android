package com.opencode.ui.builds

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.ui.components.builds.BuildOutput
import com.opencode.ui.components.builds.BuildStatus
import com.opencode.utils.ProjectType

@Composable
fun BuildsScreen(
    onBack: (() -> Unit)? = null,
    onOpenTerminal: () -> Unit = {},
    onOpenPreview: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf(ProjectType.KOTLIN_COMPOSE) }
    var buildVariant by remember { mutableStateOf("RELEASE") }
    var isBuilding by remember { mutableStateOf(false) }
    var buildOutput by remember { mutableStateOf("") }
    var buildStatus by remember { mutableStateOf(BuildStatus.IN_PROGRESS) }

    Scaffold(
        topBar = {
            if (onBack != null) {
                TopAppBar(
                    title = { Text("Builds") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenTerminal) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = "Terminal"
                            )
                        }
                        IconButton(onClick = onOpenPreview) {
                            Icon(
                                imageVector = Icons.Default.Web,
                                contentDescription = "Preview"
                            )
                        }
                        IconButton(onClick = onOpenSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Build Configuration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Project type filter
            Text(
                text = "Project Type",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProjectType.entries.filter { it != ProjectType.MIXED }.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.displayName) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Build variant
            Text(
                text = "Build Variant",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = buildVariant == "DEBUG",
                    onClick = { buildVariant = "DEBUG" },
                    label = { Text("Debug APK") }
                )
                FilterChip(
                    selected = buildVariant == "RELEASE",
                    onClick = { buildVariant = "RELEASE" },
                    label = { Text("Release APK") }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Build button
            Button(
                onClick = {
                    isBuilding = true
                    buildStatus = BuildStatus.IN_PROGRESS
                    // Simulate build
                    kotlinx.coroutines.delay(3000) {
                        buildOutput = "BUILD SUCCESSFUL in 3s\nAPK generated at: app/build/outputs/apk/release/app-release.apk"
                        buildStatus = BuildStatus.SUCCESS
                        isBuilding = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isBuilding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Building...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Build APK")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Build output
            if (isBuilding || buildOutput.isNotEmpty()) {
                BuildOutput(
                    output = buildOutput,
                    isBuilding = isBuilding,
                    buildStatus = buildStatus
                )
            }
        }
    }
}
