package com.opencode.ui.mcps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * MCP Servers screen - manage Model Context Protocol servers
 * Supports:
 * - Remote HTTP servers (Streamable HTTP transport)
 * - Local stdio servers
 * - OAuth authentication
 * - Per-server headers and env vars
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McpServersScreen(
    onBack: () -> Unit,
) {
    var addServerMode by remember { mutableStateOf<AddServerMode>(AddServerMode.REMOTE) }
    var serverUrl by remember { mutableStateOf("") }
    var serverName by remember { mutableStateOf("") }
    var serverCommand by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MCP Servers") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Add server */ }) {
                        Icon(Icons.Default.Add, "Add Server")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("MCP Servers", style = MaterialTheme.typography.titleLarge)
            Text("Connect external tools and services via Model Context Protocol",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            // Add server mode tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = addServerMode == AddServerMode.REMOTE,
                    onClick = { addServerMode = AddServerMode.REMOTE },
                    label = { Text("Remote HTTP") },
                    leadingIcon = { Icon(Icons.Default.Devices, null) },
                )
                FilterChip(
                    selected = addServerMode == AddServerMode.LOCAL,
                    onClick = { addServerMode = AddServerMode.LOCAL },
                    label = { Text("Local Stdio") },
                    leadingIcon = { Icon(Icons.Default.Terminal, null) },
                )
            }

            // Add server form
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = serverName,
                        onValueChange = { serverName = it },
                        label = { Text("Server Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (addServerMode == AddServerMode.REMOTE) {
                        TextField(
                            value = serverUrl,
                            onValueChange = { serverUrl = it },
                            label = { Text("URL") },
                            placeholder = { Text("https://mcp.example.com/mcp") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = "",
                            onValueChange = { },
                            label = { Text("Headers (optional)") },
                            placeholder = { Text("Authorization: Bearer {env:KEY}") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        TextField(
                            value = serverCommand,
                            onValueChange = { serverCommand = it },
                            label = { Text("Command") },
                            placeholder = { Text("npx -y @modelcontextprotocol/server-everything") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = { /* Add server */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Server")
                    }
                }
            }

            // Connected servers list
            Text("Connected Servers", style = MaterialTheme.typography.titleMedium)

            // Placeholder servers
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Devices, null,
                                tint = MaterialTheme.colorScheme.primary)
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Example Server", style = MaterialTheme.typography.titleMedium)
                                Text("Connected", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                            Icon(Icons.Default.Cancel, "Disconnect")
                        }
                    }
                }
            }
        }
    }
}

enum class AddServerMode {
    REMOTE,
    LOCAL,
}
