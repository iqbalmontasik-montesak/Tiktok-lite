package com.example.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.feed.formatCount
import com.example.ui.theme.LiteGold
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DiscoverScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.videos.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("Semua") }

    val trendingTags = listOf("Semua", "#fyp", "#tiktoklite", "#komedi", "#reseptiktok", "#pesonaindonesia", "#tipshp")

    val filteredList = videos.filter { video ->
        val matchesQuery = searchQuery.isEmpty() ||
                video.caption.contains(searchQuery, ignoreCase = true) ||
                video.username.contains(searchQuery, ignoreCase = true) ||
                video.creatorName.contains(searchQuery, ignoreCase = true)

        val matchesTag = selectedTag == "Semua" || video.tags.any { it.equals(selectedTag, ignoreCase = true) }

        matchesQuery && matchesTag
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .testTag("discover_screen")
    ) {
        // Search Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Cari video, kreator, atau musik...", color = Color.Gray, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = TikTokCard,
                    unfocusedContainerColor = TikTokCard,
                    focusedBorderColor = TikTokCyan,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("discover_search_field")
            )
        }

        // Trending Tags row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(trendingTags) { tag ->
                val isSelected = selectedTag == tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) TikTokRed else TikTokCard)
                        .clickable { selectedTag = tag }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("filter_tag_$tag")
                ) {
                    Text(
                        text = tag,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Promotional Banner: "Hemat Kuota & Cepat"
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(LiteGreen.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Turbo",
                        tint = LiteGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Jelajahi Konten Lite Viral",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Kecepatan maksimal, buffering 0 detik, data irit",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Grid of videos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList, key = { it.id }) { video ->
                DiscoverVideoCard(
                    video = video,
                    onClick = {
                        viewModel.navigateTo(AppScreen.HOME)
                    }
                )
            }
        }
    }
}

@Composable
private fun DiscoverVideoCard(
    video: VideoItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TikTokDarkGray),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("discover_card_${video.id}")
    ) {
        Column {
            // Visual aspect thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
                    .background(Color(video.primaryColorHex))
            ) {
                // Play count chip
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x88000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Views",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = formatCount(video.likesCount * 3),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Lite Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xAA000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.resolutionTag,
                        color = LiteGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info below thumbnail
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = video.caption,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = video.username,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "❤️ ${formatCount(video.likesCount)}",
                        color = TikTokRed,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
