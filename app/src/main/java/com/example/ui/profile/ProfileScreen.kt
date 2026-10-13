package com.example.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val dataSaverConfig by viewModel.dataSaverConfig.collectAsState()
    val videos by viewModel.videos.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: My Videos, 1: Liked, 2: Bookmarks

    val displayVideos = when (selectedTab) {
        0 -> videos.take(3)
        1 -> videos.filter { it.isLiked }
        else -> videos.filter { it.isBookmarked }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userProfile.displayName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Row {
                IconButton(
                    onClick = { viewModel.openDataSaverDialog() },
                    modifier = Modifier.testTag("profile_data_saver_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Pengaturan Hemat Kuota",
                        tint = LiteGreen
                    )
                }
                IconButton(
                    onClick = { viewModel.openLoginDialog() },
                    modifier = Modifier.testTag("profile_login_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Ganti / Masuk Akun",
                        tint = Color.White
                    )
                }
            }
        }

        // Profile Avatar and Info
        Box(
            modifier = Modifier.padding(top = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = userProfile.avatarUrl,
                contentDescription = "Avatar Profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .border(2.dp, TikTokCyan, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(TikTokRed)
                    .clickable { viewModel.openLoginDialog() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Ubah",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = userProfile.username,
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Stats Row (Mengikuti, Pengikut, Suka)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileStatItem(count = "${userProfile.followingCount}", label = "Mengikuti")
            ProfileStatItem(count = "${userProfile.followersCount}", label = "Pengikut")
            ProfileStatItem(count = userProfile.likesCount, label = "Suka")
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions Row (Edit Profile & Data Saver Control)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.openLoginDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("edit_profile_btn")
            ) {
                Text(
                    text = if (userProfile.isGuest) "Masuk Akun" else "Ganti Akun",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = { viewModel.clearCache() },
                colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("clear_cache_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.CleaningServices,
                    contentDescription = "Bersihkan Cache",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bersihkan Cache",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Data Saver Dashboard Mini Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = TikTokDarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { viewModel.openDataSaverDialog() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Data Saver",
                        tint = LiteGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Mode Hemat Kuota: Aktif",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kualitas: ${dataSaverConfig.videoQuality.label}",
                            color = LiteGreen,
                            fontSize = 11.sp
                        )
                    }
                }
                Text(
                    text = "Hemat ~${String.format("%.1f", dataSaverConfig.totalDataSavedMb)} MB",
                    color = LiteGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Selector (Grid Videos, Liked, Bookmarks)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TikTokDarkGray)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TabIconButton(
                icon = Icons.Default.GridView,
                isSelected = selectedTab == 0,
                testTag = "profile_tab_my_videos",
                onClick = { selectedTab = 0 }
            )
            TabIconButton(
                icon = Icons.Default.Favorite,
                isSelected = selectedTab == 1,
                testTag = "profile_tab_liked",
                onClick = { selectedTab = 1 }
            )
            TabIconButton(
                icon = Icons.Default.Bookmark,
                isSelected = selectedTab == 2,
                testTag = "profile_tab_bookmarks",
                onClick = { selectedTab = 2 }
            )
        }

        // Grid Content
        if (displayVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (selectedTab) {
                        0 -> "Belum ada video yang diunggah."
                        1 -> "Belum ada video yang disukai."
                        else -> "Belum ada video yang disimpan."
                    },
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayVideos, key = { it.id }) { video ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(0.8f)
                            .background(Color(video.primaryColorHex))
                            .clickable { viewModel.navigateTo(AppScreen.HOME) }
                            .testTag("profile_video_thumb_${video.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Views",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = formatCount(video.likesCount),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TabIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(vertical = 6.dp, horizontal = 24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
