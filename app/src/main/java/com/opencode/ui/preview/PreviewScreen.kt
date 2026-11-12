package com.opencode.ui.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.ui.components.preview.WebPreview

enum class PreviewMode { WEB, APK }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf(PreviewMode.WEB) }
    var previewUrl by remember { mutableStateOf("http://localhost:8080") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview") },
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Mode selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedMode == PreviewMode.WEB,
                    onClick = { selectedMode = PreviewMode.WEB },
                    label = { Text("Web Preview") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Web,
                            contentDescription = "Web"
                        )
                    }
                )
                FilterChip(
                    selected = selectedMode == PreviewMode.APK,
                    onClick = { selectedMode = PreviewMode.APK },
                    label = { Text("APK Launch") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "Devices"
                        )
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedMode) {
                PreviewMode.WEB -> {
                    Card {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Web Preview",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            WebPreview(
                                url = previewUrl,
                                onUrlChange = { previewUrl = it },
                                onBack = onBack
                            )
                        }
                    }
                }
                PreviewMode.APK -> {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "APK Launch",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "No apps built yet",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                            )
                            Spacer(Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {},
                                enabled = false
                            ) {
                                Text("Launch APK")
                            }
                        }
                    }
                }
            }
        }
    }
}
