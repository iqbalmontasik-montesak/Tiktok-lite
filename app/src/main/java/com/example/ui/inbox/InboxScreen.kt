package com.example.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LiteGold
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed
import com.example.ui.viewmodel.MainViewModel

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val icon: ImageVector,
    val iconBg: Color,
    val category: String
)

@Composable
fun InboxScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Suka", "Komentar", "Pengikut", "Sistem")

    val notifications = remember {
        listOf(
            NotificationItem(
                id = "n1",
                title = "Tim TikTok Lite",
                description = "Mode Hemat Kuota Anda telah aktif. Penghematan data hingga 70% berjalan optimal!",
                timeAgo = "10 menit lalu",
                icon = Icons.Default.Shield,
                iconBg = LiteGreen,
                category = "Sistem"
            ),
            NotificationItem(
                id = "n2",
                title = "BudiSantoso menyukai video Anda",
                description = "\"Tips HP RAM 1GB / 2GB agar anti lelet dan main TikTok lancar\"",
                timeAgo = "1 jam lalu",
                icon = Icons.Default.Favorite,
                iconBg = TikTokRed,
                category = "Suka"
            ),
            NotificationItem(
                id = "n3",
                title = "Siti_Rahma berkomentar",
                description = "\"Wkwkwk persis banget adek gue kalau disuruh ke warung 😂\"",
                timeAgo = "2 jam lalu",
                icon = Icons.Default.ChatBubble,
                iconBg = TikTokCyan,
                category = "Komentar"
            ),
            NotificationItem(
                id = "n4",
                title = "AryaTraveler mulai mengikuti Anda",
                description = "Mulai ikuti balik untuk melihat video terbaru dari kreator ini.",
                timeAgo = "5 jam lalu",
                icon = Icons.Default.PersonAdd,
                iconBg = LiteGold,
                category = "Pengikut"
            )
        )
    }

    val filteredList = if (selectedCategory == "Semua") {
        notifications
    } else {
        notifications.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .testTag("inbox_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kotak Masuk",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        // Category filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color.White else TikTokCard)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("inbox_filter_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Notifications List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TikTokDarkGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(item.iconBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.category,
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.description,
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.timeAgo,
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
