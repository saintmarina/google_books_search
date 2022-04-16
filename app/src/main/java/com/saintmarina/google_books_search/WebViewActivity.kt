package com.saintmarina.google_books_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Webview is a little finicky. http:// links opens the external browser, while https:// opens the page in-app.
        val url = this.intent.getStringExtra("url")!!
            .replace(Regex("^http://"), "https://")

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        setContentView(webView)
        webView.loadUrl(url)

    }
}