package com.cin.testfeatures.update_manager

import com.google.gson.annotations.SerializedName

data class ResponseAppUpdates (
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("url") val url: String,
)