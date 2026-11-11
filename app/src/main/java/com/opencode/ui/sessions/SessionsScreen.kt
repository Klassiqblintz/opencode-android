package com.opencode.ui.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sessions screen - manage coding sessions
 * Features:
 * - List all sessions
 * - Create new session
 * - Resume/continue sessions
 * - Compact context
 * - Undo/redo
 * - Fork sessions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onBack: () -> Unit,
    onNewSession: () -> Unit,
    onOpenChat: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sessions") },
                actions = {
                    IconButton(onClick = onNewSession) {
                        Icon(Icons.Default.Add, "New Session")
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
            Text("Your Sessions", style = MaterialTheme.typography.titleLarge)
            Text("Each session is a separate conversation with the agent",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            // New session card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNewSession),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                    Column {
                        Text("Start New Session",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Fresh conversation with the agent",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                }
            }

            // Session list
            Text("Recent Sessions", style = MaterialTheme.typography.titleMedium)

            // Placeholder session items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(3) { index ->
                    SessionCard(
                        sessionId = (index + 1).toLong(),
                        title = "Session $index",
                        status = SessionStatus.IDLE,
                        messageCount = 0,
                        onClick = { /* Open chat */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionCard(
    sessionId: Long,
    title: String,
    status: SessionStatus,
    messageCount: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        when (status) {
                            SessionStatus.IDLE -> MaterialTheme.colorScheme.primary
                            SessionStatus.RUNNING -> MaterialTheme.colorScheme.secondary
                            SessionStatus.THINKING -> MaterialTheme.colorScheme.tertiary
                            SessionStatus.WAITING_INPUT -> MaterialTheme.colorScheme.warning
                            SessionStatus.WAITING_PERMISSION -> MaterialTheme.colorScheme.warning
                            SessionStatus.COMPLETED -> MaterialTheme.colorScheme.primary
                            SessionStatus.ERRORED -> MaterialTheme.colorScheme.error
                        },
                        androidx.compose.foundation.shape.CircleShape
                    )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text("$messageCount messages • $status",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }

            // Session actions
            IconButton(onClick = { /* Compact */ }) {
                Icon(Icons.Default.CleanHands, "Compact")
            }
            IconButton(onClick = { /* Fork */ }) {
                Icon(Icons.Default.CallSplit, "Fork")
            }
            IconButton(onClick = { /* Delete */ }) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}

enum class SessionStatus {
    IDLE, RUNNING, THINKING, WAITING_INPUT, WAITING_PERMISSION, COMPLETED, ERRORED
}
