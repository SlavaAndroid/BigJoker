package com.android.games.bigjoker.ui.main.load

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.games.bigjoker.data.repository.DataRepository
import com.android.games.bigjoker.data.repository.DataRepositoryImpl
import com.android.games.bigjoker.databinding.ActivityLoadBinding
import com.android.games.bigjoker.ui.game.JokerGameActivity
import com.android.games.bigjoker.ui.main.WebView.WebViewActivity
import com.android.games.bigjoker.utils.CheckBlock

class LoadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadBinding
    private lateinit var viewModel: LoadViewModel
    private lateinit var repository: DataRepository

    private val check = CheckBlock(this)
    private var firstLink: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!check.isBlock()) {
            startJokerGame()
        } else {
            viewModel = ViewModelProvider(this)[LoadViewModel::class.java]
            repository = DataRepositoryImpl()

            when (firstLink) {
                1 -> viewModel.resultLink.observe(this) { link ->
                    firstLink = 2
                    startWebView(link)
                }
                2 -> viewModel.resultLink.observe(this) { link ->
                    firstLink = 3
                    saveLink(link)
                    startWebView(link)
                }
                3 -> getLink()?.let { startWebView(it) }
            }
        }
    }

    private fun startWebView(link: String) {
        val i = Intent(this, WebViewActivity::class.java)
        i.putExtra("link", link)
        startActivity(i)
        finish()
    }

    private fun startJokerGame() {
        val i = Intent(this, JokerGameActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun saveLink(link: String) {
        repository.saveFullLink(link)
    }

    private fun getLink(): String? {
        return repository.getFullLink()
    }
}