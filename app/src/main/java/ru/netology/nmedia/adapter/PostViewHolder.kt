package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.showAmount
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: PostCardBinding,
    private val likeListener: LikeListener,
    private val shareListener: ShareListener

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with (binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likecount.text = showAmount(post.likes)
            sharecount.text =showAmount(post.shareCount)
            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)

            like.setOnClickListener {
                  likeListener(post)          }

            share.setOnClickListener {
                shareListener(post)          }

        }
    }
}