package com.opencode.ui.model

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opencode.engine.*

/**
 * Model selection screen - configure models for each agent
 * Supports:
 * - Cloud API models (with API key + endpoint)
 * - Local GGUF models (downloaded or imported)
 * - Hybrid mode (switch between cloud and local)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionScreen(
    onSelected: (ModelConfig) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var apiKey by remember { mutableStateOf("") }
    var endpointUrl by remember { mutableStateOf("") }
    var modelName by remember { mutableStateOf("") }
    var ggufPath by remember { mutableStateOf("") }
    var temperature by remember { mutableFloatStateOf(0.7f) }
    var maxTokens by remember { mutableIntRangeStateOf(4096..8192) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure Model") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val config = ModelConfig(
                            agentType = AgentType.OPENCODE,
                            cloudProvider = "anthropic",
                            cloudModelName = modelName.ifEmpty { "claude-sonnet-5-5" },
                            apiKey = apiKey.ifEmpty { null },
                            endpointUrl = endpointUrl.ifEmpty { null },
                            ggufFilePath = if (ggufPath.isNotBlank()) java.io.File(ggufPath) else null,
                            ggufFileName = if (ggufPath.isNotBlank()) "model.gguf" else null,
                            temperature = temperature,
                            maxTokens = maxTokens.start,
                        )
                        onSelected(config)
                    }) {
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
            // Tabs: Cloud API | Local GGUF | Hybrid
            TabRow(selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text("Cloud API", modifier = Modifier.padding(12.dp))
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text("Local GGUF", modifier = Modifier.padding(12.dp))
                }
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                ) {
                    Text("Hybrid", modifier = Modifier.padding(12.dp))
                }
            }

            // Inference settings (all tabs)
            Text("Inference Settings", style = MaterialTheme.typography.titleMedium)

            // Temperature slider
            Text("Temperature: ${temperature.toInt()}/100",
                style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = temperature,
                onValueChange = { temperature = it },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth()
            )

            // Max tokens
            Text("Max Tokens: ${maxTokens.start}",
                style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = maxTokens.start.toFloat(),
                onValueChange = { maxTokens = IntRange(maxTokens.start, maxTokens.endInclusive) },
                valueRange = 1024f..131072f,
                modifier = Modifier.fillMaxWidth()
            )

            // Save button
            Button(
                onClick = {
                    val config = ModelConfig(
                        agentType = AgentType.OPENCODE,
                        cloudProvider = "anthropic",
                        cloudModelName = modelName.ifEmpty { "claude-sonnet-5-5" },
                        apiKey = apiKey.ifEmpty { null },
                        endpointUrl = endpointUrl.ifEmpty { null },
                        temperature = temperature,
                        maxTokens = maxTokens.start,
                    )
                    onSelected(config)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Save Model Config")
            }
        }
    }
}

@Composable
private fun CloudApiConfig(
    apiKey: String, onApiKeyChange: (String) -> Unit,
    endpointUrl: String, onEndpointChange: (String) -> Unit,
    modelName: String, onModelNameChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Cloud API Configuration", style = MaterialTheme.typography.titleSmall)

        TextField(
            value = apiKey,
            onValueChange = onApiKeyChange,
            placeholder = { Text("sk-...") },
            label = { Text("API Key") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = endpointUrl,
            onValueChange = onEndpointChange,
            placeholder = { Text("https://api.anthropic.com/v1") },
            label = { Text("Endpoint URL (optional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = modelName,
            onValueChange = onModelNameChange,
            placeholder = { Text("claude-sonnet-5-5") },
            label = { Text("Model Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GgufConfig(
    ggufPath: String, onGgufPathChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Local GGUF Model", style = MaterialTheme.typography.titleSmall)

        TextField(
            value = ggufPath,
            onValueChange = onGgufPathChange,
            placeholder = { Text("/storage.emulated/0/Models/model.gguf") },
            label = { Text("GGUF File Path") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { /* Open file picker for GGUF */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.UploadFile, null)
            Text("Select GGUF File")
        }
    }
}
