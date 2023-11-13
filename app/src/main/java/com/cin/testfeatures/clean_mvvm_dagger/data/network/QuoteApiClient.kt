package com.cin.testfeatures.clean_mvvm_dagger.data.network

import com.cin.testfeatures.clean_mvvm_dagger.data.model.QuoteModel
import retrofit2.Response
import retrofit2.http.GET

interface QuoteApiClient {
    @GET (".json")
    suspend fun getAllQuotes(): Response<List<QuoteModel>>
}