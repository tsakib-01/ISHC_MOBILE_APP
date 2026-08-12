package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.ui.theme.*
import kotlinx.coroutines.delay

data class ChatMessage(val sender: String, val message: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCallScreen(
    roomUrl: String?,
    expertName: String?,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isScreenSharing by remember { mutableStateOf(false) }
    var showChatPanel by remember { mutableStateOf(false) }

    var callDurationSeconds by remember { mutableIntStateOf(145) }

    // Timer simulation
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callDurationSeconds++
        }
    }

    val formatTime = remember(callDurationSeconds) {
        val mins = callDurationSeconds / 60
        val secs = callDurationSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    var chatInput by remember { mutableStateOf("") }
    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage("System", "WebRTC Peer Connection Established via Daily.co cluster", "14:00"),
            ChatMessage(expertName ?: "Dr. Elena Rostova", "Hello Alex! I am looking over your HSC transcripts and IELTS TRF right now.", "14:01"),
            ChatMessage("Alex Rivera (You)", "Hi Dr. Elena! Thank you for reviewing. Do I meet the cutoff for University of Toronto?", "14:02"),
            ChatMessage(expertName ?: "Dr. Elena Rostova", "Yes! Your 7.0 IELTS and 4.8 GPA satisfy UofT's competitive threshold.", "14:02")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        // Main Expert Video Feed Canvas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            // Simulated Expert Stream Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RoyalSlate)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(DarkNavy)
                            .border(3.dp, AccentGold, CircleShape)
                    ) {
                        Text(
                            text = (expertName ?: "Dr. Elena Rostova").split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = expertName ?: "Dr. Elena Rostova",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Text(
                        text = "WebRTC HD Video Stream • Live 1080p 60fps",
                        fontSize = 12.sp,
                        color = AccentEmerald
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Simulated Audio Wave Signal
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(8) { idx ->
                            val height = listOf(12, 28, 40, 20, 36, 16, 32, 22)[idx % 8]
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(height.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(AccentGold)
                            )
                        }
                    }
                }

                // Top Video Info Bar
                Surface(
                    color = DarkNavy.copy(alpha = 0.85f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(AccentEmerald)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily.co • $formatTime",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            text = roomUrl ?: "https://ishc.daily.co/live-consultation",
                            color = AccentSky,
                            fontSize = 11.sp
                        )
                    }
                }

                // Self Camera Preview Overlay Box (Bottom Right)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(width = 110.dp, height = 150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCameraOff) Color.Black else DarkNavy)
                        .border(1.5.dp, AccentSky, RoundedCornerShape(12.dp))
                        .testTag("self_video_preview_box")
                ) {
                    if (isCameraOff) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideocamOff,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Camera Off", fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "You (Alex)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (isMuted) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MicOff,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

        // Live Chat Drawer Panel
        AnimatedVisibility(
            visible = showChatPanel,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(300.dp)
                .statusBarsPadding()
                .padding(bottom = 90.dp)
        ) {
            Surface(
                color = DarkNavy,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "In-Call Live Chat",
                            fontWeight = FontWeight.Bold,
                            color = AccentGold,
                            fontSize = 14.sp
                        )
                        IconButton(onClick = { showChatPanel = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Chat",
                                tint = Color.White
                            )
                        }
                    }

                    HorizontalDivider(color = RoyalSlate)

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(RoyalSlate)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "${msg.sender} • ${msg.time}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentSky
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.message,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Ask question...", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("in_call_chat_input")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    val userText = chatInput.trim()
                                    val timeStr = String.format("%02d:%02d", (callDurationSeconds / 60) % 60, callDurationSeconds % 60)
                                    chatMessages.add(ChatMessage("Alex Rivera (You)", userText, timeStr))
                                    chatInput = ""
                                    
                                    // Simulated Expert Real-time Reply
                                    scope.launch {
                                        delay(1200)
                                        val expName = expertName ?: "Dr. Elena Rostova"
                                        val replyText = when {
                                            userText.contains("visa", ignoreCase = true) -> 
                                                "For your visa application, make sure your bank statement proves 12 months of tuition + living expenses."
                                            userText.contains("ielts", ignoreCase = true) -> 
                                                "Your 7.0 IELTS is great! No band is below 6.5, which satisfies direct admission."
                                            userText.contains("gpa", ignoreCase = true) -> 
                                                "Your 4.8 GPA easily meets the prerequisite threshold for top-tier university entry."
                                            else -> 
                                                "Thank you for asking! I've noted that for our document review summary."
                                        }
                                        val replyTime = String.format("%02d:%02d", (callDurationSeconds / 60) % 60, callDurationSeconds % 60)
                                        chatMessages.add(ChatMessage(expName, replyText, replyTime))
                                    }
                                }
                            },
                            modifier = Modifier.testTag("send_in_call_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = AccentGold
                            )
                        }
                    }
                }
            }
        }

        // Bottom WebRTC Control Action Bar
        Surface(
            color = DarkNavy,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            ) {
                // Mute Mic
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isMuted) Color.Red else RoyalSlate)
                        .testTag("mute_mic_button")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                // Stop Video
                IconButton(
                    onClick = { isCameraOff = !isCameraOff },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isCameraOff) Color.Red else RoyalSlate)
                        .testTag("stop_video_button")
                ) {
                    Icon(
                        imageVector = if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Camera",
                        tint = Color.White
                    )
                }

                // Screen Share
                IconButton(
                    onClick = { isScreenSharing = !isScreenSharing },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (isScreenSharing) AccentSky else RoyalSlate)
                        .testTag("share_screen_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenShare,
                        contentDescription = "Screen Share",
                        tint = Color.White
                    )
                }

                // In-Call Chat
                IconButton(
                    onClick = { showChatPanel = !showChatPanel },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (showChatPanel) AccentGold else RoyalSlate)
                        .testTag("open_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Chat",
                        tint = if (showChatPanel) DarkNavy else Color.White
                    )
                }

                // End Call Button
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .testTag("end_video_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
