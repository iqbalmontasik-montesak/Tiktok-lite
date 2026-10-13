package com.example.ui.create

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun CreateVideoScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordProgress by remember { mutableFloatStateOf(0f) }
    var hasRecorded by remember { mutableStateOf(false) }

    var selectedSpeed by remember { mutableStateOf("1x") }
    val speeds = listOf("0.5x", "1x", "2x", "3x")

    var selectedSound by remember { mutableStateOf("🎵 Suara Asli - Dangdut Viral Lite") }
    val sounds = listOf(
        "🎵 Suara Asli - Dangdut Viral Lite",
        "🎵 LoFi Chill Dapur Nusantara",
        "🎵 Beat DJ Remix Tren Terbaru"
    )

    var captionText by remember { mutableStateOf("") }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordProgress = 0f
            while (recordProgress < 1f && isRecording) {
                delay(100)
                recordProgress += 0.05f
            }
            if (recordProgress >= 1f) {
                isRecording = false
                hasRecorded = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1F1C2C), Color(0xFF928DAB), Color(0xFF141E30))
                )
            )
            .statusBarsPadding()
            .testTag("create_video_screen")
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier.testTag("close_create_screen")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = Color.White
                )
            }

            // Sound Selector
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x66000000))
                    .clickable {
                        val nextIdx = (sounds.indexOf(selectedSound) + 1) % sounds.size
                        selectedSound = sounds[nextIdx]
                    }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("select_sound_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Music",
                        tint = TikTokCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedSound.take(24) + "...",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Flip camera button
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Putar Kamera",
                    tint = Color.White
                )
            }
        }

        // Center / Recording indicator
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!hasRecorded) {
                Text(
                    text = if (isRecording) "Merekam Video Lite..." else "Siap Merekam Video Ringan",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mode Kamera Terkompresi Otomatis (Hemat Memori & Kuota)",
                    color = LiteGreen,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Speed Selectors
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x66000000))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    speeds.forEach { sp ->
                        val isSelected = selectedSpeed == sp
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { selectedSpeed = sp }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = sp,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Post-record Form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xCC111111))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Video Berhasil Direkam! 🎉",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estimasi Ukuran: 0.5 MB (Sangat Cepat Diunggah)",
                        color = LiteGreen,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = captionText,
                        onValueChange = { captionText = it },
                        placeholder = { Text("Tulis caption menarik dan hashtag...", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = TikTokCard,
                            unfocusedContainerColor = TikTokCard
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("caption_input_field")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                hasRecorded = false
                                recordProgress = 0f
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ulangi", color = Color.White)
                        }

                        Button(
                            onClick = {
                                val cap = captionText.ifBlank { "Video seru baru di TikTok Lite! #fyp #viral" }
                                viewModel.uploadVideo(cap, selectedSound, listOf("#tiktoklite", "#fyp", "#viral"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(2f)
                                .testTag("publish_video_button")
                        ) {
                            Text("Posting Video", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Bottom Record Shutter Button
        if (!hasRecorded) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular progress around record button
                if (isRecording) {
                    CircularProgressIndicator(
                        progress = { recordProgress },
                        color = TikTokRed,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(92.dp)
                    )
                }

                // Shutter trigger button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, Color.Black, CircleShape)
                        .clickable {
                            isRecording = !isRecording
                        }
                        .testTag("record_shutter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isRecording) 32.dp else 60.dp)
                            .clip(if (isRecording) RoundedCornerShape(8.dp) else CircleShape)
                            .background(TikTokRed)
                    )
                }
            }
        }
    }
}
