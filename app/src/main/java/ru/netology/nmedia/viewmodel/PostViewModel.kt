package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.util.SingleLiveEvent


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

        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })

    }

    fun likeById(id: Long, likedByMe: Boolean){
        repository.likeByIdAsync(id, likedByMe, object : PostRepository.Callback<Post?> {
            override fun onSuccess(post: Post?) {

                val posts = arrayListOf<Post>()

                _data.value?.posts?.forEach { post_ ->
                    if (post_.id == id ) {
                        posts.add(post_.copy(likes = post?.likes ?: post_.likes, likedByMe = post?.likedByMe ?: post_.likedByMe))
                    }
                    else{
                        posts.add(post_)
                    }
                }
                _data.postValue(FeedModel(posts, empty = posts.isEmpty()))

            }

            override fun onError(e: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save(content: String) {

        edited.value?.let { post ->
            val trimmed = content.trim()
            if (trimmed != post.content) {
                repository.saveAsync(post.copy(content = content), object : PostRepository.Callback<Post?> {
                    override fun onSuccess(post: Post?) {
                        _postCreated.postValue(Unit)
                        edited.postValue(emptyPost)
                    }

                    override fun onError(e: Throwable) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
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