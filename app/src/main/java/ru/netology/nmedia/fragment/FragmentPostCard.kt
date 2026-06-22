package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.showAmount
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentPostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.util.StringArg.getValue
import ru.netology.nmedia.util.StringArg.setValue
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.getValue

class FragmentPostCard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostCardBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val postId = arguments?.idArg ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id.toString() == postId } ?: return@observe
            with(binding) {

                author.text = post.author
                published.text = post.published
                content.text = post.content
                share.text = showAmount(post.shareCount)
                like.isChecked = post.likedByMe
                like.text = showAmount(post.likes)


                if (post.videoUrl == "") {
                    binding.picVideo.visibility = View.GONE
                    binding.picVideoOk.visibility = View.GONE
                } else {
                    binding.picVideo.visibility = View.VISIBLE
                    binding.picVideoOk.visibility = View.VISIBLE
                }

                like.setOnClickListener {
                    viewModel.likeById(post.id)
                }

                share.setOnClickListener {
                    viewModel.shareById(post.id)

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plaint"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }

                    val chooser =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(chooser)
                }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    val bundle = bundleOf("textArg" to post.content)
                                    findNavController().navigate(
                                        R.id.action_fragmentPostCard_to_newPostFragment,
                                        bundle
                                    )
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }

            }
        }
        return binding.root
    }

    //описание константы
    companion object {
        var Bundle.idArg by StringArg
    }
}