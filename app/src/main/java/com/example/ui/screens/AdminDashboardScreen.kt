package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.DocumentEntity
import com.example.data.UniversityEntity
import com.example.domain.model.AdminStatsDomainModel
import com.example.domain.model.ApplicationStatus
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    stats: AdminStatsDomainModel,
    universities: List<UniversityEntity>,
    applications: List<ApplicationEntity>,
    documents: List<DocumentEntity>,
    onAddUniversity: (String, String, String, Double, Double, Double, Int) -> Unit,
    onUpdateApplicationStatus: (ApplicationEntity, String) -> Unit,
    onVerifyDocument: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Universities, 2: Applications, 3: Documents

    var showAddUniDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
            .padding(16.dp)
    ) {
        // Admin Header Title Card
        Surface(
            color = RoyalSlate,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Portal",
                            tint = AccentGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ISHC Admin Management Portal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Global System Control, Verification & Compliance",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentGold)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ADMIN ACTIVE",
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp,
                        color = DarkNavy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = RoyalSlate,
            contentColor = AccentGold,
            edgePadding = 0.dp,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.testTag("admin_tab_overview")
            ) {
                Text(
                    text = "System Metrics",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.testTag("admin_tab_universities")
            ) {
                Text(
                    text = "Universities (${universities.size})",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                modifier = Modifier.testTag("admin_tab_applications")
            ) {
                Text(
                    text = "Applications (${applications.size})",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                modifier = Modifier.testTag("admin_tab_documents")
            ) {
                Text(
                    text = "Documents (${documents.size})",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> AdminMetricsOverview(stats)
            1 -> AdminUniversitiesTab(
                universities = universities,
                onOpenAddDialog = { showAddUniDialog = true }
            )
            2 -> AdminApplicationsTab(
                applications = applications,
                onUpdateStatus = onUpdateApplicationStatus
            )
            3 -> AdminDocumentsTab(
                documents = documents,
                onVerifyDocument = onVerifyDocument
            )
        }
    }

    if (showAddUniDialog) {
        AddUniversityDialog(
            onDismiss = { showAddUniDialog = false },
            onConfirm = { name, country, city, ssc, hsc, ielts, tuition ->
                onAddUniversity(name, country, city, ssc, hsc, ielts, tuition)
                showAddUniDialog = false
            }
        )
    }
}

@Composable
fun AdminMetricsOverview(stats: AdminStatsDomainModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetricCard(
                    title = "Total Registered Students",
                    value = "${stats.totalStudents}",
                    icon = Icons.Default.School,
                    accentColor = AccentSky,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Active Applications",
                    value = "${stats.totalApplications}",
                    icon = Icons.Default.Send,
                    accentColor = AccentGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetricCard(
                    title = "Verified Documents",
                    value = "${stats.totalVerifiedDocs}",
                    icon = Icons.Default.Verified,
                    accentColor = AccentEmerald,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Booked Consultations",
                    value = "${stats.activeConsultations}",
                    icon = Icons.Default.VideoCall,
                    accentColor = Color(0xFFA855F7),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = RoyalSlate),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color.White)
            Text(text = title, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun AdminUniversitiesTab(
    universities: List<UniversityEntity>,
    onOpenAddDialog: () -> Unit
) {
    Column {
        Button(
            onClick = onOpenAddDialog,
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_add_university_button")
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = DarkNavy)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Partner University", color = DarkNavy, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(universities, key = { it.id }) { uni ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = RoyalSlate),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = uni.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text(text = "${uni.city}, ${uni.country} | Rank #${uni.ranking}", color = AccentSky, fontSize = 12.sp)
                            Text(text = "Min IELTS: ${uni.minIelts} | Min HSC: ${uni.minHscGpa} | $${uni.tuitionFeeUsd}/yr", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminApplicationsTab(
    applications: List<ApplicationEntity>,
    onUpdateStatus: (ApplicationEntity, String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(applications, key = { it.id }) { app ->
            Card(
                colors = CardDefaults.cardColors(containerColor = RoyalSlate),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = app.universityName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentGold)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = app.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkNavy)
                        }
                    }
                    Text(text = app.programmeName, color = AccentSky, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onUpdateStatus(app, "ACCEPTED") },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Accept Offer", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onUpdateStatus(app, "UNDER_REVIEW") },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentSky),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Mark Review", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDocumentsTab(
    documents: List<DocumentEntity>,
    onVerifyDocument: (Int, String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(documents, key = { it.id }) { doc ->
            Card(
                colors = CardDefaults.cardColors(containerColor = RoyalSlate),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = doc.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        Text(text = "${doc.fileName} (${doc.fileSizeMb} MB)", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onVerifyDocument(doc.id, "VERIFIED") },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Verify", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AddUniversityDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Double, Double, Double, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var minSsc by remember { mutableStateOf("4.0") }
    var minHsc by remember { mutableStateOf("4.0") }
    var minIelts by remember { mutableStateOf("6.5") }
    var tuition by remember { mutableStateOf("25000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Partner University", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("University Name") })
                OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") })
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
                OutlinedTextField(value = minHsc, onValueChange = { minHsc = it }, label = { Text("Min HSC GPA") })
                OutlinedTextField(value = minIelts, onValueChange = { minIelts = it }, label = { Text("Min IELTS Score") })
                OutlinedTextField(value = tuition, onValueChange = { tuition = it }, label = { Text("Tuition USD / yr") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        name,
                        country,
                        city,
                        minSsc.toDoubleOrNull() ?: 4.0,
                        minHsc.toDoubleOrNull() ?: 4.0,
                        minIelts.toDoubleOrNull() ?: 6.5,
                        tuition.toIntOrNull() ?: 25000
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
            ) {
                Text("Save", color = DarkNavy, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.White) }
        },
        containerColor = RoyalSlate
    )
}
