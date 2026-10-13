package com.example.ui.feed

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.VideoItem
import com.example.ui.theme.LiteGold
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed

@Composable
fun VideoOverlay(
    video: VideoItem,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleFollow: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenShare: () -> Unit,
    onOpenDataSaver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Disc rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Left Column: Creator details, caption, sound, data badge
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Data Saver info chip (interactive)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x99000000))
                    .border(1.dp, LiteGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable { onOpenDataSaver() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("data_saver_chip")
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Hemat Kuota",
                    tint = LiteGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Lite: ${video.resolutionTag} • ~${video.estimatedDataMb}MB",
                    color = LiteGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Creator name and username
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Lite Verified Badge
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(TikTokCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verified",
                        tint = Color.Black,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Caption
            Text(
                text = video.caption,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sound title banner with sound icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x55000000))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Sound",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = video.soundTitle,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Right Side: Creator avatar + Floating Side Action Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Mute / Unmute Button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0x66000000), CircleShape)
                    .clickable { onToggleMute() }
                    .testTag("mute_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = if (isMuted) "Nyalakan Suara" else "Matikan Suara",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Creator Avatar with Follow button
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                AsyncImage(
                    model = video.creatorAvatar,
                    contentDescription = "Avatar ${video.creatorName}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                )

                // Follow toggle badge
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(if (video.isFollowing) TikTokCyan else TikTokRed, CircleShape)
                        .clickable { onToggleFollow() }
                        .testTag("follow_button_${video.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (video.isFollowing) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Ikuti",
                        tint = if (video.isFollowing) Color.Black else Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // ========================================================
            // FLOATING SIDE ACTION BAR ('Like', 'Comment', 'Share')
            // ========================================================
            FloatingSideActionBar(
                video = video,
                onToggleLike = onToggleLike,
                onOpenComments = onOpenComments,
                onOpenShare = onOpenShare
            )

            // Spinning Vinyl Disc
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    .rotate(rotation),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = video.creatorAvatar,
                    contentDescription = "Vinyl Disc",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

/**
 * Floating side action bar enclosing 'Like', 'Comment', and 'Share' buttons
 * with glassmorphic backdrop, micro-interaction scale bounce, and clear visual labels.
 */
@Composable
fun FloatingSideActionBar(
    video: VideoItem,
    onToggleLike: () -> Unit,
    onOpenComments: () -> Unit,
    onOpenShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0x8818181C))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(28.dp))
            .padding(horizontal = 6.dp, vertical = 12.dp)
            .testTag("floating_side_action_bar")
    ) {
        // 1. LIKE BUTTON
        FloatingActionButtonItem(
            label = "Like",
            countText = formatCount(video.likesCount),
            icon = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            iconTint = if (video.isLiked) TikTokRed else Color.White,
            isActive = video.isLiked,
            testTag = "like_button_${video.id}",
            onClick = onToggleLike
        )

        // Subtle divider inside floating pill
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.15f))
        )

        // 2. COMMENT BUTTON
        FloatingActionButtonItem(
            label = "Comment",
            countText = formatCount(video.commentsCount),
            icon = Icons.Default.ChatBubble,
            iconTint = Color.White,
            isActive = false,
            testTag = "comment_button_${video.id}",
            onClick = onOpenComments
        )

        // Subtle divider inside floating pill
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.15f))
        )

        // 3. SHARE BUTTON
        FloatingActionButtonItem(
            label = "Share",
            countText = formatCount(video.sharesCount),
            icon = Icons.Default.Share,
            iconTint = Color.White,
            isActive = false,
            testTag = "share_button_${video.id}",
            onClick = onOpenShare
        )
    }
}

@Composable
private fun FloatingActionButtonItem(
    label: String,
    countText: String,
    icon: ImageVector,
    iconTint: Color,
    isActive: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
            .testTag(testTag)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        // Reset scale after animation
        if (isPressed) {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(120)
                isPressed = false
            }
        }

        // Circular background for the icon
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) TikTokRed.copy(alpha = 0.2f) else Color(0x33FFFFFF)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Formatted count (e.g. 142.5K)
        Text(
            text = countText,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        // Explicit button label ('Like', 'Comment', 'Share')
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
