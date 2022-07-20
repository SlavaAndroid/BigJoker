package com.android.games.bigjoker.ui.main.WebView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.games.bigjoker.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}