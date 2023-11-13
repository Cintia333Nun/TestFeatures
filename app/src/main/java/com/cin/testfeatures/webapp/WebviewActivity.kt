package com.cin.testfeatures.webapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.retrofit_mvvm.view.getViewModel

/***/
class WebviewActivity : AppCompatActivity() {
    private val viewModel: WebViewViewModel by lazy { getViewModel { WebViewViewModel() } }
    private lateinit var customWebView: CustomWebView

    //region ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponentsCompose()
        configViewModelComponent()
        testViewModel()
    }

    private fun configViewModelComponent() {
        customWebView.loadSettingsWebView()
        customWebView.loadJavaScriptsListeners()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val canGoBack = customWebView.getWebView()?.canGoBack() ?: false
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack) {
            customWebView.getWebView()?.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onStop() {
        super.onStop()
        customWebView.getWebView()?.clearCache(false) // Limpia la memoria RAM en uso sin incluir los archivos del disco
    }

    override fun onDestroy() {
        super.onDestroy()
        customWebView.clearCookies()
    }
    //endregion

    //region METHODS ACTIVITY
    private fun initComponentsCompose() {
        setContent {
            customWebView.WebviewCompose(viewModel)
        }
    }
    //endregion

    //region TEST
    private fun testViewModel() {
        val url = "https://www.google.com/"
        viewModel.loadUrl(url)
        viewModel.setIsJavaScriptEnabled(true)
    }
    //endregion
}