package ru.netology.nmedia.repository;

import ru.netology.nmedia.dto.Post;

public interface PostRepository {
    fun getAll(): List<Post>

    fun likeById(id: Long, likedByMe: Boolean):Post?

    fun shareById(id:Long)

    fun removeById(id:Long)

    fun save(post: Post): Post?

    fun getAllAsync(callback: PostRepository.Callback<List<Post>>)

    fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: Callback<Post?>)

    fun saveAsync(post: Post, callback: Callback<Post?>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Throwable) {}
    }






}
