package ru.netology.nmedia.entity;

import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "likes")
    val likes: Int = 0,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "shareCount")
    val shareCount: Int = 0,
    @ColumnInfo(name = "videoUrl")
    val videoUrl: String = ""
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likes =likes,
        likedByMe = likedByMe,
        shareCount = shareCount,
        videoUrl =videoUrl
    )

    companion object {
        fun fromDto(dto:Post): PostEntity = with(dto){
            PostEntity(
                id = id,
                author = author,
                published = published,
                content = content,
                likes =likes,
                likedByMe = likedByMe,
                shareCount = shareCount,
                videoUrl =videoUrl
            )
        }
    }
}
