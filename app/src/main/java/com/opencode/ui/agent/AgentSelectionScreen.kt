package com.opencode.ui.agent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opencode.engine.AgentType

/**
 * Agent selection screen - choose which coding agent to use
 * Available agents:
 * - OpenCode (blue accent)
 * - DeepSeek (purple accent)
 * - Claude Code (deep purple accent)
 * - Pi Agent (teal accent)
 * - Custom (blue-gray accent)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentSelectionScreen(
    onSelected: (AgentType) -> Unit,
    onDismiss: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Coding Agent") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Choose your coding agent",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Each agent has its own strengths. You can switch between them at any time.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            // Agent cards
            AgentType.entries.forEach { agentType ->
                AgentCard(
                    agentType = agentType,
                    onClick = { onSelected(agentType) }
                )
            }
        }
    }
}

/**
 * Individual agent card with accent color
 */
@Composable
private fun AgentCard(
    agentType: AgentType,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Accent color indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(agentType.accentColor))
            ) {
                Text(
                    text = agentType.displayName.first().toString().uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Agent info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = agentType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = agentType.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // Tags for capabilities
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (agentType.supportsLocal) {
                        AssistChip(
                            onClick = { },
                            label = { Text("Local") },
                            colors = AssistChipDefaults.asstChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                    if (agentType.supportsRemote) {
                        AssistChip(
                            onClick = { },
                            label = { Text("Remote") },
                            colors = AssistChipDefaults.asstChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                    if (agentType.supportsGguf) {
                        AssistChip(
                            onClick = { },
                            label = { Text("GGUF") },
                            colors = AssistChipDefaults.asstChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    }
                    if (agentType.supportsCloudApi) {
                        AssistChip(
                            onClick = { },
                            label = { Text("API Key") },
                            colors = AssistChipDefaults.asstChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}
