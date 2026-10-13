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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommentItem
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    videoId: String,
    comments: List<CommentItem>,
    onDismiss: () -> Unit,
    onAddComment: (String) -> Unit,
    onToggleLike: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var commentText by remember { mutableStateOf("") }
    val quickEmojis = listOf("❤️", "😂", "🔥", "👏", "🥰", "🙌")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkGray,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "${comments.size} Komentar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_comments_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (comments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada komentar. Jadilah yang pertama berkomentar!",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    items(comments, key = { it.id }) { item ->
                        CommentRow(
                            item = item,
                            onToggleLike = { onToggleLike(item.id) }
                        )
                    }
                }
            }

            // Quick Emojis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TikTokSurface)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                quickEmojis.forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable { commentText += emoji }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TikTokDarkGray)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = {
                        Text(
                            text = "Tambahkan komentar...",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = TikTokCard,
                        unfocusedContainerColor = TikTokCard,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_input_field"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onAddComment(commentText)
                            commentText = ""
                        }
                    },
                    modifier = Modifier.testTag("send_comment_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Kirim",
                        tint = if (commentText.isNotBlank()) TikTokRed else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentRow(
    item: CommentItem,
    onToggleLike: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Initial Avatar circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(TikTokCard),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.username.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.username,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.text,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.timeAgo,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 11.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onToggleLike() }
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Suka Komentar",
                tint = if (item.isLiked) TikTokRed else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
            if (item.likesCount > 0) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.likesCount.toString(),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }
        }
    }
}
