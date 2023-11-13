package com.cin.testfeatures.webapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * */
class WebViewViewModel : ViewModel() {
    /**
     *
     * */
    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    /**
     *
     * */
    fun loadUrl(newUrl: String) = _url.postValue(newUrl)

    /**
     *
     * */
    private val _isJavaScriptEnabled = MutableLiveData<Boolean>()
    val isJavaScriptEnabled: LiveData<Boolean> = _isJavaScriptEnabled

    /**
     *
     * */
    fun setIsJavaScriptEnabled(status: Boolean) = _isJavaScriptEnabled.postValue(status)

    /**
     *
     * */
    private val _userAgent = MutableLiveData<String>()
    val userAgent: LiveData<String> = _userAgent

    /**
     *
     * */
    fun setUserAgent(user: String) = _userAgent.postValue(user)
}