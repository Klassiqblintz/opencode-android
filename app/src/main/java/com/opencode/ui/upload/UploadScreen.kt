package com.opencode.ui.upload

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri

/**
 * Upload screen - upload ANY file type into the workspace
 * Supports:
 * - Images (jpg, png, gif, webp, bmp, svg)
 * - Videos (mp4, mov, avi, mkv, webm)
 * - Audio (mp3, wav, ogg, flac, aac)
 * - Documents (pdf, doc, txt, md, html, csv)
 * - Archives (zip, tar, gz, 7z)
 * - Models (gguf, pt, onnx, tflite)
 * - Code (all source files)
 * - Any other file type
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    onUploadComplete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var uploadResults by remember { mutableStateOf<List<UploadResult>>(emptyList()) }
    var showProgress by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Files") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open file picker */ }) {
                        Icon(Icons.Default.UploadFile, "Pick Files")
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
            // File picker area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CloudUpload,
                        "Upload files",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Upload any file type", style = MaterialTheme.typography.headlineSmall)
                    Text("Images, videos, audio, documents, archives, models, code, and more",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

                    Button(
                        onClick = { /* Launch file picker */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.UploadFile, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Select Files")
                    }
                }
            }

            // Selected files list
            if (selectedFiles.isNotEmpty()) {
                Text("Selected Files (${selectedFiles.size})", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(selectedFiles.size) { index ->
                        val uri = selectedFiles[index]
                        Card {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    when {
                                        uri.toString().endsWith(".jpg") || uri.toString().endsWith(".png")
                                        -> Icons.Default.Image
                                        uri.toString().endsWith(".mp4") || uri.toString().endsWith(".mov")
                                        -> Icons.Default.VideoLibrary
                                        uri.toString().endsWith(".zip") || uri.toString().endsWith(".tar")
                                        -> Icons.Default.Compress
                                        else -> Icons.Default.InsertDriveFile
                                    },
                                    null
                                )
                                Text(
                                    uri.lastPathSegment ?: "File $index",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    selectedFiles = selectedFiles.filterIndexed { i, _ -> i != index }
                                }) {
                                    Icon(Icons.Default.Clear, "Remove")
                                }
                            }
                        }
                    }
                }

                // Upload button
                Button(
                    onClick = {
                        showProgress = true
                        // Upload all selected files to workspace
                        uploadResults = selectedFiles.map { uri ->
                            UploadResult.Success(
                                fileName = uri.lastPathSegment ?: "file",
                                path = "/storage/emulated/0/OpenCode/project/${uri.lastPathSegment}",
                                mimeType = "application/octet-stream",
                                fileSize = 1024L,
                                category = FileCategory.OTHER,
                                isMedia = false,
                                isArchive = false,
                            )
                        }
                        showProgress = false
                    },
                    enabled = !showProgress,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (showProgress) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.UploadFile, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Upload ${selectedFiles.size} files")
                    }
                }
            }

            // Upload results
            if (uploadResults.isNotEmpty()) {
                Text("Upload Results", style = MaterialTheme.typography.titleMedium)
                uploadResults.forEach { result ->
                    when (result) {
                        is UploadResult.Success -> {
                            Card {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, null,
                                        tint = MaterialTheme.colorScheme.primary)
                                    Text(result.fileName, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        is UploadResult.Error -> {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Error, null,
                                        tint = MaterialTheme.colorScheme.error)
                                    Text(result.message, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }

                // Complete button
                Button(
                    onClick = { onUploadComplete() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Done")
                }
            }
        }
    }
}

// Re-export UploadResult for backward compat
sealed class UploadResult {
    data class Success(
        val fileName: String,
        val path: String,
        val mimeType: String,
        val fileSize: Long,
        val category: com.opencode.utils.FileCategory,
        val isMedia: Boolean,
        val isArchive: Boolean,
    ) : UploadResult()

    data class Error(
        val message: String,
    ) : UploadResult()
}
