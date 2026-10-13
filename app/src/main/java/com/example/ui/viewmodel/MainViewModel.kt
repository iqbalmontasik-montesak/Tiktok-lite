package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CommentItem
import com.example.data.model.DataSaverConfig
import com.example.data.model.QualityLevel
import com.example.data.model.UserProfile
import com.example.data.model.VideoItem
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME, DISCOVER, CREATE, INBOX, PROFILE
}

enum class HomeTab {
    FOR_YOU, FOLLOWING
}

class MainViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    val videos: StateFlow<List<VideoItem>> = repository.videos
    val commentsMap: StateFlow<Map<String, List<CommentItem>>> = repository.comments
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val dataSaverConfig: StateFlow<DataSaverConfig> = repository.dataSaverConfig

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _homeTab = MutableStateFlow(HomeTab.FOR_YOU)
    val homeTab: StateFlow<HomeTab> = _homeTab.asStateFlow()

    private val _activeCommentVideoId = MutableStateFlow<String?>(null)
    val activeCommentVideoId: StateFlow<String?> = _activeCommentVideoId.asStateFlow()

    private val _activeShareVideoId = MutableStateFlow<String?>(null)
    val activeShareVideoId: StateFlow<String?> = _activeShareVideoId.asStateFlow()

    private val _showDataSaverDialog = MutableStateFlow(false)
    val showDataSaverDialog: StateFlow<Boolean> = _showDataSaverDialog.asStateFlow()

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog: StateFlow<Boolean> = _showLoginDialog.asStateFlow()

    private val _isGlobalMuted = MutableStateFlow(false)
    val isGlobalMuted: StateFlow<Boolean> = _isGlobalMuted.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setHomeTab(tab: HomeTab) {
        _homeTab.value = tab
    }

    fun toggleMute() {
        _isGlobalMuted.value = !_isGlobalMuted.value
    }

    fun openComments(videoId: String) {
        _activeCommentVideoId.value = videoId
    }

    fun closeComments() {
        _activeCommentVideoId.value = null
    }

    fun openShare(videoId: String) {
        _activeShareVideoId.value = videoId
    }

    fun closeShare() {
        _activeShareVideoId.value = null
    }

    fun openDataSaverDialog() {
        _showDataSaverDialog.value = true
    }

    fun closeDataSaverDialog() {
        _showDataSaverDialog.value = false
    }

    fun openLoginDialog() {
        _showLoginDialog.value = true
    }

    fun closeLoginDialog() {
        _showLoginDialog.value = false
    }

    fun toggleLike(videoId: String) {
        repository.toggleLike(videoId)
    }

    fun toggleBookmark(videoId: String) {
        repository.toggleBookmark(videoId)
        showNotification("Video disimpan ke Favorit")
    }

    fun toggleFollow(videoId: String) {
        repository.toggleFollow(videoId)
        showNotification("Berhasil memperbarui status mengikuti")
    }

    fun addComment(videoId: String, text: String) {
        if (text.isBlank()) return
        repository.addComment(videoId, text.trim())
        showNotification("Komentar terkirim!")
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        repository.toggleCommentLike(videoId, commentId)
    }

    fun shareVideo(videoId: String, channelName: String) {
        repository.shareVideo(videoId)
        closeShare()
        showNotification("Dibagikan ke $channelName!")
    }

    fun copyVideoLink(videoId: String) {
        repository.shareVideo(videoId)
        closeShare()
        showNotification("Tautan video berhasil disalin!")
    }

    fun toggleDataSaver(enabled: Boolean) {
        repository.toggleDataSaver(enabled)
        val text = if (enabled) "Mode Hemat Kuota Diaktifkan ⚡" else "Mode Kualitas Standar Aktif"
        showNotification(text)
    }

    fun setQualityLevel(quality: QualityLevel) {
        repository.setQualityLevel(quality)
        showNotification("Kualitas diubah ke ${quality.label}")
    }

    fun toggleLowRamMode(enabled: Boolean) {
        repository.toggleLowRamMode(enabled)
        val text = if (enabled) "Mode RAM Ringan Aktif (Cocok HP Android 4+)" else "Mode Grafis Standar"
        showNotification(text)
    }

    fun clearCache() {
        val freedMb = repository.clearCache()
        showNotification(String.format("%.1f MB memori cache berhasil dibersihkan! 🚀", freedMb))
    }

    fun uploadVideo(caption: String, soundTitle: String, tags: List<String>) {
        repository.addNewVideo(caption, soundTitle, tags)
        _currentScreen.value = AppScreen.HOME
        showNotification("Video berhasil diunggah ke TikTok Lite!")
    }

    fun login(username: String, displayName: String, isGuest: Boolean) {
        repository.login(username, displayName, isGuest)
        closeLoginDialog()
        val text = if (isGuest) "Masuk sebagai Tamu Cepat!" else "Selamat datang kembali, $displayName!"
        showNotification(text)
    }

    fun logout() {
        repository.logout()
        showNotification("Anda keluar dari akun")
    }

    private fun showNotification(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }
}
