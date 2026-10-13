package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DataSaverConfig
import com.example.data.model.QualityLevel
import com.example.ui.theme.LiteGold
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed

@Composable
fun DataSaverDialog(
    config: DataSaverConfig,
    onToggleDataSaver: (Boolean) -> Unit,
    onSetQuality: (QualityLevel) -> Unit,
    onToggleLowRamMode: (Boolean) -> Unit,
    onClearCache: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TikTokDarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("data_saver_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LiteGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Hemat Kuota",
                                tint = LiteGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Penghemat Kuota & RAM",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "TikTok Lite Turbo Engine",
                                color = LiteGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_data_saver_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Dashboard
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TikTokCard)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Sesi Ini", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Text(
                            text = "${config.currentSessionDataMb} MB",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Hemat Kuota", color = LiteGreen, fontSize = 11.sp)
                        Text(
                            text = "~${String.format("%.1f", config.totalDataSavedMb)} MB",
                            color = LiteGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Hemat RAM", color = TikTokCyan, fontSize = 11.sp)
                        Text(
                            text = "Hingga 80%",
                            color = TikTokCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Toggle 1: Hemat Kuota
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mode Hemat Kuota",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Kompresi video pintar, kurangi buffer",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = config.isDataSaverActive,
                        onCheckedChange = onToggleDataSaver,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = LiteGreen
                        ),
                        modifier = Modifier.testTag("toggle_data_saver_switch")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Toggle 2: Mode RAM Ringan (Android 4+ low spec optimization)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Optimasi Android Spek Rendah",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Nir-lag untuk HP RAM 512MB - 2GB / Android 4+",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = config.isLowRamMode,
                        onCheckedChange = onToggleLowRamMode,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = TikTokCyan
                        ),
                        modifier = Modifier.testTag("toggle_low_ram_switch")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quality Selector
                Text(
                    text = "Resolusi Pemutaran Video:",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    QualityLevel.entries.forEach { level ->
                        val isSelected = config.videoQuality == level
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF2A2A2A) else TikTokCard)
                                .border(
                                    width = if (isSelected) 1.dp else 0.dp,
                                    color = if (isSelected) LiteGreen else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onSetQuality(level) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = level.label,
                                    color = if (isSelected) LiteGreen else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${level.resolution} • ${level.bitRate}",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 10.sp
                                )
                            }
                            Text(
                                text = level.estMbPerMin,
                                color = if (isSelected) LiteGold else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button Clear Cache
                Button(
                    onClick = onClearCache,
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("clear_cache_dialog_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Bersihkan Cache",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bersihkan Cache & Percepat HP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
