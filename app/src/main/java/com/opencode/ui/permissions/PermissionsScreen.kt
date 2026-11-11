package com.opencode.ui.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Permissions screen - configure tool and action permissions
 * Supports:
 * - Shell commands
 * - File editing
 * - Subagent spawning
 * - Skill loading
 * - Web fetch/search
 * - MCP tool calls
 * - Permission rules (allow/ask/deny per action+resource)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    onBack: () -> Unit,
) {
    var shellPermission by remember { mutableStateOf(PermissionLevel.ASK) }
    var editPermission by remember { mutableStateOf(PermissionLevel.ALLOW) }
    var subagentPermission by remember { mutableStateOf(PermissionLevel.ASK) }
    var skillPermission by remember { mutableStateOf(PermissionLevel.ALLOW) }
    var webPermission by remember { mutableStateOf(PermissionLevel.ASK) }
    var mcpPermission by remember { mutableStateOf(PermissionLevel.ASK) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permissions") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Save */ }) {
                        Icon(Icons.Default.Check, "Save")
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
            Text("Tool & Action Permissions", style = MaterialTheme.typography.titleLarge)
            Text("Control which actions the agent can perform and when to ask for approval",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            // Permission items
            PermissionItem(
                icon = Icons.Default.Terminal,
                title = "Shell Commands",
                description = "Run shell commands on the device",
                selected = shellPermission,
                onChange = { shellPermission = it }
            )

            PermissionItem(
                icon = Icons.Default.Edit,
                title = "File Editing",
                description = "Edit, write, and patch files in the workspace",
                selected = editPermission,
                onChange = { editPermission = it }
            )

            PermissionItem(
                icon = Icons.Default.Person,
                title = "Subagent Spawning",
                description = "Launch child sessions for specialized tasks",
                selected = subagentPermission,
                onChange = { subagentPermission = it }
            )

            PermissionItem(
                icon = Icons.Default.SmartButton,
                title = "Skill Loading",
                description = "Load skills from .opencode/skills/ directory",
                selected = skillPermission,
                onChange = { skillPermission = it }
            )

            PermissionItem(
                icon = Icons.Default.Web,
                title = "Web Fetch & Search",
                description = "Fetch URLs and search the web",
                selected = webPermission,
                onChange = { webPermission = it }
            )

            PermissionItem(
                icon = Icons.Default.Devices,
                title = "MCP Tool Calls",
                description = "Use tools from connected MCP servers",
                selected = mcpPermission,
                onChange = { mcpPermission = it }
            )
        }
    }
}

@Composable
private fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    selected: PermissionLevel,
    onChange: (PermissionLevel) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, title, tint = MaterialTheme.colorScheme.primary)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }

            // Permission selector
            MenuAnchor(type = MenuAnchorType.PrimaryBottom) {
                TextButton(
                    onClick = {
                        onChange(
                            when (selected) {
                                PermissionLevel.ALLOW -> PermissionLevel.ASK
                                PermissionLevel.ASK -> PermissionLevel.DENY
                                PermissionLevel.DENY -> PermissionLevel.ALLOW
                            }
                        )
                    }
                ) {
                    when (selected) {
                        PermissionLevel.ALLOW -> {
                            Icon(Icons.Default.CheckCircle, "Allow",
                                tint = MaterialTheme.colorScheme.primary)
                            Text("Allow")
                        }
                        PermissionLevel.ASK -> {
                            Icon(Icons.Default.QuestionAnswer, "Ask",
                                tint = MaterialTheme.colorScheme.secondary)
                            Text("Ask")
                        }
                        PermissionLevel.DENY -> {
                            Icon(Icons.Default.Cancel, "Deny",
                                tint = MaterialTheme.colorScheme.error)
                            Text("Deny")
                        }
                    }
                }
            }
        }
    }
}

enum class PermissionLevel {
    ALLOW, ASK, DENY
}
