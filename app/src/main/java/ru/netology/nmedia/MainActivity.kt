package ru.netology.nmedia

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

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

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "21 мая в 18:36",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            100,
            false,
            999
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likecount.text = showAmount(post.likes)
            sharecount.text = showAmount(post.shareCount)
            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
            like.setOnClickListener {
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)
                likecount.text = showAmount(post.likes)
            }

            share.setOnClickListener {
                post.shareCount++
                sharecount.text = showAmount(post.shareCount)
            }

            root.setOnClickListener {
                println("root")
            }

            avatar.setOnClickListener {
                println("avatar")
            }
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