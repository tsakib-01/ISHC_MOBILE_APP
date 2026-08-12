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
import com.example.data.ExpertEntity
import com.example.data.SeminarEntity
import com.example.ui.theme.*

@Composable
fun SeminarsScreen(
    seminars: List<SeminarEntity>,
    onToggleRegistration: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
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
                                text = "Live International Seminars",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                            Text(
                                text = "Free webinars on visa interview prep, university scholarships & IELTS.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        items(seminars, key = { it.id }) { sem ->
            SeminarCardItem(
                seminar = sem,
                onToggleRegistration = { onToggleRegistration(sem.id, !sem.isRegistered) }
            )
        }
    }
}

@Composable
fun SeminarCardItem(
    seminar: SeminarEntity,
    onToggleRegistration: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("seminar_card_${seminar.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = RoyalSlate,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${seminar.date} • ${seminar.time}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = if (seminar.isRegistered) AccentEmerald.copy(alpha = 0.15f) else AccentSky.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (seminar.isRegistered) "REGISTERED" else "OPEN FOR REGISTRATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (seminar.isRegistered) AccentEmerald else AccentSky,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = seminar.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Keynote Speaker: ${seminar.speaker}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentSky
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = seminar.topic,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onToggleRegistration,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (seminar.isRegistered) AccentEmerald else DarkNavy
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_seminar_button_${seminar.id}")
            ) {
                Icon(
                    imageVector = if (seminar.isRegistered) Icons.Default.CheckCircle else Icons.Default.EventAvailable,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (seminar.isRegistered) "Registered (Reminder Set)" else "Register for Free Seminar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ExpertsScreen(
    experts: List<ExpertEntity>,
    onBookConsultation: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
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
                                text = "Verified Expert Advisors",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                            Text(
                                text = "Certified study abroad counselors & former university admissions officers.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.SupervisorAccount,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        items(experts, key = { it.id }) { expert ->
            ExpertCardItem(
                expert = expert,
                onBook = { onBookConsultation(expert.name, expert.title) }
            )
        }
    }
}

@Composable
fun ExpertCardItem(
    expert: ExpertEntity,
    onBook: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("expert_card_${expert.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(DarkNavy)
                    ) {
                        Text(
                            text = expert.name.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                            fontWeight = FontWeight.Bold,
                            color = AccentGold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = expert.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = expert.title,
                            fontSize = 12.sp,
                            color = AccentSky,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    color = AccentGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${expert.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = DarkNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = RoyalSlate,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Specialty: ${expert.countrySpecialization}",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${expert.totalConsultations}+ Sessions",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = expert.bio,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("book_expert_button_${expert.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.VideoCall,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Book 1-on-1 Consultation",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
