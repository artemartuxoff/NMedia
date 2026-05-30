package ru.netology.nmedia.repository;

import androidx.lifecycle.LiveData;

import ru.netology.nmedia.dto.Post;

public interface PostRepository {
    fun getall(): LiveData<List<Post>>
    fun likeById(id:Long)

    fun shareById(id:Long)
}
