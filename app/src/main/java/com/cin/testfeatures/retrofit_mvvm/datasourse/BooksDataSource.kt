package com.cin.testfeatures.retrofit_mvvm.datasourse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cin.testfeatures.retrofit_mvvm.model.VolumeResponse
import com.cin.testfeatures.retrofit_mvvm.service.BooksApi
import com.cin.testfeatures.retrofit_mvvm.service.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BooksDataSource(private val booksApi: BooksApi) {
    private val TAG = this.javaClass.simpleName
    val responseVolume = MutableLiveData<VolumeResponse>()
    val networkState = MutableLiveData<NetworkState>()

    //fun getBooks(query: String, author: String, keyApi: String) {
    fun getBooks(query: String, author: String) {
        networkState.value = NetworkState.LOADING
        //booksApi.getBooks(query, author, keyApi).enqueue(object : Callback<VolumeResponse> {
        booksApi.getBooks(query, author).enqueue(object : Callback<VolumeResponse> {
            override fun onResponse(
                call: Call<VolumeResponse>,
                response: Response<VolumeResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) responseVolume.value = response.body()
                    else networkState.postValue(NetworkState.ERROR)
                } else networkState.postValue(NetworkState.ERROR)
            }

            override fun onFailure(call: Call<VolumeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message?:"Error"}")
                networkState.value = NetworkState.ERROR
            }

        })
    }
}