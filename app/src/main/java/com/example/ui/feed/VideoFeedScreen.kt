package com.example.ui.feed

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.HomeTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun VideoFeedScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.videos.collectAsState()
    val homeTab by viewModel.homeTab.collectAsState()
    val isMuted by viewModel.isGlobalMuted.collectAsState()
    val dataSaverConfig by viewModel.dataSaverConfig.collectAsState()

    val filteredVideos = if (homeTab == HomeTab.FOLLOWING) {
        videos.filter { it.isFollowing }.ifEmpty { videos.take(2) }
    } else {
        videos
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { filteredVideos.size }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_feed_screen")
    ) {
        // Vertical Pager for snapping full-screen TikTok videos
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val video = filteredVideos[pageIndex]
            val isActive = pagerState.currentPage == pageIndex

            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayerView(
                    video = video,
                    isActive = isActive,
                    isMuted = isMuted,
                    isLowRamMode = dataSaverConfig.isLowRamMode,
                    onDoubleTap = { viewModel.toggleLike(video.id) }
                )

                VideoOverlay(
                    video = video,
                    isMuted = isMuted,
                    onToggleMute = { viewModel.toggleMute() },
                    onToggleLike = { viewModel.toggleLike(video.id) },
                    onToggleBookmark = { viewModel.toggleBookmark(video.id) },
                    onToggleFollow = { viewModel.toggleFollow(video.id) },
                    onOpenComments = { viewModel.openComments(video.id) },
                    onOpenShare = { viewModel.openShare(video.id) },
                    onOpenDataSaver = { viewModel.openDataSaverDialog() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // Top Navigation Bar (Tabs: Mengikuti | Untuk Anda, Search icon, Data Saver pill)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Data Saver quick button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x88000000))
                        .clickable { viewModel.openDataSaverDialog() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("top_data_saver_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Data Saver",
                        tint = if (dataSaverConfig.isDataSaverActive) LiteGreen else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (dataSaverConfig.isDataSaverActive) "Lite ON" else "Standar",
                        color = if (dataSaverConfig.isDataSaverActive) LiteGreen else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center: Tabs Mengikuti | Untuk Anda
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TabItem(
                        title = "Mengikuti",
                        isSelected = homeTab == HomeTab.FOLLOWING,
                        testTag = "tab_following",
                        onClick = { viewModel.setHomeTab(HomeTab.FOLLOWING) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "|", color = Color.White.copy(alpha = 0.3f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    TabItem(
                        title = "Untuk Anda",
                        isSelected = homeTab == HomeTab.FOR_YOU,
                        testTag = "tab_foryou",
                        onClick = { viewModel.setHomeTab(HomeTab.FOR_YOU) }
                    )
                }

                // Right: Search icon
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.DISCOVER) },
                    modifier = Modifier.testTag("search_top_icon")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TabItem(
    title: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = if (isSelected) 17.sp else 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}
