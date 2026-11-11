package com.opencode.ui.components.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TerminalView component - embedded xterm.js-style terminal emulator.
 * Renders command output with ANSI-style colors, supports command input.
 * Used in both inline panels and full-screen mode.
 */
@Composable
fun TerminalView(
    history: List<String> = emptyList(),
    currentInput: String = "",
    onInputChange: (String) -> Unit = {},
    onExecute: () -> Unit = {},
    onClear: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    prompt: String = "opencode@android:~\$ ",
    isRunning: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header bar (only in inline/panel mode)
        if (onBack != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Terminal",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = onClear) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.CleanHands,
                            "Clear terminal",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onBack) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.Close,
                            "Close terminal",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
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
        ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    if (history.isEmpty()) {
                        item {
                            Text(
                                "Welcome to OpenCode Terminal.\nType a command to get started.\n\n",
                                color = Color.White.copy(alpha = 0.5f),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        items(history.size) { index ->
                            val line = history[index]
                            val lineColor = when {
                                line.contains("ERROR", ignoreCase = true) -> Color(0xFFFF5252)
                                line.contains("WARNING", ignoreCase = true) -> Color(0xFFFFC107)
                                line.contains("SUCCESS", ignoreCase = true) || line.contains("BUILD", ignoreCase = true) -> Color(0xFF69F0AE)
                                line.contains("\$") || line.contains("opencode") -> Color(0xFF448AFF)
                                else -> Color.White.copy(alpha = 0.85f)
                            }
                            Text(
                                text = line,
                                color = lineColor,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    // Current input line with cursor
                    item {
                        Text(
                            text = prompt + currentInput + (if (isRunning) "..." else "█"),
                            color = Color(0xFF448AFF),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 11.sp,
                        )
                    }
                }
        }

        // Command input bar
        if (onBack == null) {
            // Full-screen mode: input at bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        prompt,
                        color = Color(0xFF448AFF),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                    TextField(
                        value = currentInput,
                        onValueChange = onInputChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                    IconButton(
                        onClick = onExecute,
                        enabled = currentInput.isNotBlank() && !isRunning
                    ) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.ArrowForward,
                            "Execute command",
                            tint = if (currentInput.isNotBlank() && !isRunning)
                                Color.White
                            else
                                Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
