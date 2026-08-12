package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.ApplicationEntity
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(
    applications: List<ApplicationEntity>,
    onAddApplication: (String, String, String, String, String, String) -> Unit,
    onUpdateStatus: (ApplicationEntity, String) -> Unit,
    onDeleteApplication: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedStatusFilter by remember { mutableStateOf("ALL") }

    val statusOptions = listOf("ALL", "DRAFT", "SUBMITTED", "UNDER_REVIEW", "ACCEPTED", "OFFER_ISSUED")

    val filteredApps = remember(applications, selectedStatusFilter) {
        if (selectedStatusFilter == "ALL") applications
        else applications.filter { it.status.equals(selectedStatusFilter, ignoreCase = true) }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null, tint = DarkNavy) },
                text = { Text("Log New Application", fontWeight = FontWeight.Bold, color = DarkNavy) },
                containerColor = AccentGold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("log_application_fab")
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
                                    text = "My Applications Tracker",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Track admission status, deadlines & offer letters across universities.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Status Pipeline Filter Chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(statusOptions) { status ->
                                val isSel = selectedStatusFilter == status
                                FilterChip(
                                    selected = isSel,
                                    onClick = { selectedStatusFilter = status },
                                    label = { Text(status.replace("_", " "), fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AccentGold,
                                        selectedLabelColor = DarkNavy,
                                        containerColor = RoyalSlate,
                                        labelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("app_filter_$status")
                                )
                            }
                        }
                    }
                }
            }

            // Application Items
            if (filteredApps.isEmpty()) {
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
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No applications logged in this status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Click 'Log New Application' to start tracking university admissions.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            items(filteredApps, key = { it.id }) { app ->
                ApplicationCardItem(
                    application = app,
                    onUpdateStatus = { newStatus -> onUpdateStatus(app, newStatus) },
                    onDelete = { onDeleteApplication(app.id) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddApplicationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { uni, prog, country, intake, deadline, notes ->
                onAddApplication(uni, prog, country, intake, deadline, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ApplicationCardItem(
    application: ApplicationEntity,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    val (statusColor, statusBg) = when (application.status) {
        "ACCEPTED" -> Pair(AccentEmerald, AccentEmerald.copy(alpha = 0.15f))
        "OFFER_ISSUED" -> Pair(AccentEmerald, AccentEmerald.copy(alpha = 0.15f))
        "UNDER_REVIEW" -> Pair(AccentGold, AccentGold.copy(alpha = 0.15f))
        "SUBMITTED" -> Pair(AccentSky, AccentSky.copy(alpha = 0.15f))
        else -> Pair(TextSecondary, TextSecondary.copy(alpha = 0.15f))
    }

    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("app_card_${application.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${application.country} • ${application.intake}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = application.universityName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = application.programmeName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentSky
                    )
                }

                Box {
                    Surface(
                        color = statusBg,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { expandedMenu = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = application.status.replace("_", " "),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Change Status",
                                tint = statusColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false }
                    ) {
                        listOf("DRAFT", "SUBMITTED", "UNDER_REVIEW", "ACCEPTED", "OFFER_ISSUED").forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.replace("_", " "), fontSize = 12.sp) },
                                onClick = {
                                    onUpdateStatus(st)
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Applied: ${application.appliedDate}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Deadline: ${application.deadline}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            if (application.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notes: ${application.notes}",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Application",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddApplicationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var uniName by remember { mutableStateOf("") }
    var progName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("USA") }
    var intake by remember { mutableStateOf("Fall 2026") }
    var deadline by remember { mutableStateOf("2026-11-01") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Log University Application", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uniName,
                    onValueChange = { uniName = it },
                    label = { Text("University Name (e.g. University of Toronto)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_app_uni_input")
                )
                OutlinedTextField(
                    value = progName,
                    onValueChange = { progName = it },
                    label = { Text("Programme / Degree Major") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_app_prog_input")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = intake,
                        onValueChange = { intake = it },
                        label = { Text("Intake") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Application Deadline") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Application Notes / Portal Reference ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (uniName.isNotBlank() && progName.isNotBlank()) {
                        onConfirm(uniName, progName, country, intake, deadline, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                enabled = uniName.isNotBlank() && progName.isNotBlank()
            ) {
                Text("Log Application", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
