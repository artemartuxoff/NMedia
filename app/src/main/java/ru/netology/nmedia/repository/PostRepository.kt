package ru.netology.nmedia.repository;

import androidx.lifecycle.LiveData;

import ru.netology.nmedia.dto.Post;

public interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id:Long):Post

    fun shareById(id:Long)

    fun removeById(id:Long)

    fun save(post: Post):Post
}
