package com.opencode.ui.components.builds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BuildStatus {
    IN_PROGRESS, SUCCESS, FAILURE, CANCELLED
}

@Composable
fun BuildOutput(
    output: String = "",
    isBuilding: Boolean = false,
    buildStatus: BuildStatus = BuildStatus.IN_PROGRESS
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Build Output",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // Status indicator
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isBuilding || buildStatus == BuildStatus.IN_PROGRESS) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Building...")
                } else {
                    when (buildStatus) {
                        BuildStatus.SUCCESS -> {
                            Text("BUILD SUCCESSFUL", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                        BuildStatus.FAILURE -> {
                            Text("BUILD FAILED", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                        }
                        BuildStatus.CANCELLED -> {
                            Text("CANCELLED", color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Output area
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .background(Color(0xFF1E1E1E))
                    .verticalScroll(rememberScrollState())
            ) {
                if (output.isEmpty()) {
                    Text(
                        text = "No build output yet. Tap \"Build APK\" to start.",
                        color = Color.White.copy(0.5f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Text(
                        text = output,
                        color = Color.White.copy(0.85f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Install/Download buttons if successful
            if (buildStatus == BuildStatus.SUCCESS && output.contains("BUILD SUCCESSFUL")) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {}) {
                        Text("Install APK")
                    }
                    OutlinedButton(onClick = {}) {
                        Text("Download ZIP")
                    }
                }
            }
        }
    }
}
