package com.cin.testfeatures.retrofit_mvvm.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceApi {
    private var retrofit: Retrofit? = null
    private lateinit var baseURL: String

    fun getBooksApi(): BooksApi {
        defineURL(TypeBASEURL.BOOKS)
        return getClient()!!.create(
            BooksApi::class.java
        )
    }

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    fun defineURL(type: TypeBASEURL) {
        baseURL = when(type) {
            TypeBASEURL.MASTER -> "https://www.googleapis.com/books/"
            TypeBASEURL.BOOKS -> "https://www.googleapis.com/books/"
            TypeBASEURL.TEST -> "https://www.googleapis.com/books/"
            TypeBASEURL.QUOTES -> "https://www.googleapis.com/books/"
        }
    }
}