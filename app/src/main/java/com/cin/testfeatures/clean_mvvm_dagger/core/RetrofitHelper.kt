package com.cin.testfeatures.clean_mvvm_dagger.core

import com.cin.testfeatures.retrofit_mvvm.service.TypeBASEURL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    private var retrofit: Retrofit? = null
    private lateinit var baseURL: String

    fun getQuotesApi(): Retrofit {
        defineURL(TypeBASEURL.QUOTES)
        return getClient()!!
    }

    private fun getClient(): Retrofit? {
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

    private fun defineURL(type: TypeBASEURL) {
        baseURL = when(type) {
            TypeBASEURL.MASTER -> "https://www.googleapis.com/books/"
            TypeBASEURL.BOOKS -> "https://www.googleapis.com/books/"
            TypeBASEURL.TEST -> "https://www.googleapis.com/books/"
            TypeBASEURL.QUOTES -> "https://drawsomething-59328-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }
}