package com.cin.testfeatures.webapp

data class WebviewModel(
    var url: String,
    var userAgent: String,
    var interfaceListener: () -> Unit
)
