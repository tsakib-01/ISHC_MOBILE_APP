package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.BookingEntity
import com.example.data.ExpertEntity
import com.example.ui.theme.*

@Composable
fun MeetingsScreen(
    bookings: List<BookingEntity>,
    experts: List<ExpertEntity>,
    onStartVideoCall: (String, String) -> Unit,
    onBookConsultation: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBookDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showBookDialog = true },
                icon = { Icon(Icons.Default.VideoCall, contentDescription = null, tint = DarkNavy) },
                text = { Text("Book 1-on-1 Meeting", fontWeight = FontWeight.Bold, color = DarkNavy) },
                containerColor = AccentGold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("book_meeting_fab")
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
                                    text = "Scheduled Video Consultations",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Daily.co WebRTC Video Call room for official student visa & admissions advice.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.VideoCameraFront,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = RoyalSlate,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = AccentEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Daily.co WebRTC End-to-End Encrypted HD Video Link Active",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Bookings List
            if (bookings.isEmpty()) {
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
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No video meetings scheduled",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Book a 1-on-1 session with a certified study abroad advisor.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            items(bookings, key = { it.id }) { booking ->
                BookingCardItem(
                    booking = booking,
                    onJoinCall = {
                        onStartVideoCall(booking.meetingUrl, booking.expertName)
                    }
                )
            }
        }
    }

    if (showBookDialog) {
        BookMeetingDialog(
            experts = experts,
            onDismiss = { showBookDialog = false },
            onConfirm = { name, title, scheduledAt, topic ->
                onBookConsultation(name, title, scheduledAt, topic)
                showBookDialog = false
            }
        )
    }
}

@Composable
fun BookingCardItem(
    booking: BookingEntity,
    onJoinCall: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_card_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(DarkNavy)
                    ) {
                        Text(
                            text = booking.expertName.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                            fontWeight = FontWeight.Bold,
                            color = AccentGold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = booking.expertName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = booking.expertTitle,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    color = if (booking.status == "CONFIRMED") AccentEmerald.copy(alpha = 0.15f) else AccentGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = booking.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (booking.status == "CONFIRMED") AccentEmerald else AccentGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = AccentSky,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Topic: ${booking.topic}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = booking.scheduledAt,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = AccentSky,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = booking.meetingUrl,
                            fontSize = 11.sp,
                            color = AccentSky
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onJoinCall,
                colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("join_video_call_button_${booking.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.VideoCall,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Join WebRTC Video Call Room",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMeetingDialog(
    experts: List<ExpertEntity>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var selectedExpert by remember { mutableStateOf(experts.firstOrNull()?.name ?: "Dr. Elena Rostova") }
    var selectedTitle by remember { mutableStateOf(experts.firstOrNull()?.title ?: "US Admissions Specialist") }
    var topic by remember { mutableStateOf("University Selection & SOP Review") }
    var scheduledAt by remember { mutableStateOf("2026-08-20 14:00 EST") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule 1-on-1 Consultation", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Expert Advisor:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                experts.forEach { exp ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedExpert = exp.name
                                selectedTitle = exp.title
                            }
                    ) {
                        RadioButton(
                            selected = selectedExpert == exp.name,
                            onClick = {
                                selectedExpert = exp.name
                                selectedTitle = exp.title
                            }
                        )
                        Column {
                            Text(exp.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(exp.countrySpecialization, fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Consultation Topic / Questions") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = scheduledAt,
                    onValueChange = { scheduledAt = it },
                    label = { Text("Preferred Date & Time") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedExpert, selectedTitle, scheduledAt, topic) },
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy)
            ) {
                Text("Confirm Meeting Booking", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
