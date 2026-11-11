package com.opencode.ui.components.preview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * WebPreview component - embedded WebView for previewing web apps and built websites.
 * Supports:
 * - Loading URLs from localhost (development servers)
 * - Loading remote URLs
 * - Reloading the page
 * - Navigation history
 * - Loading state indication
 */
@Composable
fun WebPreview(
    url: String = "http://localhost:8080",
    onUrlChange: (String) -> Unit = {},
    onReload: () -> Unit = {},
    onBack: (() -> Unit)? = null,
) {
    var isLoading by remember { mutableStateOf(false) }
    var currentUrl by remember { mutableStateOf(url) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header bar
        if (onBack != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Preview",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = onReload) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.Refresh,
                            "Reload",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onBack) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.Close,
                            "Close preview",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // URL bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = currentUrl,
                    onValueChange = { currentUrl = it; onUrlChange(it) },
                    placeholder = { Text("Enter URL to preview") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
                IconButton(
                    onClick = {
                        if (currentUrl.isNotBlank()) {
                            isLoading = true
                            onReload()
                        }
                    },
                    enabled = currentUrl.isNotBlank() && !isLoading
                ) {
                    Icon(
                        androidx.compose.material.icons.Icons.Default.Visibility,
                        "Load URL",
                        tint = if (currentUrl.isNotBlank() && !isLoading)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.Center)
                        .size(16.dp),
                    strokeWidth = 1.5.dp
                )
            }
        }

        // WebView area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            if (isLoading) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Loading preview...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                // AndroidView for actual WebView rendering
                // This will be set up in the parent composable with android.webkit.WebView
                Text(
                    "WebView will render here\nURL: $currentUrl",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // APK launch section (below WebView area)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    "APK Launch",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Launch compiled APKs directly on the device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { /* Launch last built APK */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Launch APK")
                }
            }
        }
    }
}
