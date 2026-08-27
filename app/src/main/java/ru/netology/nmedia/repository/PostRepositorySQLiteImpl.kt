package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi

class PostRepositorySQLiteImpl : PostRepository {

    override fun getAll(): List<Post> {
        return PostsApi.retrofitService.getAll()
            .execute()
            .body()
            .orEmpty()
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {

        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun save(post: Post): Post? {
        return PostsApi.retrofitService.save(post)
            .execute()
            .body()
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post?>) {

        PostsApi.retrofitService.save(post)
            .enqueue(object : Callback<Post> {

                override fun onResponse(
                    call: Call<Post>,
                    response: Response<Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    else{
                        callback.onSuccess(response.body())
                    }

                }

                override fun onFailure(
                    call: Call<Post>,
                    t: Throwable
                ) {
                    callback.onError(t)
                }
            })
    }

    override fun removeById(id: Long) {

        PostsApi.retrofitService.removeById(id)
            .execute()
    }

    override fun likeById(id: Long, likedByMe: Boolean): Post? {

        if (likedByMe) {
            return PostsApi.retrofitService.dislikeById(id)
                .execute()
                .body()
        } else {
            return PostsApi.retrofitService.likeById(id)
                .execute()
                .body()
        }
    }

    override fun likeByIdAsync(
        id: Long,
        likedByMe: Boolean,
        callback: PostRepository.Callback<Post?>
    ) {

        if (likedByMe) {

            PostsApi.retrofitService.dislikeById(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(
                        call: Call<Post?>,
                        response: Response<Post?>
                    ) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }

                        callback.onSuccess(response.body())
                    }

                    override fun onFailure(
                        call: Call<Post?>,
                        t: Throwable
                    ) {
                        callback.onError(t)
                    }
                })
        } else {
            PostsApi.retrofitService.likeById(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(
                        call: Call<Post?>,
                        response: Response<Post?>
                    ) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }

                        callback.onSuccess(response.body())
                    }

                    override fun onFailure(
                        call: Call<Post?>,
                        t: Throwable
                    ) {
                        callback.onError(t)
                    }
                })
        }
    }


    override fun shareById(id: Long) {
        //dao.shareById(id)
    }


}