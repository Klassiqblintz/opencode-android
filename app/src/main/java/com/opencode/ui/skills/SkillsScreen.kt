package com.opencode.ui.skills

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Skills screen - manage agent skills
 * Skills are loaded from:
 * - .opencode/skills/ (project level)
 * - ~/.config/opencode/skills/ (global)
 * - .claude/skills/ (Claude compatible)
 * - .agents/skills/ (Agent compatible)
 *
 * Each skill is a SKILL.md file with:
 * - Frontmatter: name, description
 * - Body: workflow instructions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skills") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Create skill */ }) {
                        Icon(Icons.Default.Add, "Create Skill")
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
            Text("Agent Skills", style = MaterialTheme.typography.titleLarge)
            Text("Reusable workflows for the coding agent. Each skill is a SKILL.md file with instructions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            // Skill discovery paths
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Skill Discovery Paths", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("• .opencode/skills/ (project)",
                        style = MaterialTheme.typography.bodySmall)
                    Text("• ~/.config/opencode/skills/ (global)",
                        style = MaterialTheme.typography.bodySmall)
                    Text("• .claude/skills/ (Claude compatible)",
                        style = MaterialTheme.typography.bodySmall)
                    Text("• .agents/skills/ (Agent compatible)",
                        style = MaterialTheme.typography.bodySmall)
                }
            }

            // Available skills list
            Text("Available Skills", style = MaterialTheme.typography.titleMedium)

            // Placeholder skills
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.SmartButton, null,
                                tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text("Code Review", style = MaterialTheme.typography.titleMedium)
                                Text("Review changes for correctness and regressions",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        }
    }
}
