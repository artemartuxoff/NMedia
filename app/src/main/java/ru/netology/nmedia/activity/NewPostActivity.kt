package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostActivity.Companion.KEY_POST_TEXT
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postText = intent.getStringExtra("KEY_POST_TEXT_EDIT")

        if (postText.isNullOrBlank()) {
            binding.edit.setText("")
        } else {
            binding.edit.setText(postText)
        }


        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isNullOrBlank()) {
                setResult(RESULT_CANCELED)

            } else {
                setResult(RESULT_OK, Intent().apply { putExtra(KEY_POST_TEXT, text) })
            }
            finish()
        }


    }

    //описание константы
    companion object {
        val KEY_POST_TEXT = "post text"
    }
}

object NewPostContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(
        context: Context,
        input: Unit
    ) = Intent(context, NewPostActivity::class.java)

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) = intent?.getStringExtra(NewPostActivity.KEY_POST_TEXT)

}

class EditNewPostContract() : ActivityResultContract<String, String?>() {
    override fun createIntent(
        context: Context,
        input: String
    ) = Intent(context, NewPostActivity::class.java).apply {
        putExtra("KEY_POST_TEXT_EDIT", input)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) = intent?.getStringExtra(NewPostActivity.KEY_POST_TEXT)
}
