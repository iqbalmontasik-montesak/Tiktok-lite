package com.example.data.model

data class VideoItem(
    val id: String,
    val videoUrl: String,
    val creatorName: String,
    val username: String,
    val creatorAvatar: String,
    val caption: String,
    val soundTitle: String,
    val likesCount: Long,
    val commentsCount: Long,
    val sharesCount: Long,
    val bookmarksCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val isFollowing: Boolean = false,
    val tags: List<String> = emptyList(),
    val resolutionTag: String = "360p Lite",
    val estimatedDataMb: Double = 0.8,
    val primaryColorHex: Long = 0xFF1A1A24
)

data class CommentItem(
    val id: String,
    val username: String,
    val userAvatar: String,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class UserProfile(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val bio: String,
    val followingCount: Int,
    val followersCount: Int,
    val likesCount: String,
    val isLoggedIn: Boolean = true,
    val isGuest: Boolean = false
)

data class DataSaverConfig(
    val isDataSaverActive: Boolean = true,
    val videoQuality: QualityLevel = QualityLevel.LITE_360P,
    val isLowRamMode: Boolean = true,
    val autoPlayNext: Boolean = true,
    val cacheClearedMb: Float = 0f,
    val totalDataSavedMb: Float = 42.5f,
    val currentSessionDataMb: Float = 1.8f
)

enum class QualityLevel(val label: String, val resolution: String, val bitRate: String, val estMbPerMin: String) {
    ULTRA_240P("Ultra Hemat (240p)", "426x240", "200 kbps", "~1.5 MB/menit"),
    LITE_360P("Hemat Lite (360p)", "640x360", "450 kbps", "~3.2 MB/menit"),
    STANDARD_480P("Standar (480p)", "854x480", "800 kbps", "~6.0 MB/menit")
}
