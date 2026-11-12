package com.opencode.ui.components.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient

@Composable
fun WebPreview(
    url: String = "http://localhost:8080",
    onUrlChange: (String) -> Unit = {},
    onReload: () -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    var loading by remember { mutableStateOf(false) }
    var currentUrl by remember { mutableStateOf(url) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Header bar
        if (onBack != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Web Preview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { onReload(); loading = true }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Refresh,
                            contentDescription = "Reload",
                            tint = Color(0xFF1E88E5)
                        )
                    }
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // URL input card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = currentUrl,
                    onValueChange = { currentUrl = it; onUrlChange(it) },
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { loading = true },
                    enabled = currentUrl.isNotBlank()
                ) {
                    Text("Load Preview")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (loading) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.width(8.dp))
                Text("Loading preview...")
            }
        } else {
            // WebView area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            webViewClient = WebViewClient()
                            loadUrl(currentUrl)
                            setOnTouchListener { _, _ -> true }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // APK Launch section
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "APK Launch",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                ) {
                    Text("Launch APK")
                }
            }
        }
    }
}
