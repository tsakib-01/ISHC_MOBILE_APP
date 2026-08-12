package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentProfileEntity
import com.example.data.UserEntity
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: UserEntity?,
    studentProfile: StudentProfileEntity?,
    onSaveProfile: (Double, Double, Double, Double, Double, Double, Double, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sscGpa by remember(studentProfile) { mutableStateOf((studentProfile?.sscGpa ?: 5.0).toString()) }
    var hscGpa by remember(studentProfile) { mutableStateOf((studentProfile?.hscGpa ?: 4.8).toString()) }
    var ieltsOverall by remember(studentProfile) { mutableStateOf((studentProfile?.ieltsOverall ?: 7.0).toString()) }
    var ieltsReading by remember(studentProfile) { mutableStateOf((studentProfile?.ieltsReading ?: 7.5).toString()) }
    var ieltsWriting by remember(studentProfile) { mutableStateOf((studentProfile?.ieltsWriting ?: 6.5).toString()) }
    var ieltsListening by remember(studentProfile) { mutableStateOf((studentProfile?.ieltsListening ?: 7.5).toString()) }
    var ieltsSpeaking by remember(studentProfile) { mutableStateOf((studentProfile?.ieltsSpeaking ?: 7.0).toString()) }
    var preferredCountries by remember(studentProfile) { mutableStateOf(studentProfile?.preferredCountries ?: "USA, UK, Canada, Germany") }

    var saveSuccessMessage by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // User Banner Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(AccentGold)
                    ) {
                        Text(
                            text = "AR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = DarkNavy
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = user?.name ?: "Alex Rivera",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = user?.email ?: "alex.rivera@student.ishc.org",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = AccentSky,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "STUDENT PORTAL PROFILE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Academic Scores Section
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Academic GPA Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = sscGpa,
                            onValueChange = { sscGpa = it },
                            label = { Text("SSC / O-Level GPA") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_ssc_input")
                        )
                        OutlinedTextField(
                            value = hscGpa,
                            onValueChange = { hscGpa = it },
                            label = { Text("HSC / A-Level GPA") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_hsc_input")
                        )
                    }
                }
            }
        }

        // IELTS Scores Breakdown Section
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "IELTS Language Test Score Breakdown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = ieltsOverall,
                        onValueChange = { ieltsOverall = it },
                        label = { Text("IELTS Overall Band Score") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_ielts_overall_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = ieltsReading,
                            onValueChange = { ieltsReading = it },
                            label = { Text("Reading") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = ieltsWriting,
                            onValueChange = { ieltsWriting = it },
                            label = { Text("Writing") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = ieltsListening,
                            onValueChange = { ieltsListening = it },
                            label = { Text("Listening") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = ieltsSpeaking,
                            onValueChange = { ieltsSpeaking = it },
                            label = { Text("Speaking") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Target Destinations Section
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferred Destination Countries",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = preferredCountries,
                        onValueChange = { preferredCountries = it },
                        label = { Text("Countries (e.g. USA, UK, Canada, Germany)") },
                        modifier = Modifier.fillMaxWidth().testTag("profile_countries_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val ssc = sscGpa.toDoubleOrNull() ?: 5.0
                            val hsc = hscGpa.toDoubleOrNull() ?: 4.8
                            val overall = ieltsOverall.toDoubleOrNull() ?: 7.0
                            val r = ieltsReading.toDoubleOrNull() ?: 7.5
                            val w = ieltsWriting.toDoubleOrNull() ?: 6.5
                            val l = ieltsListening.toDoubleOrNull() ?: 7.5
                            val s = ieltsSpeaking.toDoubleOrNull() ?: 7.0

                            onSaveProfile(ssc, hsc, overall, r, w, l, s, preferredCountries, studentProfile?.journeyStage ?: 2)
                            saveSuccessMessage = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Student Profile Changes",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (saveSuccessMessage) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Profile updated successfully!",
                            color = AccentEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
