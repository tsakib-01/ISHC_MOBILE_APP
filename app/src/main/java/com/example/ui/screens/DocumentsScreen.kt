package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentEntity
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    documents: List<DocumentEntity>,
    onAddDocument: (String, String, String, Double) -> Unit,
    onDeleteDocument: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilterCategory by remember { mutableStateOf("ALL") }

    val categories = listOf("ALL", "TRANSCRIPT", "IELTS_CERT", "SOP", "PASSPORT", "LOR")

    val filteredDocs = remember(documents, selectedFilterCategory) {
        if (selectedFilterCategory == "ALL") documents
        else documents.filter { it.category.equals(selectedFilterCategory, ignoreCase = true) }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.CloudUpload, contentDescription = null, tint = DarkNavy) },
                text = { Text("Upload Document", fontWeight = FontWeight.Bold, color = DarkNavy) },
                containerColor = AccentGold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("upload_document_fab")
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = padding.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkNavy),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Document Vault & Attestations",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Secure cloud repository for transcripts, IELTS certificates, SOPs & passports.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Category Filter Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSel = selectedFilterCategory == cat
                                FilterChip(
                                    selected = isSel,
                                    onClick = { selectedFilterCategory = cat },
                                    label = { Text(cat.replace("_", " "), fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AccentGold,
                                        selectedLabelColor = DarkNavy,
                                        containerColor = RoyalSlate,
                                        labelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("doc_filter_$cat")
                                )
                            }
                        }
                    }
                }
            }

            // Document Cards
            if (filteredDocs.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp).fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No documents found in vault",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Click 'Upload Document' to add transcripts or certificates.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            items(filteredDocs, key = { it.id }) { doc ->
                DocumentCardItem(
                    document = doc,
                    onDelete = { onDeleteDocument(doc.id) }
                )
            }
        }
    }

    // Add Document Modal Dialog
    if (showAddDialog) {
        AddDocumentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, category, fileName, sizeMb ->
                onAddDocument(name, category, fileName, sizeMb)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DocumentCardItem(
    document: DocumentEntity,
    onDelete: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("doc_item_${document.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        when (document.category) {
                            "TRANSCRIPT" -> AccentSky.copy(alpha = 0.15f)
                            "IELTS_CERT" -> AccentGold.copy(alpha = 0.15f)
                            "SOP" -> AccentEmerald.copy(alpha = 0.15f)
                            else -> DarkNavy.copy(alpha = 0.1f)
                        }
                    )
            ) {
                Icon(
                    imageVector = when (document.category) {
                        "TRANSCRIPT" -> Icons.Default.School
                        "IELTS_CERT" -> Icons.Default.Translate
                        "SOP" -> Icons.Default.Description
                        "PASSPORT" -> Icons.Default.Badge
                        else -> Icons.Default.Article
                    },
                    contentDescription = null,
                    tint = when (document.category) {
                        "TRANSCRIPT" -> AccentSky
                        "IELTS_CERT" -> AccentGold
                        "SOP" -> AccentEmerald
                        else -> DarkNavy
                    },
                        modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = document.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${document.fileName} • ${document.fileSizeMb} MB",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (document.status == "VERIFIED") AccentEmerald.copy(alpha = 0.15f) else AccentGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = document.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (document.status == "VERIFIED") AccentEmerald else AccentGold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Added: ${document.dateAdded}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_doc_${document.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Document",
                    tint = Color(0xFFEF4444)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDocumentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("TRANSCRIPT") }
    var fileName by remember { mutableStateOf("transcript_2026.pdf") }

    val categoryOptions = listOf("TRANSCRIPT", "IELTS_CERT", "SOP", "PASSPORT", "LOR")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Upload to Document Vault",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Document Title (e.g. Official HSC Grade Sheet)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_doc_title_input")
                )

                Text("Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    categoryOptions.take(3).forEachIndexed { index, cat ->
                        SegmentedButton(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = 3)
                        ) {
                            Text(cat.replace("_", " "), fontSize = 10.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("File Name (.pdf / .docx)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_doc_filename_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, selectedCategory, fileName, 1.8)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                enabled = name.isNotBlank(),
                modifier = Modifier.testTag("confirm_add_doc_button")
            ) {
                Text("Upload to Vault", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
