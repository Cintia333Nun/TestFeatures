package com.cin.testfeatures.clean_mvvm_dagger.domain.model

import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.entities.QuoteEntity
import com.cin.testfeatures.clean_mvvm_dagger.data.model.QuoteModel

data class Quote (val quote:String, val author:String)

fun QuoteModel.toDomain() = Quote(this.quote ?: "", this.author ?: "")
fun QuoteEntity.toDomain() = Quote(this.quote, this.author)