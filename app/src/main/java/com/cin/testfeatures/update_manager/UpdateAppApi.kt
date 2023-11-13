package com.cin.testfeatures.update_manager

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UpdateAppApi {
    @FormUrlEncoded
    @POST("")
    fun checkForUpdates(
        @Field("application_id") applicationId: String,
        @Field("version_code") versionCode: String,
    ): Call<ResponseAppUpdates>

    @GET("test_apk.apk")
    @Streaming
    fun downloadApk(): Call<ResponseBody>
}