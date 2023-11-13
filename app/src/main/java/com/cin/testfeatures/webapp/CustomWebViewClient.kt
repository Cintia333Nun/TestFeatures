package com.cin.testfeatures.webapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.ref.WeakReference

class CustomWebViewClient(activity: Activity): WebViewClient() {
    private val weakActivity = WeakReference(activity)

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val notFoundPages = arrayListOf("www.example.com", "www.example2.com", "www.example3.com")
        val actualPage = request?.url
        return super.shouldOverrideUrlLoading(view, request)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val notFoundPages = arrayListOf("www.example.com", "www.example2.com", "www.example3.com")
        val actualPage = Uri.parse(url).host
        Log.i("TAG", "shouldOverrideUrlLoading: url = $actualPage")
        if (notFoundPages.contains(Uri.parse(url).host)) return false

        openUrlInNavigation(url)
        return true
    }

    private fun openUrlInNavigation(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        weakActivity.get()?.startActivity(intent)
    }
}