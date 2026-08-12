package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentProfileEntity
import com.example.data.UniversityEntity
import com.example.ui.MatchFilterState
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgrammesScreen(
    matchFilter: MatchFilterState,
    onFilterChange: (Double, Double, Double, String, Int) -> Unit,
    studentProfile: StudentProfileEntity?,
    universities: List<UniversityEntity>,
    searchQuery: String,
    onToggleFavorite: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFilterExpanded by remember { mutableStateOf(true) }

    // State bindings
    var inputSsc by remember(matchFilter) { mutableStateOf(matchFilter.sscGpa.toString()) }
    var inputHsc by remember(matchFilter) { mutableStateOf(matchFilter.hscGpa.toString()) }
    var inputIelts by remember(matchFilter) { mutableStateOf(matchFilter.ieltsScore.toString()) }
    var selectedCountry by remember(matchFilter) { mutableStateOf(matchFilter.targetCountry) }
    var maxTuition by remember(matchFilter) { mutableStateOf(matchFilter.maxTuitionUsd) }

    // Filtered Universities Logic
    val filteredList = remember(universities, matchFilter, searchQuery) {
        universities.filter { uni ->
            val matchCountry = matchFilter.targetCountry == "All" || uni.country.equals(matchFilter.targetCountry, ignoreCase = true)
            val matchTuition = uni.tuitionFeeUsd <= matchFilter.maxTuitionUsd
            val matchQuery = searchQuery.isEmpty() ||
                    uni.name.contains(searchQuery, ignoreCase = true) ||
                    uni.country.contains(searchQuery, ignoreCase = true) ||
                    uni.popularProgrammes.contains(searchQuery, ignoreCase = true)
            matchCountry && matchTuition && matchQuery
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Hero Header Banner
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "University Eligibility Matcher",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Match your Academic GPA & IELTS scores with official entry requirements worldwide.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        IconButton(
                            onClick = { isFilterExpanded = !isFilterExpanded },
                            modifier = Modifier.testTag("toggle_eligibility_filter_button")
                        ) {
                            Icon(
                                imageVector = if (isFilterExpanded) Icons.Default.ExpandLess else Icons.Default.FilterList,
                                contentDescription = "Toggle Filters",
                                tint = AccentGold
                            )
                        }
                    }

                    // Collapsible Filter Form
                    AnimatedVisibility(visible = isFilterExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            HorizontalDivider(color = RoyalSlate, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Score Inputs Row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = inputSsc,
                                    onValueChange = { inputSsc = it },
                                    label = { Text("SSC GPA", fontSize = 11.sp, color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentGold,
                                        unfocusedBorderColor = RoyalSlate,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("ssc_gpa_input")
                                )

                                OutlinedTextField(
                                    value = inputHsc,
                                    onValueChange = { inputHsc = it },
                                    label = { Text("HSC GPA", fontSize = 11.sp, color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentGold,
                                        unfocusedBorderColor = RoyalSlate,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("hsc_gpa_input")
                                )

                                OutlinedTextField(
                                    value = inputIelts,
                                    onValueChange = { inputIelts = it },
                                    label = { Text("IELTS", fontSize = 11.sp, color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentGold,
                                        unfocusedBorderColor = RoyalSlate,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("ielts_score_input")
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Target Country Filter Chips
                            Text(
                                text = "Target Country:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            val countries = listOf("All", "USA", "UK", "Canada", "Germany", "Australia", "Japan")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(countries) { country ->
                                    val isSel = selectedCountry == country
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { selectedCountry = country },
                                        label = { Text(country, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = AccentGold,
                                            selectedLabelColor = DarkNavy,
                                            containerColor = RoyalSlate,
                                            labelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("country_chip_$country")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Tuition Fee Range
                            Text(
                                text = "Max Annual Tuition Fee: $$maxTuition USD",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Slider(
                                value = maxTuition.toFloat(),
                                onValueChange = { maxTuition = it.toInt() },
                                valueRange = 0f..60000f,
                                steps = 11,
                                colors = SliderDefaults.colors(
                                    thumbColor = AccentGold,
                                    activeTrackColor = AccentSky,
                                    inactiveTrackColor = RoyalSlate
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val ssc = inputSsc.toDoubleOrNull() ?: 5.0
                                    val hsc = inputHsc.toDoubleOrNull() ?: 4.8
                                    val ielts = inputIelts.toDoubleOrNull() ?: 7.0
                                    onFilterChange(ssc, hsc, ielts, selectedCountry, maxTuition)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("apply_eligibility_filter_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DarkNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Calculate Academic Matches (${filteredList.size} Found)",
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavy
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Matching Universities (${filteredList.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Sorted by Rank",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Empty state if no match
        if (filteredList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching universities found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your target country or tuition budget slider.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // University Result Cards List
        items(filteredList, key = { it.id }) { uni ->
            UniversityMatchCard(
                university = uni,
                studentSsc = matchFilter.sscGpa,
                studentHsc = matchFilter.hscGpa,
                studentIelts = matchFilter.ieltsScore,
                onToggleFavorite = { onToggleFavorite(uni.id, !uni.isFavorite) },
                onApplyDirectly = {
                    val url = if (uni.applyUrl.startsWith("http")) uni.applyUrl else "https://${uni.applyUrl}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UniversityMatchCard(
    university: UniversityEntity,
    studentSsc: Double,
    studentHsc: Double,
    studentIelts: Double,
    onToggleFavorite: () -> Unit,
    onApplyDirectly: () -> Unit
) {
    // Determine Requirement Matches
    val sscMet = studentSsc >= university.minSscGpa
    val hscMet = studentHsc >= university.minHscGpa
    val ieltsMet = studentIelts >= university.minIelts

    val metCount = (if (sscMet) 1 else 0) + (if (hscMet) 1 else 0) + (if (ieltsMet) 1 else 0)

    val (statusLabel, statusColor, statusBg) = when (metCount) {
        3 -> Triple("HIGH ELIGIBILITY", AccentEmerald, AccentEmerald.copy(alpha = 0.15f))
        2 -> Triple("CONDITIONAL MATCH", AccentGold, AccentGold.copy(alpha = 0.15f))
        else -> Triple("REJECTION RISK", Color(0xFFEF4444), Color(0xFFEF4444).copy(alpha = 0.15f))
    }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("uni_card_${university.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Uni Name, Ranking & Star Favorite
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = RoyalSlate,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "QS #${university.ranking}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${university.country} • ${university.city}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = university.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.testTag("favorite_star_${university.id}")
                ) {
                    Icon(
                        imageVector = if (university.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (university.isFavorite) AccentGold else TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Eligibility Badge + Requirements Comparison Chips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }

                Text(
                    text = "Tuition: $${university.tuitionFeeUsd}/yr",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentSky
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Academic Cutoffs Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                RequirementBadge(
                    label = "SSC >= ${university.minSscGpa}",
                    isMet = sscMet
                )
                RequirementBadge(
                    label = "HSC >= ${university.minHscGpa}",
                    isMet = hscMet
                )
                RequirementBadge(
                    label = "IELTS >= ${university.minIelts}",
                    isMet = ieltsMet
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = university.description,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Intakes: ${university.intakes}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkNavy
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row: Direct Apply Link
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onApplyDirectly,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("apply_directly_button_${university.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Apply Directly (Official Link)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun RequirementBadge(label: String, isMet: Boolean) {
    Surface(
        color = if (isMet) AccentEmerald.copy(alpha = 0.12f) else Color(0xFFEF4444).copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Icon(
                imageVector = if (isMet) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = if (isMet) AccentEmerald else Color(0xFFEF4444),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isMet) AccentEmerald else Color(0xFFEF4444)
            )
        }
    }
}
