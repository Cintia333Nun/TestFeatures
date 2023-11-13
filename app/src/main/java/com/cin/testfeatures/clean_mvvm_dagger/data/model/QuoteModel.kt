package com.cin.testfeatures.clean_mvvm_dagger.data.model

import com.google.gson.annotations.SerializedName

data class QuoteModel (
    @SerializedName("quote") val quote: String?,
    @SerializedName("author") val author: String?
)
