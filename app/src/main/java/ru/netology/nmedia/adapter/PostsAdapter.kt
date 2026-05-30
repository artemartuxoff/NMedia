package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post


typealias LikeListener = (Post) -> Unit
typealias ShareListener = (Post) -> Unit

class PostsAdapter(
    private val likeListener: LikeListener,
    private val shareListener: ShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return PostViewHolder(binding, likeListener, shareListener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }


}

object PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    )= oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ) = oldItem == newItem
}
