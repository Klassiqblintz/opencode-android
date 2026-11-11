package com.opencode.ui.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.compose.ui.platform.LocalContext

/**
 * Preview screen - view built apps and web apps
 * Two modes:
 * 1. Web Preview: WebView showing http://localhost:port (for Flutter web, React, etc.)
 * 2. APK Launch: Launch the compiled APK on the device
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    onBack: () -> Unit,
) {
    var selectedMode by remember { mutableStateOf<PreviewMode>(PreviewMode.WEB) }
    var webUrl by remember { mutableStateOf("http://localhost:8080") }
    var isPreviewing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview") },
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
        ) {
            // Mode selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedMode == PreviewMode.WEB,
                    onClick = { selectedMode = PreviewMode.WEB },
                    label = { Text("Web Preview") },
                    leadingIcon = { Icon(Icons.Default.Web, null) },
                )
                FilterChip(
                    selected = selectedMode == PreviewMode.APK,
                    onClick = { selectedMode = PreviewMode.APK },
                    label = { Text("APK Launch") },
                    leadingIcon = { Icon(Icons.Default.Devices, null) },
                )
            }

            if (selectedMode == PreviewMode.WEB) {
                // Web preview mode
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Web Preview URL", style = MaterialTheme.typography.titleSmall)
                        TextField(
                            value = webUrl,
                            onValueChange = { webUrl = it },
                            placeholder = { Text("http://localhost:8080") },
                            singleLine = true,
                        )
                        Button(
                            onClick = {
                                isPreviewing = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Web, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Load Preview")
                        }
                    }
                }
            } else {
                // APK launch mode
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Installed Apps", style = MaterialTheme.typography.titleSmall)
                        // List of installed APKs from the workspace
                        Text("No apps built yet. Build an APK first.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { /* Launch last built APK */ },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false, // Disabled if no APK built
                        ) {
                            Icon(Icons.Default.Devices, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Launch APK")
                        }
                    }
                }
            }

            // WebView / APK rendering area
            if (isPreviewing && selectedMode == PreviewMode.WEB) {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    // WebView will be rendered here
                    androidx.compose.foundation.AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                loadUrl(webUrl)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

enum class PreviewMode {
    WEB,    // WebView for web apps
    APK,    // Launch compiled APK
}
