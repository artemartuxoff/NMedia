package ru.netology.nmedia.repository;

import ru.netology.nmedia.dto.Post;

public interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long, likedByMe: Boolean):Post

    fun shareById(id:Long)

    fun removeById(id:Long)

    fun save(post: Post):Post

    fun getAllAsync(callback: GetAllCallback)

    fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: LikeCallback)

    fun saveAsync(post: Post, callback: SaveCallback)

    interface GetAllCallback{

        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface LikeCallback{

        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }
    interface SaveCallback{

        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }


}
