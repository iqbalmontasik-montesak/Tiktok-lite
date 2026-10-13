package com.example.data.repository

import com.example.data.model.CommentItem
import com.example.data.model.DataSaverConfig
import com.example.data.model.QualityLevel
import com.example.data.model.UserProfile
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoRepository {

    // Initial curated lightweight video feed
    private val initialVideos = listOf(
        VideoItem(
            id = "vid_1",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            creatorName = "Dita Gaming & Comedy",
            username = "@ditacomedy",
            creatorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=60",
            caption = "Ketika disuruh beli telur 1kg tapi dapatnya malah ini 😂😭 Tag teman kalian yang sering pelupa!",
            soundTitle = "🎵 Suara Asli - Dita Comedy Viral",
            likesCount = 142500,
            commentsCount = 2840,
            sharesCount = 11200,
            bookmarksCount = 8900,
            tags = listOf("#fyp", "#komedi", "#tiktoklite", "#relatable"),
            resolutionTag = "360p Lite",
            estimatedDataMb = 0.6,
            primaryColorHex = 0xFF2A1530
        ),
        VideoItem(
            id = "vid_2",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            creatorName = "Resep Dapur Kilat",
            username = "@dapurkilat_id",
            creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=60",
            caption = "Cuma 3 bahan bisa bikin camilan seenak kafe mahal! Hemat biaya & kuota tontonan 🥞✨ #resepsimpel",
            soundTitle = "🎵 Cooking Beats - LoFi Kitchen",
            likesCount = 89300,
            commentsCount = 1205,
            sharesCount = 6300,
            bookmarksCount = 22400,
            tags = listOf("#reseptiktok", "#kuliner", "#masakhemat", "#fyp"),
            resolutionTag = "360p Lite",
            estimatedDataMb = 0.5,
            primaryColorHex = 0xFF1E2D24
        ),
        VideoItem(
            id = "vid_3",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            creatorName = "Travel Nusantara",
            username = "@jelajahalam.id",
            creatorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=60",
            caption = "Hidden gem pantai pasir putih tercantik di Indonesia Timur! Wajib save buat liburan nanti 🌊🏝️",
            soundTitle = "🎵 Suara Asli - Alam Indah Indonesia",
            likesCount = 312000,
            commentsCount = 4590,
            sharesCount = 18900,
            bookmarksCount = 47800,
            tags = listOf("#pesonaindonesia", "#pantai", "#traveltiktok", "#fyp"),
            resolutionTag = "360p Lite",
            estimatedDataMb = 0.7,
            primaryColorHex = 0xFF142B3B
        ),
        VideoItem(
            id = "vid_4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            creatorName = "Tips HP Ringan & Android",
            username = "@trikgadget_lite",
            creatorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=60",
            caption = "Cara bersihkan memori HP RAM 1GB / 2GB agar anti lelet dan main TikTok lancar jaya! 🚀📲",
            soundTitle = "🎵 Beat Edukasi - TechHacks ID",
            likesCount = 205600,
            commentsCount = 3710,
            sharesCount = 29400,
            bookmarksCount = 31200,
            tags = listOf("#tipshp", "#androidringan", "#antigelag", "#tiktoklite"),
            resolutionTag = "240p Ultra",
            estimatedDataMb = 0.4,
            primaryColorHex = 0xFF2B1C10
        ),
        VideoItem(
            id = "vid_5",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            creatorName = "Rendy Dance Crew",
            username = "@rendydance",
            creatorAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150&auto=format&fit=crop&q=60",
            caption = "Challenge dance viral minggu ini! Gerakannya gampang banget, coba duet sekarang 🔥🕺",
            soundTitle = "🎵 Viral Dance Sound - Sound Seleb",
            likesCount = 512400,
            commentsCount = 8900,
            sharesCount = 34500,
            bookmarksCount = 15300,
            tags = listOf("#dancechallenge", "#trending", "#fypviral"),
            resolutionTag = "360p Lite",
            estimatedDataMb = 0.6,
            primaryColorHex = 0xFF351221
        )
    )

    private val initialComments = mapOf(
        "vid_1" to listOf(
            CommentItem("c1", "BudiSantoso", "", "Wkwkwk persis banget adek gue kalau disuruh ke warung 😂", "2j lalu", 423),
            CommentItem("c2", "Siti_Rahma", "", "Suaranya pas banget bikin ngakak guling-guling 🤣", "3j lalu", 188),
            CommentItem("c3", "RianGaming", "", "Aplikasi TikTok Lite ini beneran lancar banget di HP jadulku!", "4j lalu", 95)
        ),
        "vid_2" to listOf(
            CommentItem("c4", "MamaKreatif", "", "Resepnya gampang banget besok mau langsung coba!", "1j lalu", 312),
            CommentItem("c5", "DiniCulinary", "", "Cuma 3 bahan ya kak? Mantap hemat budget anak kos", "3j lalu", 145)
        ),
        "vid_3" to listOf(
            CommentItem("c6", "PetualangSejati", "", "MasyaAllah indahnya ciptaan Tuhan, lokasi persisnya di mana kak?", "5j lalu", 560),
            CommentItem("c7", "AryaTraveler", "", "Airnya jernih banget kayak kaca! Keren videonya", "6j lalu", 220)
        ),
        "vid_4" to listOf(
            CommentItem("c8", "AntoLekat", "", "Makasih tipsnya bang, HP RAM 2GB saya jadi enteng lagi", "1j lalu", 890),
            CommentItem("c9", "Kholid_Tech", "", "TikTok Lite emang paling bersahabat buat kuota pas-pasan 👍", "2j lalu", 410)
        ),
        "vid_5" to listOf(
            CommentItem("c10", "Chika_Dance", "", "Gerakannya lincah banget! Keren abis 🔥", "1j lalu", 1200),
            CommentItem("c11", "AgusSetiawan", "", "Lagu ini emang nagih banget di kepala haha", "3j lalu", 340)
        )
    )

    private val _videos = MutableStateFlow(initialVideos)
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _comments = MutableStateFlow(initialComments)
    val comments: StateFlow<Map<String, List<CommentItem>>> = _comments.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "user_me",
            username = "@pengguna_lite",
            displayName = "Pengguna TikTok Lite",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=60",
            bio = "Menikmati video pendek seru, hemat kuota & anti lelet! ✨📱\nBergabung sejak 2026",
            followingCount = 48,
            followersCount = 129,
            likesCount = "1.4K",
            isLoggedIn = true,
            isGuest = true
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _dataSaverConfig = MutableStateFlow(DataSaverConfig())
    val dataSaverConfig: StateFlow<DataSaverConfig> = _dataSaverConfig.asStateFlow()

    fun toggleLike(videoId: String) {
        _videos.update { list ->
            list.map { item ->
                if (item.id == videoId) {
                    val newIsLiked = !item.isLiked
                    val newCount = if (newIsLiked) item.likesCount + 1 else (item.likesCount - 1).coerceAtLeast(0)
                    item.copy(isLiked = newIsLiked, likesCount = newCount)
                } else item
            }
        }
    }

    fun toggleBookmark(videoId: String) {
        _videos.update { list ->
            list.map { item ->
                if (item.id == videoId) {
                    val newBookmarked = !item.isBookmarked
                    val newCount = if (newBookmarked) item.bookmarksCount + 1 else (item.bookmarksCount - 1).coerceAtLeast(0)
                    item.copy(isBookmarked = newBookmarked, bookmarksCount = newCount)
                } else item
            }
        }
    }

    fun toggleFollow(videoId: String) {
        _videos.update { list ->
            list.map { item ->
                if (item.id == videoId) {
                    val newFollowing = !item.isFollowing
                    item.copy(isFollowing = newFollowing)
                } else item
            }
        }
    }

    fun addComment(videoId: String, text: String) {
        val currentList = _comments.value[videoId] ?: emptyList()
        val newComment = CommentItem(
            id = "c_${System.currentTimeMillis()}",
            username = _userProfile.value.username.removePrefix("@"),
            userAvatar = _userProfile.value.avatarUrl,
            text = text,
            timeAgo = "Baru saja",
            likesCount = 0
        )
        _comments.update { currentMap ->
            currentMap + (videoId to (listOf(newComment) + currentList))
        }
        _videos.update { list ->
            list.map { item ->
                if (item.id == videoId) item.copy(commentsCount = item.commentsCount + 1) else item
            }
        }
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        val currentList = _comments.value[videoId] ?: return
        val updated = currentList.map { c ->
            if (c.id == commentId) {
                val newLiked = !c.isLiked
                c.copy(isLiked = newLiked, likesCount = if (newLiked) c.likesCount + 1 else c.likesCount - 1)
            } else c
        }
        _comments.update { map -> map + (videoId to updated) }
    }

    fun shareVideo(videoId: String) {
        _videos.update { list ->
            list.map { item ->
                if (item.id == videoId) item.copy(sharesCount = item.sharesCount + 1) else item
            }
        }
    }

    fun addNewVideo(caption: String, soundTitle: String, tags: List<String>) {
        val newVideo = VideoItem(
            id = "vid_user_${System.currentTimeMillis()}",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            creatorName = _userProfile.value.displayName,
            username = _userProfile.value.username,
            creatorAvatar = _userProfile.value.avatarUrl,
            caption = caption,
            soundTitle = soundTitle,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            bookmarksCount = 0,
            tags = tags,
            resolutionTag = "360p Lite",
            estimatedDataMb = 0.5,
            primaryColorHex = 0xFF192A32
        )
        _videos.update { listOf(newVideo) + it }
    }

    fun toggleDataSaver(enabled: Boolean) {
        _dataSaverConfig.update { it.copy(isDataSaverActive = enabled) }
    }

    fun setQualityLevel(quality: QualityLevel) {
        _dataSaverConfig.update { it.copy(videoQuality = quality) }
    }

    fun toggleLowRamMode(enabled: Boolean) {
        _dataSaverConfig.update { it.copy(isLowRamMode = enabled) }
    }

    fun clearCache(): Float {
        val freedMb = 18.4f + (Math.random() * 4.2).toFloat()
        _dataSaverConfig.update {
            it.copy(
                cacheClearedMb = it.cacheClearedMb + freedMb,
                totalDataSavedMb = it.totalDataSavedMb + freedMb * 0.5f
            )
        }
        return freedMb
    }

    fun login(username: String, displayName: String, isGuest: Boolean) {
        _userProfile.update {
            it.copy(
                username = if (username.startsWith("@")) username else "@$username",
                displayName = displayName,
                isLoggedIn = true,
                isGuest = isGuest
            )
        }
    }

    fun logout() {
        _userProfile.update {
            it.copy(
                username = "@tamu_lite",
                displayName = "Mode Tamu (Klik untuk Masuk)",
                isLoggedIn = false,
                isGuest = true
            )
        }
    }
}
