package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {


    private var nextId = 1L
    private var posts = emptyList<Post>()

        set(value) {
            field = value
        sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILENAME_POSTS)
        if (file.exists()) {
            context.openFileInput(FILENAME_POSTS).bufferedReader().use { str ->
                posts = gson.fromJson(str, typeToken)

                nextId = posts.maxOf { it.id } + 1
                data.value = posts
            }
        }
    }

    private fun sync(){

        context.openFileOutput(FILENAME_POSTS, Context.MODE_PRIVATE).bufferedWriter().use{
        it.write(gson.toJson(posts))
        }
    }

    override fun getall(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {

        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            nextId = nextId + 1
            posts = listOf(
                post.copy(
                    id = nextId,
                    author = "Me",
                    likes = 0,
                    likedByMe = false,
                    shareCount = 0,
                    published = "now"
                )
            ) + posts

        }
        else {
            posts = posts.map{
                if (it.id == post.id)
                    it.copy(content = post.content)
                else it
            }
        }

        data.value = posts

    }

    companion object {
        private const val FILENAME_POSTS = "posts_test.json"
        private val gson = Gson()
        private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type


    }

}