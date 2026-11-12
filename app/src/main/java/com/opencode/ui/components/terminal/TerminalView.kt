package com.opencode.ui.components.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerminalView(
    history: List<String>,
    currentInput: String,
    onInputChange: (String) -> Unit,
    onExecute: (String) -> Unit,
    onClear: () -> Unit = {},
    prompt: String = "opencode@android:~$ ",
    isRunning: Boolean = false,
    onBack: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        // Header bar (only if there's a back button)
        if (onBack != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White.copy(0.7f)
                    )
                }
                Text(
                    text = "Terminal",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color.White.copy(0.7f)
                        )
                    }
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(0.7f)
                        )
                    }
                }
            }
        }

        // Terminal output area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            if (history.isEmpty()) {
                Text(
                    text = "Welcome to OpenCode Terminal",
                    color = Color.White.copy(0.5f),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(history, key = { it.hashCode() }) { line ->
                        TerminalLine(line)
                    }
                }
            }
        }

        // Input area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = prompt,
                    color = Color(0xFF448AFF),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(4.dp))
                BasicTextField(
                    value = currentInput,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    ),
                    enabled = !isRunning
                )
                Text(
                    text = "▎",
                    color = Color.White.copy(0.5f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = { if (currentInput.isNotBlank() && !isRunning) onExecute(currentInput) },
                    enabled = currentInput.isNotBlank() && !isRunning
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Send,
                        contentDescription = "Execute",
                        tint = if (currentInput.isNotBlank() && !isRunning) Color(0xFF448AFF) else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun TerminalLine(line: String) {
    val (color, text) = parseTerminalLine(line)
    Text(
        text = text,
        color = color,
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        modifier = Modifier.padding(vertical = 1.dp)
    )
}

private fun parseTerminalLine(line: String): Pair<Color, String> {
    return when {
        line.contains("ERROR", ignoreCase = true) -> Color(0xFFFF5555) to line
        line.contains("WARNING", ignoreCase = true) -> Color(0xFFF1FA8C) to line
        line.contains("SUCCESS", ignoreCase = true) || line.contains("BUILD", ignoreCase = true) ->
            Color(0xFF50FA7B) to line
        line.contains("$", ignoreCase = true) || line.contains("opencode", ignoreCase = true) ->
            Color(0xFF448AFF) to line
        else -> Color.White.copy(0.85f) to line
    }
}
