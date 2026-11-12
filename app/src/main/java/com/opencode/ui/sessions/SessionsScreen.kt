package com.opencode.ui.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onBack: (() -> Unit)? = null,
    onNewSession: () -> Unit = {},
    onDeleteSession: (Long) -> Unit = {},
    onForkSession: (Long) -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (onBack != null) {
                TopAppBar(
                    title = { Text("Sessions") },
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Sessions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Manage your AI coding sessions",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Start New Session card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = onNewSession
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Session",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Start New Session",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Begin a new AI coding session",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Recent sessions header
            Text(
                text = "Recent Sessions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Session list (empty for now, will be populated from DB)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(emptyList<String>()) { session ->
                    SessionCard(
                        title = session,
                        status = "IDLE",
                        messageCount = 0,
                        onFork = { onForkSession(0) },
                        onDelete = { onDeleteSession(0) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionCard(
    title: String,
    status: String,
    messageCount: Int,
    onFork: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (status.uppercase()) {
        "IDLE" -> Color(0xFF1E88E5)
        "RUNNING" -> Color(0xFF00BFA5)
        "THINKING" -> Color(0xFF7C4DFF)
        "WAITING_INPUT" -> Color(0xFFF9A825)
        "COMPLETED" -> Color(0xFF1E88E5)
        "ERRORED" -> Color(0xFFD32F2F)
        "PAUSED" -> Color(0xFF707D8B)
        "WAITING_PERMISSION" -> Color(0xFFF9A825)
        else -> Color(0xFF1E88E5)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(statusColor, androidx.compose.foundation.shape.CircleShape)
                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onFork) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Fork",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Text(
                text = "$messageCount messages · $status",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
            )
        }
    }
}
