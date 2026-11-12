package com.opencode.ui.agent

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import com.opencode.engine.AgentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentSelectionScreen(
    onBack: () -> Unit = {},
    onSelected: (AgentType) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Coding Agent") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                text = "Select Coding Agent",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Choose your AI coding agent for this session",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Agent cards
            AgentType.entries.forEach { agent ->
                Spacer(Modifier.height(8.dp))
                AgentCard(
                    agent = agent,
                    onClick = {
                        onSelected(agent)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun AgentCard(agent: AgentType, onClick: () -> Unit) {
    val accentColor = Color(agent.accentColor)
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(3.dp, accentColor, androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = agent.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = agent.description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
                // Capability badges
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (agent.supportsGguf) {
                        Badge(text = "GGUF")
                    }
                    if (agent.supportsCloudApi) {
                        Badge(text = "Cloud")
                    }
                    if (agent.supportsToolCalls) {
                        Badge(text = "Tools")
                    }
                    if (agent.supportsVision) {
                        Badge(text = "Vision")
                    }
                }
            }
        }
    }
}

@Composable
private fun Badge(text: String) {
    Text(
        text = text,
        fontSize = 9.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        fontWeight = FontWeight.Bold
    )
}
