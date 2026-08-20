package ru.netology.nmedia.dto

data class Post (
    val id: Long = 0,
    val author: String = "",
    val authorAvatar: String? = null,
    val published: Long = 0,
    val content: String = "",
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    val shareCount: Int = 0,
    val videoUrl: String = ""
)