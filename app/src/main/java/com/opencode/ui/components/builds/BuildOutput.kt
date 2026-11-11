package com.opencode.ui.components.builds

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

/**
 * BuildOutput component - displays build output from Gradle, Flutter, CMake, etc.
 * Supports syntax highlighting hints, scrollable output, and status indicators.
 */
@Composable
fun BuildOutput(
    output: String,
    isBuilding: Boolean = false,
    buildStatus: BuildStatus = BuildStatus.IN_PROGRESS,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Status header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Build Output",
                    style = MaterialTheme.typography.titleSmall
                )
                when (buildStatus) {
                    BuildStatus.IN_PROGRESS -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 1.5.dp
                            )
                            Text(
                                "Building...",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    BuildStatus.SUCCESS -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "BUILD SUCCESSFUL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    BuildStatus.FAILURE -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "BUILD FAILED",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    BuildStatus.CANCELLED -> {
                        Text(
                            "CANCELLED",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Scrollable output area
            if (output.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Text(
                            text = output,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = androidx.compose.ui.unit.sp(10),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Build output will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }

            // Action buttons for successful builds
            if (buildStatus == BuildStatus.SUCCESS && output.contains("BUILD SUCCESSFUL")) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* Install APK */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Install APK")
                    }
                    OutlinedButton(
                        onClick = { /* Download ZIP */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Download ZIP")
                    }
                }
            }
        }
    }
}

/**
 * Build status enum for displaying build progress.
 */
enum class BuildStatus {
    IN_PROGRESS,
    SUCCESS,
    FAILURE,
    CANCELLED,
}
