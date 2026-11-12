package com.opencode.ui.skills

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skills") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
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
                text = "Skills",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Skills are discoverable capabilities defined in SKILL.md files",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // About skills card
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "About Skills",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Skills provide structured capabilities to the AI agent. Each skill is defined in a SKILL.md file that describes what the skill does and how to use it.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Skill Discovery Paths:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• Project-level: from SKILL.md in the project root",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "• Workspace-level: from SKILL.md in the workspace",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "• System-level: from SKILL.md in the system directory",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "• Built-in: pre-packaged with the app",
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Available skills header
            Text(
                text = "Available Skills",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Skills list (empty for now)
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartButton,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "No skills installed",
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
