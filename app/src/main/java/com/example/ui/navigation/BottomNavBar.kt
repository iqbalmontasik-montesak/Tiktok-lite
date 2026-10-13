package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.viewmodel.AppScreen

@Composable
fun BottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .navigationBarsPadding()
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                title = "Beranda",
                icon = Icons.Default.Home,
                isSelected = currentScreen == AppScreen.HOME,
                testTag = "nav_home",
                onClick = { onNavigate(AppScreen.HOME) }
            )

            NavItem(
                title = "Temukan",
                icon = Icons.Default.Search,
                isSelected = currentScreen == AppScreen.DISCOVER,
                testTag = "nav_discover",
                onClick = { onNavigate(AppScreen.DISCOVER) }
            )

            // Signature TikTok Plus Add Button
            TikTokPlusButton(
                onClick = { onNavigate(AppScreen.CREATE) }
            )

            NavItem(
                title = "Kotak Masuk",
                icon = Icons.Default.ChatBubbleOutline,
                isSelected = currentScreen == AppScreen.INBOX,
                testTag = "nav_inbox",
                onClick = { onNavigate(AppScreen.INBOX) }
            )

            NavItem(
                title = "Profil",
                icon = Icons.Default.PersonOutline,
                isSelected = currentScreen == AppScreen.PROFILE,
                testTag = "nav_profile",
                onClick = { onNavigate(AppScreen.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun TikTokPlusButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .testTag("nav_create_button")
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Cyan and Red layer illusion
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(28.dp)
        ) {
            // Cyan background left offset
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TikTokCyan)
                    .align(Alignment.CenterStart)
            )
            // Red background right offset
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TikTokRed)
                    .align(Alignment.CenterEnd)
            )
            // White core center
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Buat Video",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
