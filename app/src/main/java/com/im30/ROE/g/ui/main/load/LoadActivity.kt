package com.im30.ROE.g.ui.main.load

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.im30.ROE.g.R
import com.im30.ROE.g.data.repository.DataRepository
import com.im30.ROE.g.data.repository.DataRepositoryImpl
import com.im30.ROE.g.databinding.ActivityLoadingBinding
import com.im30.ROE.g.ui.game.JokerActivity
import com.im30.ROE.g.ui.main.WebView.WebViewActivity
import com.im30.ROE.g.utils.CheckBlock

class LoadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private lateinit var viewModel: LoadViewModel
    private lateinit var repository: DataRepository

    private val check = CheckBlock(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BigJoker)
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (check.isBlock()) {
            startJokerGame()
        } else {
            viewModel = ViewModelProvider(this)[LoadViewModel::class.java]
            repository = DataRepositoryImpl()

            when (repository.getCurrentOpenNumber()) {
                1 -> viewModel.resultLink.observe(this) { link ->
                    repository.setCurrentOpenNumber(2)
                    startWebView(link)
                }
                2 -> viewModel.resultLink.observe(this) { link ->
                    saveLink(link)
                    startWebView(link)
                }
                else -> getLink()?.let { startWebView(it) }
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
        val i = Intent(this, JokerActivity::class.java)
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