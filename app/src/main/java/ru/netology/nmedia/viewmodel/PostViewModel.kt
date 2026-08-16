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

//    fun load(){
//        thread {
//            _data.postValue((FeedModel(loading = true)))
//
//            val state = try {
//                val posts = repository.getAll()
//                FeedModel(posts, empty = posts.isEmpty())
//
//
//            }
//            catch (e: Exception){
//                FeedModel(error = true)
//            }
//
//            _data.postValue(state)
//        }
//    }

    fun load(){

        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })

    }

//    fun likeById(id: Long, likedByMe: Boolean){
//        thread {
//
//            val postResponse: Post = repository.likeById(id, likedByMe)
//
//            val posts = arrayListOf<Post>()
//
//            _data.value?.posts?.forEach { post ->
//                if (post.id == id) {
//                    posts.add(post.copy(likes = postResponse.likes, likedByMe = postResponse.likedByMe))
//                }
//                else{
//                    posts.add(post)
//                }
//            }
//
//            _data.postValue(FeedModel(posts, empty = posts.isEmpty()))
//        }
//    }


    fun likeById(id: Long, likedByMe: Boolean){


        repository.likeByIdAsync(id, likedByMe, object : PostRepository.LikeCallback {
            override fun onSuccess(post: Post) {

                val posts = arrayListOf<Post>()

                _data.value?.posts?.forEach { post_ ->
                    if (post_.id == id) {
                        posts.add(post_.copy(likes = post.likes, likedByMe = post.likedByMe))
                    }
                    else{
                        posts.add(post_)
                    }
                }
                _data.postValue(FeedModel(posts, empty = posts.isEmpty()))

            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
//    fun save(content: String) {
//
//        thread {
//            edited.value?.let { post ->
//                val trimmed = content.trim()
//                if (trimmed != post.content) {
//                    repository.save(post.copy(content = content))
//                }
//
//                _postCreated.postValue(Unit)
//
//                edited.postValue(emptyPost)
//            }
//        }
//    }

    fun save(content: String) {

        edited.value?.let { post ->
            val trimmed = content.trim()
            if (trimmed != post.content) {
                repository.saveAsync(post.copy(content = content), object : PostRepository.SaveCallback {
                    override fun onSuccess(post: Post) {
                        _postCreated.postValue(Unit)
                        edited.postValue(emptyPost)
                    }

                    override fun onError(e: Exception) {
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