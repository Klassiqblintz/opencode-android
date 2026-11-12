package com.opencode.ui.model

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.opencode.engine.AgentType
import com.opencode.engine.ModelSource
import com.opencode.engine.DefaultModelConfigs
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionScreen(
    onBack: () -> Unit = {},
    onSelected: (com.opencode.engine.ModelConfig) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var apiKey by remember { mutableStateOf("") }
    var endpointUrl by remember { mutableStateOf("") }
    var modelName by remember { mutableStateOf("") }
    var ggufPath by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf(0.7f) }
    var maxTokens by remember { mutableStateOf(4096) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure Model") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val config = DefaultModelConfigs.defaultOpenCodeConfig().copy(
                            apiKey = apiKey,
                            endpointUrl = endpointUrl,
                            cloudModelName = modelName,
                            temperature = temperature,
                            maxTokens = maxTokens
                        )
                        onSelected(config)
                        onDismiss()
                    }) {
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // TabRow
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Cloud API") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Local GGUF") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Hybrid") }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    // Cloud API tab
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API Key") },
                        placeholder = { Text("sk-...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = endpointUrl,
                        onValueChange = { endpointUrl = it },
                        label = { Text("Endpoint URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = modelName,
                        onValueChange = { modelName = it },
                        label = { Text("Model Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                1 -> {
                    // Local GGUF tab
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ggufPath,
                            onValueChange = { ggufPath = it },
                            label = { Text("GGUF File Path") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(onClick = { /* TODO: file picker */ }) {
                            Text("Select")
                        }
                    }
                }
                2 -> {
                    // Hybrid: show both
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API Key (Cloud)") },
                        placeholder = { Text("sk-...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ggufPath,
                            onValueChange = { ggufPath = it },
                            label = { Text("GGUF Path") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(onClick = { /* TODO */ }) {
                            Text("Select")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Shared settings
            Text(
                text = "Temperature: ${"%.2f".format(temperature)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = temperature,
                onValueChange = { temperature = it },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Max Tokens: $maxTokens",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = maxTokens.toFloat(),
                onValueChange = { maxTokens = it.toInt().coerceIn(1024, 131072) },
                valueRange = 1024f..131072f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
