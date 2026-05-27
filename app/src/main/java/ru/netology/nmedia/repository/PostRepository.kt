package ru.netology.nmedia.repository;

import androidx.lifecycle.LiveData;

import ru.netology.nmedia.dto.Post;

public interface PostRepository {
    fun get(): LiveData<Post>
    fun like()

    fun share()
}
