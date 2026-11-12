package com.opencode.ui.upload

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UploadScreen(
    onBack: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onUploadComplete: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var selectedFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var showProgress by remember { mutableStateOf(false) }
    var uploadResults by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Files") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (selectedFiles.isNotEmpty()) {
                        IconButton(onClick = { /* TODO: open file picker */ }) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = "Pick Files"
                            )
                        }
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
            // Upload instruction card
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Upload any file type",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Code, images, models, archives, and more",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { /* TODO: open file picker */ }) {
                        Text("Select Files")
                    }
                }
            }

            // Selected files list
            if (selectedFiles.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Selected Files (${selectedFiles.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    selectedFiles.forEach { fileName ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.InsertDriveFile,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(fileName, fontSize = 12.sp)
                            }
                            IconButton(onClick = {
                                selectedFiles = selectedFiles - fileName
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            showProgress = true
                            // Simulate upload
                            delay(2000L)
                            uploadResults = listOf("SUCCESS: file1.kt", "SUCCESS: image.png")
                            showProgress = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload ${selectedFiles.size} files")
                }
            }

            // Progress indicator
            if (showProgress) {
                Spacer(Modifier.height(16.dp))
                Card {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("Uploading...")
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Upload results
            if (uploadResults.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Upload Results",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    uploadResults.forEach { result ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(result, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        onUploadComplete()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done")
                }
            }
        }
    }
}
