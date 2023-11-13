package com.cin.testfeatures.retrofit_mvvm.service

import com.cin.testfeatures.retrofit_mvvm.model.VolumeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("/books/v1/volumes")
    fun getBooks(
        @Query("q") query: String,
        @Query("inauthor") author: String,
        //@Query("key") apiKey: String
    ): Call<VolumeResponse>
}