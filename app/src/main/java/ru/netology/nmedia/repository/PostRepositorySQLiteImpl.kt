package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class PostRepositorySQLiteImpl : PostRepository {

    private companion object{
        const val BASE_URL = "http://10.0.2.2:9999"
        val jsonType = "application/json".toMediaType()
        val gson = Gson()
        val postsType: Type = object: TypeToken<List<Post>>(){}.type
    }

    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()


    override fun getAll(): List<Post> {
        val call = client.newCall(
            Request.Builder()
                .url("$BASE_URL/api/slow/posts")
                .build()
        )

        val responce = call.execute()
        val responceText = responce.body.string()

        return gson.fromJson<List<Post>>(responceText, postsType)
    }

    override fun likeById(id: Long, likedByMe: Boolean):Post {

        if (likedByMe) {

            val request: Request = Request.Builder()
                .delete(RequestBody.EMPTY)
                .url("$BASE_URL/api/posts/$id/likes")
                .build()

            val call = client.newCall(request)

            val responce = call.execute()
            val responceText = responce.body.string()

            return gson.fromJson(responceText, Post::class.java)
        }
        else {
            val request: Request = Request.Builder()
                .post(RequestBody.EMPTY)
                .url("$BASE_URL/api/posts/$id/likes")
                .build()

            val call = client.newCall(request)

            val responce = call.execute()
            val responceText = responce.body.string()

            return gson.fromJson(responceText, Post::class.java)
        }




    }

    override fun shareById(id: Long) {
        //dao.shareById(id)
    }

    override fun removeById(id: Long) {

        val call = client.newCall(
            Request.Builder()
                .url("$BASE_URL/api/slow/posts/$id")
                .delete()
                .build()
        )

        call.execute()


    }

    override fun save(post: Post):Post {

        val call = client.newCall(
            Request.Builder()
                .url("$BASE_URL/api/slow/posts")
                .post(gson.toJson(post).toRequestBody(jsonType))
                .build()
        )

        val responce = call.execute()
        val responceText = responce.body.string()

        return gson.fromJson(responceText, Post::class.java)

    }
}