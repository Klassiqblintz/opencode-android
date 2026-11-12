package com.opencode.ui.terminal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.ui.components.terminal.TerminalView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var currentInput by remember { mutableStateOf("") }
    var history by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminal") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { history = emptyList() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        TerminalView(
            history = history,
            currentInput = currentInput,
            onInputChange = { currentInput = it },
            onExecute = { command ->
                history = history + listOf(
                    "opencode@android:~$ $command",
                    "Executing: $command...",
                    "Done.",
                    ""
                )
                currentInput = ""
            },
            onClear = { history = emptyList() },
            onBack = onBack,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
