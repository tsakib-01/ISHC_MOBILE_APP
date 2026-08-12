package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.StudentProfileEntity
import com.example.ui.theme.*

data class JourneyStageInfo(
    val stageNumber: Int,
    val title: String,
    val description: String,
    val subTasks: List<String>
)

val journeyStagesList = listOf(
    JourneyStageInfo(
        stageNumber = 1,
        title = "Academic & Language Evaluation",
        description = "Calculate SSC/HSC GPAs, complete IELTS Academic exam, and evaluate test scores.",
        subTasks = listOf(
            "Obtain official SSC academic transcripts & certificate",
            "Obtain official HSC academic transcripts & certificate",
            "Take IELTS Academic exam (Target 6.5 - 7.5)",
            "Upload TRF test report form to ISHC Vault"
        )
    ),
    JourneyStageInfo(
        stageNumber = 2,
        title = "University Shortlisting & Eligibility",
        description = "Filter global universities based on entry requirements, tuition budget, and intake dates.",
        subTasks = listOf(
            "Run ISHC University Matcher for target countries",
            "Save 5-8 shortlisted universities to Favorites",
            "Verify application deadlines for target intake (Fall/Winter)",
            "Review country work rights & post-study visa policies"
        )
    ),
    JourneyStageInfo(
        stageNumber = 3,
        title = "Document Vault & SOP Attestation",
        description = "Draft Statement of Purpose, obtain Letters of Recommendation, and attest documents.",
        subTasks = listOf(
            "Write Statement of Purpose (SOP) tailored for target major",
            "Request 2 Academic Letters of Recommendation (LORs)",
            "Verify passport validity (minimum 2 years remaining)",
            "Attest academic certificates via Education Board / Ministry"
        )
    ),
    JourneyStageInfo(
        stageNumber = 4,
        title = "Direct Application Submissions",
        description = "Submit official university portal applications and track application status.",
        subTasks = listOf(
            "Submit online application to primary university choices",
            "Pay international university application fees",
            "Submit Uni-assist VPD / credential evaluations if required",
            "Log submitted applications in ISHC Tracker"
        )
    ),
    JourneyStageInfo(
        stageNumber = 5,
        title = "Offer Letter & Financial Proof",
        description = "Receive admission offer letters, pay deposit, and set up Blocked Account / Solvency proof.",
        subTasks = listOf(
            "Receive Conditional / Unconditional Offer Letter",
            "Book 1-on-1 Consultation with ISHC Expert Advisor",
            "Prepare Bank Solvency Certificate / German Blocked Account",
            "Pay tuition deposit & receive official CAS / I-20 / COE"
        )
    ),
    JourneyStageInfo(
        stageNumber = 6,
        title = "Visa Processing & Flight Departure",
        description = "Schedule embassy visa interview, secure medical clearance, and prepare pre-departure checklist.",
        subTasks = listOf(
            "Complete online visa application (DS-160 / UK Student / Subclass 500)",
            "Book visa appointment at Embassy / VFS Global center",
            "Attend embassy visa interview with ISHC mock prep",
            "Book flight tickets & arrange university accommodation"
        )
    )
)

@Composable
fun JourneyScreen(
    studentProfile: StudentProfileEntity?,
    onUpdateStage: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStageNum = studentProfile?.journeyStage ?: 2
    val overallProgressFraction = (currentStageNum.toFloat() / journeyStagesList.size.toFloat())

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Overall Roadmap Header
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
                                text = "Study Abroad Journey Progress",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                            Text(
                                text = "Stage $currentStageNum of ${journeyStagesList.size}: ${journeyStagesList[currentStageNum - 1].title}",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Surface(
                            color = AccentGold,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${(overallProgressFraction * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DarkNavy,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { overallProgressFraction },
                        color = AccentGold,
                        trackColor = RoyalSlate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "6-Stage Roadmap Milestones",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Stages List
        itemsIndexed(journeyStagesList) { idx, stage ->
            val stageNum = idx + 1
            val isCompleted = stageNum < currentStageNum
            val isCurrent = stageNum == currentStageNum
            val isLocked = stageNum > currentStageNum

            JourneyStageCard(
                stage = stage,
                isCompleted = isCompleted,
                isCurrent = isCurrent,
                isLocked = isLocked,
                onSetCurrentStage = { onUpdateStage(stageNum) }
            )
        }
    }
}

@Composable
fun JourneyStageCard(
    stage: JourneyStageInfo,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean,
    onSetCurrentStage: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(isCurrent) }

    val cardBg = when {
        isCurrent -> MaterialTheme.colorScheme.surface
        isCompleted -> AccentEmerald.copy(alpha = 0.05f)
        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    }

    val borderColor = when {
        isCurrent -> AccentGold
        isCompleted -> AccentEmerald
        else -> Color.Transparent
    }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardBg),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isCurrent) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrent || isCompleted) 1.5.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("journey_stage_card_${stage.stageNumber}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Status Badge Icon
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> AccentEmerald
                                    isCurrent -> AccentGold
                                    else -> RoyalSlate
                                }
                            )
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "${stage.stageNumber}",
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) DarkNavy else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stage.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = when {
                                isCompleted -> "Completed"
                                isCurrent -> "In Progress (Current Active Stage)"
                                else -> "Upcoming Stage"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                isCompleted -> AccentEmerald
                                isCurrent -> AccentGold
                                else -> TextSecondary
                            }
                        )
                    }
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = TextSecondary
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = stage.description,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Stage Action Items:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    stage.subTasks.forEach { subtask ->
                        var isChecked by remember { mutableStateOf(isCompleted) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentEmerald,
                                    uncheckedColor = TextSecondary
                                )
                            )
                            Text(
                                text = subtask,
                                fontSize = 12.sp,
                                color = if (isChecked) TextSecondary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!isCurrent) {
                        OutlinedButton(
                            onClick = onSetCurrentStage,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("set_current_stage_button_${stage.stageNumber}")
                        ) {
                            Text(
                                text = "Set as My Current Active Stage",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
