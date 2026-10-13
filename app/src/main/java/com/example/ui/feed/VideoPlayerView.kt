package com.example.ui.feed

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun VideoPlayerView(
    video: VideoItem,
    isActive: Boolean,
    isMuted: Boolean,
    isLowRamMode: Boolean,
    onDoubleTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var tapHeartPosition by remember { mutableStateOf<Offset?>(null) }
    var showPauseIcon by remember { mutableStateOf(false) }

    // Wave animation phase for ultra-lite visualizer fallback
    var animPhase by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isActive, isPlaying) {
        if (isActive && isPlaying) {
            while (true) {
                delay(16)
                animPhase += 0.05f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(video.primaryColorHex))
            .testTag("video_player_box_${video.id}")
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isPlaying = !isPlaying
                        showPauseIcon = true
                    },
                    onDoubleTap = { offset ->
                        tapHeartPosition = offset
                        onDoubleTap()
                    }
                )
            }
    ) {
        // Native VideoView embedded via AndroidView only when active to avoid lag and save RAM
        if (isActive && !hasError && !isLowRamMode) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setVideoURI(Uri.parse(video.videoUrl))
                        setOnPreparedListener { mp ->
                            isBuffering = false
                            mp.isLooping = true
                            if (isMuted) {
                                mp.setVolume(0f, 0f)
                            } else {
                                mp.setVolume(1f, 1f)
                            }
                            if (isPlaying) {
                                start()
                            }
                        }
                        setOnErrorListener { _, _, _ ->
                            hasError = true
                            isBuffering = false
                            true // Handled gracefully with fallback
                        }
                    }
                },
                update = { videoView ->
                    if (isPlaying) {
                        if (!videoView.isPlaying) videoView.start()
                    } else {
                        if (videoView.isPlaying) videoView.pause()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Ultra-Lite High Efficiency Visualizer (Consumes near 0MB RAM, zero lag, saves 90% quota)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Ambient gradient background
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(video.primaryColorHex),
                            Color(0xFF0A0A0E),
                            Color(0xFF140711)
                        )
                    )
                )

                // Rhythm pulse waves
                val centerY = h * 0.45f
                val barCount = 36
                val barWidth = w / (barCount * 1.5f)
                val spacing = barWidth * 0.5f

                for (i in 0 until barCount) {
                    val factor = sin(animPhase + i * 0.35f).coerceAtLeast(0.1f)
                    val barHeight = 40f + factor * 140f
                    val x = i * (barWidth + spacing) + spacing

                    val color = if (i % 2 == 0) TikTokCyan.copy(alpha = 0.55f) else TikTokRed.copy(alpha = 0.55f)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, centerY - barHeight / 2),
                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )
                }
            }
        }

        // Top and Bottom gradient overlays for high contrast text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0x66000000),
                        0.25f to Color.Transparent,
                        0.65f to Color.Transparent,
                        1.0f to Color(0xCC000000)
                    )
                )
        )

        // Loading indicator if buffering
        if (isActive && isBuffering && !hasError && !isLowRamMode) {
            CircularProgressIndicator(
                color = TikTokCyan,
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Center)
            )
        }

        // Pause/Play state indicator flash
        LaunchedEffect(showPauseIcon) {
            if (showPauseIcon) {
                delay(800)
                showPauseIcon = false
            }
        }

        AnimatedVisibility(
            visible = !isPlaying || showPauseIcon,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(Color(0x77000000), shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Jeda atau Putar",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Heart burst animation on double tap
        tapHeartPosition?.let { pos ->
            LaunchedEffect(pos) {
                delay(650)
                tapHeartPosition = null
            }
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Suka",
                tint = TikTokRed,
                modifier = Modifier
                    .offset { IntOffset(pos.x.toInt() - 48, pos.y.toInt() - 48) }
                    .size(96.dp)
            )
        }
    }

    DisposableEffect(video.id) {
        onDispose {
            isPlaying = false
        }
    }
}
