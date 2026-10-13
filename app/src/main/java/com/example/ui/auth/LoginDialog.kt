package com.example.ui.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LiteGold
import com.example.ui.theme.LiteGreen
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokRed

@Composable
fun LoginDialog(
    onLogin: (username: String, displayName: String, isGuest: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var isPhoneTab by remember { mutableStateOf(false) }
    var inputIdentifier by remember { mutableStateOf("") }
    var inputName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TikTokDarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("login_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = "Masuk ke TikTok Lite",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_login_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Feature Highlights
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = "⚡ Ringan <15MB", color = LiteGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "•", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "📶 Hemat Kuota 70%", color = TikTokCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "•", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "📱 Support Android 4+", color = LiteGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!isPhoneTab) {
                    // Option 1: Instant Guest Login (1-click entry)
                    Button(
                        onClick = {
                            val randomId = (100..999).random()
                            onLogin("kreator_lite_$randomId", "Pengguna Lite #$randomId", true)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(TikTokRed, Color(0xFFFF5277))
                                )
                            )
                            .testTag("guest_login_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Masuk Cepat",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Masuk Cepat 1 Detik (Tanpa Password)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 2: Phone / Email login option
                    Button(
                        onClick = { isPhoneTab = true },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("switch_to_phone_login")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Telepon",
                                tint = TikTokCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Gunakan Nomor HP atau Email",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 3: Google Login
                    Button(
                        onClick = {
                            onLogin("google_user_id", "Kreator TikTok Google", false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("google_login_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Google",
                                tint = LiteGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Lanjutkan dengan Akun Google",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    // Phone / Email form
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Masukkan Nomor HP / Email:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = inputIdentifier,
                            onValueChange = { inputIdentifier = it },
                            placeholder = { Text("0812xxxx atau email@domain.com", color = Color.Gray, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = TikTokCard,
                                unfocusedContainerColor = TikTokCard
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("phone_input_field")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Nama Tampilan:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            placeholder = { Text("Nama Anda", color = Color.Gray, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = TikTokCard,
                                unfocusedContainerColor = TikTokCard
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input_field")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val finalName = inputName.ifBlank { "Pengguna TikTok Lite" }
                                val finalUser = inputIdentifier.substringBefore("@").ifBlank { "pengguna_${System.currentTimeMillis() % 1000}" }
                                onLogin(finalUser, finalName, false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("submit_login_button")
                        ) {
                            Text(
                                text = "Masuk Sekarang",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { isPhoneTab = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Kembali ke opsi lain", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Dengan masuk, Anda menyetujui Ketentuan Layanan & Kebijakan Privasi TikTok Lite.",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
