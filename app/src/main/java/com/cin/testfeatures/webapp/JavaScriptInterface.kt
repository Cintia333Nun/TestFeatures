package com.cin.testfeatures.webapp

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import java.lang.ref.WeakReference

/***/
class JavaScriptInterface(context: Context) {
    private val weakContext = WeakReference(context)

    /**
     * Test
     * */
    @JavascriptInterface
    fun interfaceJavaScriptTestOne() {
        Toast.makeText(weakContext.get(), "interfaceJavaScriptTestOne", Toast.LENGTH_SHORT).show()
    }

    /**
     * Test
     * */
    @JavascriptInterface //Anotacion para acceder a metodo
    fun interfaceJavaScriptTestTwo():String {
        return "interfaceJavaScriptTestTwo"
    }
}