package ru.netology.nmedia.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.showAmount
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: PostCardBinding,
    private val listener: PostListener

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            share.text = showAmount(post.shareCount)
            like.isChecked = post.likedByMe
            like.text = showAmount(post.likes)


            if (post.videoUrl =="") {
                binding.picVideo.visibility = View.GONE
                binding.picVideoOk.visibility = View.GONE
            }
            else{
                binding.picVideo.visibility = View.VISIBLE
                binding.picVideoOk.visibility = View.VISIBLE
            }

            like.setOnClickListener {
                listener.onLike(post)
            }

            share.setOnClickListener {
                listener.onShare(post)
            }

            picVideoOk.setOnClickListener {
                listener.onClickVideo(post)
            }

            binding.menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                listener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            binding.content.setOnClickListener {
                listener.onClickContent(post)
            }
        }
    }
}