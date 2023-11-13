package com.cin.testfeatures.update_manager

import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import retrofit2.Callback
import retrofit2.Response

class UpdateAppDataSource(private val api: UpdateAppApi) {

    /**
     * Agregar el version code y revisar el version code actual para comprobar
     * si es necesario actualizar la version
     * */
    /*fun checkForAppUpdates (
        applicationId: String, versionCode: String,
        target: File, listener: (Boolean) -> Unit
    ) {
        api.checkForUpdates(applicationId,versionCode).enqueue(object : Callback<ResponseAppUpdates>{
            override fun onResponse(
                call: Call<ResponseAppUpdates>,
                response: Response<ResponseAppUpdates>
            ) {
                response.body()?.let { appUpdateResponse ->
                    if (appUpdateResponse.status.uppercase().contains("OK")) {
                        try {
                            download(appUpdateResponse.url, target, listener)
                        } catch (e: Exception) {
                            listener.invoke(false)
                        }
                    } else {
                        listener.invoke(false)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseAppUpdates>, t: Throwable) {
                listener.invoke(false)
            }
        })
    }*/

    fun download(target: File, listener: (Boolean, File?) -> Unit) {
        api.downloadApk().enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.byteStream()?.use {
                    target.parentFile?.mkdirs()
                    FileOutputStream(target).use { targetOutputStream ->
                        it.copyTo(targetOutputStream)
                    }
                    listener.invoke(true, target)
                } ?: listener.invoke(false, null)//throw RuntimeException("failed to download: $url")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.invoke(false, null)
            }

        })
    }
}