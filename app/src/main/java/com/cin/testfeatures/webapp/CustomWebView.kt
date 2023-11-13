package com.cin.testfeatures.webapp

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import android.webkit.WebStorage
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.lang.ref.WeakReference

class CustomWebView(activity: Activity) {
    private val weakActivity = WeakReference(activity)
    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun WebviewCompose(viewModel: WebViewViewModel) {
        // Buscar como usar inyecccion de dependencias para inyectar el ViewModel
        // Variables de estado:
        // Al crear componentes se pueden ligar directamente a la vista y cambiar directamente con la interaccion
        val url by viewModel.url.observeAsState("")
        val isJavaScriptEnabled by viewModel.isJavaScriptEnabled.observeAsState(true)
        val userAgent by viewModel.userAgent.observeAsState()

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    viewModel.setUserAgent(settings.userAgentString) // En caso de no definirse en un futuro se asigna un usaria por defecto
                    settings.javaScriptEnabled = isJavaScriptEnabled // Habilitar JavaScript si es necesario, podrás crear interfaces entre el código de la app y el de JavaScript.
                    settings.userAgentString = userAgent // Asignar un usuario de identificacion para personalizar o seguradad
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.apply {
                    settings.javaScriptEnabled = isJavaScriptEnabled // Para actualizar la vista en un cambio de LiveData
                    settings.userAgentString = userAgent //
                    loadUrl(url)
                }
            }
        )
    }

    fun getWebView(): WebView? {
        return if (webView == null) null
        else webView
    }

    fun loadSettingsWebView() {
        webView.let {
            it?.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                weakActivity.get()?.let { activity ->
                    webViewClient = CustomWebViewClient(activity)
                }

                //Caracteristicas que aun no se en que usar:
                clearFormData() // Elimina los datos de un formulario.
                clearHistory() // Limpia el historial del navegador.
                clearMatches() //
            }
        }
    }

    fun clearCookies() {
        webView?.clearCache(true)
        webView?.clearFormData()
        webView?.clearHistory()
        webView?.clearSslPreferences()
        webView?.clearMatches()
        WebStorage.getInstance().deleteAllData()
    }

    fun loadJavaScriptsListeners() {
        weakActivity.get()?.let {  activity ->
            val jsInterface = JavaScriptInterface(activity)
            webView?.addJavascriptInterface(jsInterface, "AndroidInterface")
        }
    }
}