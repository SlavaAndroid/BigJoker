package com.im30.ROE.g.ui.main.WebView

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.im30.ROE.g.data.repository.DataRepository
import com.im30.ROE.g.data.repository.DataRepositoryImpl
import com.im30.ROE.g.databinding.ActivityWebViewBinding
import com.im30.ROE.g.ui.game.JokerActivity
import com.im30.ROE.g.utils.Params


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView: WebView
    private lateinit var repository: DataRepository
    var messageAb: ValueCallback<Array<Uri?>>? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webView
        repository = DataRepositoryImpl()

        intent.getStringExtra("link")?.let { webView.loadUrl(it) }
        webView.webViewClient = Client()
        webView.settings.javaScriptEnabled = true

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = IMAGE
        startActivityForResult(
            Intent.createChooser(intent, IMAGE_TITLE),
            RESULT_CODE
        )
    }

    private inner class Client() : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {
                Toast.makeText(this@WebViewActivity, "Error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let {
                if (it == Params.BASE_LINK.substringAfter("https://").substringBefore("/")) {
                    startActivity(Intent(this@WebViewActivity, JokerActivity::class.java))
                } else {
                    if (repository.getCurrentOpenNumber() != 1 && !it.contains(Params.BASE_LINK)) {
                        repository.saveFullLink(it)
                    }
                    CookieManager.getInstance().flush()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val IMAGE_TITLE = "Image Chooser"
        private const val IMAGE = "image/*"

        private const val RESULT_CODE = 1
    }
}