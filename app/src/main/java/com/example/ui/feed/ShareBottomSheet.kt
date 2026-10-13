package com.example.ui.feed

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    videoId: String,
    onDismiss: () -> Unit,
    onCopyLink: () -> Unit,
    onShareTo: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkGray,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bagikan Video",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social platforms row
            Text(
                text = "Kirim ke",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ShareTargetItem(
                        title = "WhatsApp",
                        icon = Icons.Default.Whatsapp,
                        bgColor = Color(0xFF25D366),
                        testTag = "share_wa",
                        onClick = { onShareTo("WhatsApp") }
                    )
                }
                item {
                    ShareTargetItem(
                        title = "Salin Tautan",
                        icon = Icons.Default.ContentCopy,
                        bgColor = TikTokCyan,
                        iconTint = Color.Black,
                        testTag = "share_copy_link",
                        onClick = onCopyLink
                    )
                }
                item {
                    ShareTargetItem(
                        title = "Pesan SMS",
                        icon = Icons.Default.Message,
                        bgColor = Color(0xFF007AFF),
                        testTag = "share_sms",
                        onClick = { onShareTo("SMS") }
                    )
                }
                item {
                    ShareTargetItem(
                        title = "Simpan Video",
                        icon = Icons.Default.Download,
                        bgColor = LiteGreen,
                        iconTint = Color.Black,
                        testTag = "share_download",
                        onClick = { onShareTo("Penyimpanan Lokal") }
                    )
                }
                item {
                    ShareTargetItem(
                        title = "Laporkan",
                        icon = Icons.Default.Flag,
                        bgColor = Color(0xFF424242),
                        testTag = "share_report",
                        onClick = { onShareTo("Laporan") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ShareTargetItem(
    title: String,
    icon: ImageVector,
    bgColor: Color,
    testTag: String,
    iconTint: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
