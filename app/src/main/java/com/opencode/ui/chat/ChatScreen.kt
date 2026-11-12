package com.opencode.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.ui.chat.MessageEntity
import com.opencode.ui.components.chat.ChatMessages

@Composable
fun ChatScreen(
    onBack: (() -> Unit)? = null,
    onNewSession: () -> Unit = {}
) {
    var currentInput by remember { mutableStateOf("") }

    // Empty messages list for now (will be loaded from DB)
    var messages by remember { mutableStateOf<List<MessageEntity>>(emptyList()) }

    Scaffold(
        topBar = {
            if (onBack != null) {
                TopAppBar(
                    title = { Text("Chat") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onNewSession) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Session"
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
        ) {
            // New Session button
            if (messages.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = onNewSession
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("New Session")
                    }
                }
            }

            // Chat messages
            ChatMessages(
                messages = messages,
                onPermissionGrant = { id, granted -> /* handle permission */ },
                onToolExecute = { id -> /* handle tool */ }
            )

            // Input bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = { currentInput = it },
                        placeholder = { Text("Type your message...") },
                        modifier = Modifier.weight(1f),
                        maxLines = 3
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (currentInput.isNotBlank()) {
                                messages = messages + MessageEntity(
                                    sessionId = 0,
                                    role = "USER",
                                    content = currentInput,
                                    createdAt = System.currentTimeMillis()
                                )
                                currentInput = ""
                                // Trigger streaming response
                            }
                        },
                        enabled = currentInput.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (currentInput.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
