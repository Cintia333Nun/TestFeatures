package com.cin.testfeatures.recorder_audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class RecorderWork(var context: Context) {
    private val TAG = this.javaClass.simpleName
    private var mediaRecorder: MediaRecorder? = null
    var state: Boolean = false

    fun initMediaRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()

        mediaRecorder!!.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            val filePathRoot = File(context.filesDir.absolutePath,"RECORDING_MC")
            filePathRoot.mkdirs()
            val fileName = filePathRoot.absolutePath +
                    "/${SimpleDateFormat("ddMMyyyyhhmmss").format(Date())}.mp3"
            Log.i(TAG, "init -> fileName: $fileName")
            setOutputFile(fileName)
        }
    }

    fun startRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder!!.apply {
                    prepare()
                    start()
                }
                state = true
                Log.i(TAG, "startRecording" )
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                Log.e(TAG, "startRecording: ${e.message}" )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording() {
        if (mediaRecorder != null) {
            if(state) {
                mediaRecorder!!.apply {
                    stop()
                    release()
                }
                state = false
                Log.i(TAG, "stopRecording" )
            } else {
                Log.i(TAG, "you are not recording right now" )
            }
        }
    }

    fun checkPauseIsAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mediaRecorder != null) {
                if(state) {
                    mediaRecorder!!.pause()
                    Log.i(TAG, "pauseRecording" )
                }
            }
        }
    }

    fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(state) {
                if (mediaRecorder != null) mediaRecorder!!.resume()
                Log.i(TAG, "resumeRecording" )
            }
        }
    }

    fun removeRecords() {
        File(context.filesDir.absolutePath, "RECORDING_MC").deleteRecursively()
        Log.i(TAG, "destroyFiles" )
    }
}