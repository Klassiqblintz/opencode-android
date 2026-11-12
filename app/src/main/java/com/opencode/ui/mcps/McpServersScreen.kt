package com.opencode.ui.mcps

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

@Composable
fun McpServersScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var serverName by remember { mutableStateOf("") }
    var serverUrl by remember { mutableStateOf("") }
    var serverCommand by remember { mutableStateOf("") }
    var serverType by remember { mutableStateOf<ServerType>(ServerType.REMOTE_HTTP) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MCP Servers") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Server"
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "MCP Servers",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Manage MCP (Model Context Protocol) servers",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Server type selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = serverType == ServerType.REMOTE_HTTP,
                    onClick = { serverType = ServerType.REMOTE_HTTP },
                    label = { Text("Remote HTTP") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = "Remote"
                        )
                    }
                )
                FilterChip(
                    selected = serverType == ServerType.LOCAL_STDIO,
                    onClick = { serverType = ServerType.LOCAL_STDIO },
                    label = { Text("Local Stdio") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Local"
                        )
                    }
                )
            }

            Spacer(Modifier.height(12.dp))

            // Add server form
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = serverName,
                        onValueChange = { serverName = it },
                        label = { Text("Server Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    if (serverType == ServerType.REMOTE_HTTP) {
                        OutlinedTextField(
                            value = serverUrl,
                            onValueChange = { serverUrl = it },
                            label = { Text("URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = serverCommand,
                            onValueChange = { serverCommand = it },
                            label = { Text("Command") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Headers (JSON, optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Env Vars (JSON, optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { /* TODO: add server */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Server")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Connected servers header
            Text(
                text = "Connected Servers",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Server list (empty for now)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "No servers configured",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

enum class ServerType { REMOTE_HTTP, LOCAL_STDIO }
