package com.opencode.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opencode.ui.chat.MessageEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatMessages(
    messages: List<MessageEntity>,
    onPermissionGrant: (Long, Boolean) -> Unit = { _, _ -> },
    onToolExecute: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            when (message.role) {
                "USER" -> UserMessageBubble(message)
                "AGENT" -> AgentMessageBubble(message)
                "TOOL_CALL" -> ToolCallCard(message, onToolExecute)
                "TOOL_RESULT" -> ToolResultCard(message)
                "PERMISSION" -> PermissionCard(message, onPermissionGrant)
                "ERROR" -> ErrorCard(message, onToolExecute)
                else -> DefaultMessageBubble(message)
            }
        }
    }
}

@Composable
private fun UserMessageBubble(message: MessageEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
            shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "You",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = message.content,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = formatTimestamp(message.createdAt),
                    color = Color.White.copy(0.6f),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun AgentMessageBubble(message: MessageEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Agent",
                    color = Color(0xFF1E88E5),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = message.content,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = formatTimestamp(message.createdAt),
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ToolCallCard(message: MessageEntity, onExecute: (Long) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "🔧 ${message.content.take(60)}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF7E57C2)
            )
            if (expanded) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message.content,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Collapse" else "Expand")
                }
                TextButton(onClick = { onExecute(message.id) }) {
                    Text("Run")
                }
            }
        }
    }
}

@Composable
private fun ToolResultCard(message: MessageEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "✅ Tool Result",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = message.content,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun PermissionCard(message: MessageEntity, onGrant: (Long, Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "⚠️ Permission Request",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFFF9A825)
            )
            Text(
                text = message.content,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onGrant(message.id, true) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Approve")
                }
                OutlinedButton(onClick = { onGrant(message.id, false) }) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(message: MessageEntity, onRetry: (Long) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "❌ Error",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFFD32F2F)
            )
            Text(
                text = message.content,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            TextButton(onClick = { onRetry(message.id) }) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun DefaultMessageBubble(message: MessageEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = message.content,
            fontSize = 13.sp,
            modifier = Modifier.padding(12.dp)
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
