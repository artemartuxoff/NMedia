package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

import kotlin.getValue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_main)
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.setPadding(systemBars.left + v.paddingLeft, systemBars.top + v.paddingTop, systemBars.right + v.paddingRight, systemBars.bottom + v.paddingBottom)
        //     insets
        // }
        //findViewById<ImageButton>(R.id.like).setOnClickListener {
        //     (it as ImageButton).setImageResource(R.drawable.ic_liked_24)
        // }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

       val viewModel by viewModels<PostViewModel>()

       viewModel.data.observe(this){post->
           with(binding) {
               author.text = post.author
               published.text = post.published
               content.text = post.content
               likecount.text = showAmount(post.likes)
               sharecount.text = showAmount(post.shareCount)
               like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)

           }
       }

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
           viewModel.share()
        }
    }

    fun showAmount(amount: Int): String {
        return when {
            (amount >= 1100000) -> ((amount / 1000000.0 * 10).toInt() / 10.0).toString() + "M"
            (amount >= 1000000) -> (amount / 1000000).toString() + "M"
            (amount >= 10000) -> (amount / 1000).toString() + "K"
            (amount >= 1100) -> ((amount / 1000.0 * 10).toInt() / 10.0).toString() + "K"
            (amount >= 1000) -> (amount / 1000).toString() + "K"
            else -> amount.toString()
        }
    }
}