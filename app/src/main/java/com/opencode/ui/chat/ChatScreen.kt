package com.opencode.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencode.ui.components.chat.ChatMessages
import com.opencode.ui.components.terminal.TerminalView
import com.opencode.ui.components.preview.WebPreview

/**
 * Chat screen - main conversation interface with the coding agent
 * Features:
 * - Streaming chat messages
 * - Tool call display
 * - File diff display
 * - Permission request handling
 * - Embedded terminal button
 * - Preview button for web/app
 * - Code Mode toggle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    sessionId: Long? = null,
    onBack: (() -> Unit)? = null,
    onOpenTerminal: () -> Unit,
    onOpenPreview: () -> Unit,
    onNewSession: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    var inputText by remember { mutableStateOf("") }
    var isStreaming by remember { mutableStateOf(false) }
    var showTerminalPanel by remember { mutableStateOf(false) }
    var showPreviewPanel by remember { mutableStateOf(false) }
    var terminalHistory by remember { mutableStateOf(emptyList<String>()) }
    var terminalInput by remember { mutableStateOf("") }
    var previewUrl by remember { mutableStateOf("http://localhost:8080") }
    var chatMessages by remember { mutableStateOf(emptyList<com.opencode.storage.entities.MessageEntity>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OpenCode Chat") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                },
                actions = {
                    // Terminal button
                    IconButton(onClick = onOpenTerminal) {
                        Icon(Icons.Default.Terminal, "Terminal")
                    }

                    // Preview button
                    IconButton(onClick = onOpenPreview) {
                        Icon(Icons.Default.Web, "Preview")
                    }

                    // New session
                    IconButton(onClick = onNewSession) {
                        Icon(Icons.Default.Add, "New Session")
                    }

                    // Settings
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        },
        bottomBar = {
            // Chat input bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask the agent to code...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 4,
                    )
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                // Send message to agent
                                isStreaming = true
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank() && !isStreaming,
                    ) {
                        Icon(Icons.Default.Send, "Send")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showTerminalPanel || showPreviewPanel) {
                // Split view: chat + panel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    // Chat messages area (60%)
                    Box(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    ) {
                        ChatMessages(
                            messages = chatMessages,
                            onPermissionGrant = { msgId, granted ->
                                // Handle permission grant
                            }
                        )
                    }

                    // Side panel (40%) - either Terminal or Preview
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                    ) {
                        if (showTerminalPanel) {
                            TerminalView(
                                history = terminalHistory,
                                currentInput = terminalInput,
                                onInputChange = { terminalInput = it },
                                onExecute = {
                                    if (terminalInput.isNotBlank()) {
                                        terminalHistory = terminalHistory + terminalInput
                                        terminalInput = ""
                                    }
                                },
                                onClear = { terminalHistory = emptyList() },
                                onBack = { showTerminalPanel = false },
                            )
                        } else if (showPreviewPanel) {
                            WebPreview(
                                url = previewUrl,
                                onUrlChange = { previewUrl = it },
                                onReload = { /* Reload WebView */ },
                                onBack = { showPreviewPanel = false },
                            )
                        }
                    }
                }
            } else {
                // Full chat view
                ChatMessages(
                    messages = chatMessages,
                    onPermissionGrant = { msgId, granted ->
                        // Handle permission grant
                    }
                )
            }
        }
    }
}
