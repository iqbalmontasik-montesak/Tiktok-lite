package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.auth.LoginDialog
import com.example.ui.components.DataSaverDialog
import com.example.ui.create.CreateVideoScreen
import com.example.ui.discover.DiscoverScreen
import com.example.ui.feed.CommentBottomSheet
import com.example.ui.feed.ShareBottomSheet
import com.example.ui.feed.VideoFeedScreen
import com.example.ui.inbox.InboxScreen
import com.example.ui.navigation.BottomNavBar
import com.example.ui.profile.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TikTokLiteApp()
            }
        }
    }
}

@Composable
fun TikTokLiteApp(
    viewModel: MainViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeCommentVideoId by viewModel.activeCommentVideoId.collectAsState()
    val activeShareVideoId by viewModel.activeShareVideoId.collectAsState()
    val showDataSaverDialog by viewModel.showDataSaverDialog.collectAsState()
    val showLoginDialog by viewModel.showLoginDialog.collectAsState()
    val dataSaverConfig by viewModel.dataSaverConfig.collectAsState()
    val commentsMap by viewModel.commentsMap.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to snackbar messages from ViewModel
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Android Hardware / Predictive Back Handler
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = TikTokCard,
                    contentColor = Color.White,
                    actionColor = TikTokCyan
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.CREATE) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentScreen != AppScreen.CREATE) innerPadding.calculateBottomPadding() else 0.dp)
                .background(Color.Black)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> VideoFeedScreen(viewModel = viewModel)
                AppScreen.DISCOVER -> DiscoverScreen(viewModel = viewModel)
                AppScreen.CREATE -> CreateVideoScreen(viewModel = viewModel)
                AppScreen.INBOX -> InboxScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }
    }

    // Comments Sheet
    activeCommentVideoId?.let { videoId ->
        val comments = commentsMap[videoId] ?: emptyList()
        CommentBottomSheet(
            videoId = videoId,
            comments = comments,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(videoId, text) },
            onToggleLike = { commentId -> viewModel.toggleCommentLike(videoId, commentId) }
        )
    }

    // Share Sheet
    activeShareVideoId?.let { videoId ->
        ShareBottomSheet(
            videoId = videoId,
            onDismiss = { viewModel.closeShare() },
            onCopyLink = { viewModel.copyVideoLink(videoId) },
            onShareTo = { channel -> viewModel.shareVideo(videoId, channel) }
        )
    }

    // Data Saver Dialog
    if (showDataSaverDialog) {
        DataSaverDialog(
            config = dataSaverConfig,
            onToggleDataSaver = { viewModel.toggleDataSaver(it) },
            onSetQuality = { viewModel.setQualityLevel(it) },
            onToggleLowRamMode = { viewModel.toggleLowRamMode(it) },
            onClearCache = { viewModel.clearCache() },
            onDismiss = { viewModel.closeDataSaverDialog() }
        )
    }

    // Login Dialog
    if (showLoginDialog) {
        LoginDialog(
            onLogin = { username, name, isGuest ->
                viewModel.login(username, name, isGuest)
            },
            onDismiss = { viewModel.closeLoginDialog() }
        )
    }
}
