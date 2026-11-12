package com.opencode.ui.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

enum class PermissionState { ALLOW, ASK, DENY }

@Composable
fun PermissionsScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var permissions by remember {
        mutableStateOf(
            mapOf(
                "shell" to PermissionState.ASK,
                "file_editing" to PermissionState.ASK,
                "subagent" to PermissionState.ASK,
                "skill" to PermissionState.ASK,
                "web_fetch" to PermissionState.ASK,
                "mcp" to PermissionState.ASK
            )
        )
    }

    val permissionItems = listOf(
        PermissionItem(
            key = "shell",
            title = "Shell Commands",
            description = "Terminal, file editing, and more",
            icon = Icons.Default.Terminal
        ),
        PermissionItem(
            key = "file_editing",
            title = "File Editing",
            description = "Edit, create, and delete files",
            icon = Icons.Default.Edit
        ),
        PermissionItem(
            key = "subagent",
            title = "Subagent Spawning",
            description = "Launch subagents for parallel work",
            icon = Icons.Default.Person
        ),
        PermissionItem(
            key = "skill",
            title = "Skill Loading",
            description = "Discover and use skills",
            icon = Icons.Default.SmartButton
        ),
        PermissionItem(
            key = "web_fetch",
            title = "Web Fetch",
            description = "Fetch web content and APIs",
            icon = Icons.Default.Web
        ),
        PermissionItem(
            key = "mcp",
            title = "MCP Tool Calls",
            description = "Use MCP server tools",
            icon = Icons.Default.Devices
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permissions") },
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
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
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
                text = "Tool & Action Permissions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Configure how each tool type is handled",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            permissionItems.forEach { item ->
                Spacer(Modifier.height(8.dp))
                PermissionItemCard(
                    item = item,
                    currentState = permissions[item.key] ?: PermissionState.ASK,
                    onStateChange = { newState ->
                        permissions = permissions.toMutableMap().apply {
                            this[item.key] = newState
                        }
                    }
                )
            }
        }
    }
}

data class PermissionItem(
    val key: String,
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun PermissionItemCard(
    item: PermissionItem,
    currentState: PermissionState,
    onStateChange: (PermissionState) -> Unit
) {
    val (iconColor, iconVector) = when (currentState) {
        PermissionState.ALLOW -> MaterialTheme.colorScheme.primary to Icons.Default.CheckCircle
        PermissionState.ASK -> MaterialTheme.colorScheme.primary.copy(0.7f) to Icons.Default.Help
        PermissionState.DENY -> MaterialTheme.colorScheme.error to Icons.Default.Cancel
    }

    val stateLabel = when (currentState) {
        PermissionState.ALLOW -> "ALLOW"
        PermissionState.ASK -> "ASK"
        PermissionState.DENY -> "DENY"
    }

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(item.title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text(item.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                }
            }
            TextButton(
                onClick = {
                    val next = when (currentState) {
                        PermissionState.ALLOW -> PermissionState.ASK
                        PermissionState.ASK -> PermissionState.DENY
                        PermissionState.DENY -> PermissionState.ALLOW
                    }
                    onStateChange(next)
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(stateLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = iconColor)
                }
            }
        }
    }
}
