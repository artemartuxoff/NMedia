package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread


private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl()

    private val _data = MutableLiveData(FeedModel())
    val data : LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(emptyPost)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated


    init {
        load()
    }

    fun load(){
        thread {
            _data.postValue((FeedModel(loading = true)))

            val state = try {
                val posts = repository.getAll()
                FeedModel(posts, empty = posts.isEmpty())


            }
            catch (e: Exception){
                FeedModel(error = true)
            }

            _data.postValue(state)
        }
    }

    fun likeById(id: Long, likedByMe: Boolean){
        thread {

            val postResponse: Post = repository.likeById(id, likedByMe)

            val posts = arrayListOf<Post>()

            _data.value?.posts?.forEach { post ->
                if (post.id == id) {
                    posts.add(post.copy(likes = postResponse.likes, likedByMe = postResponse.likedByMe))
                }
                else{
                    posts.add(post)
                }
            }

            _data.postValue(FeedModel(posts, empty = posts.isEmpty()))
        }
    }
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun save(content: String) {

        thread {
            edited.value?.let { post ->
                val trimmed = content.trim()
                if (trimmed != post.content) {
                    repository.save(post.copy(content = content))
                }

                _postCreated.postValue(Unit)

                edited.postValue(emptyPost)
            }
        }
    }

    fun cancelEdit(){
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }


}