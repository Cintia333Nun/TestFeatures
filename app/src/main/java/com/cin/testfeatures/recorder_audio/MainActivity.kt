package com.cin.testfeatures.recorder_audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cin.testfeatures.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityMainBinding
    //  MEDIA RECORDER
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObjects()
        initListeners()
    }

    /*override fun onPause() {
        super.onPause()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) pauseRecording()
        else stopRecording()
    }

    override fun onResume() {
        super.onResume()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) resumeRecording()
        else startRecording()
    }*/

    private fun initObjects() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf (
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions,0)
        } else initRecorderObjects()
    }

    private fun initListeners() {
        with(binding) {
            buttonStartRecording.setOnClickListener { checkPermissionsRecorder() }

            buttonStopRecording.setOnClickListener { stopRecording() }

            if (checkIsPauseAvailable()) {
                buttonPauseRecording.setOnClickListener { pauseRecording() }
            }
        }
    }

    // region RECORDER
    @Suppress("DEPRECATION")
    private fun initRecorderObjects() {
        //output = filesDir.absolutePath + "/recording.mp3"
        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else MediaRecorder()

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(output)
        }
    }

    private fun checkPermissionsRecorder() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf (
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions,0)
        } else startRecording()
    }

    private fun startRecording() {
        try {
            mediaRecorder?.apply {
                prepare()
                start()
            }
            state = true
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Log.e(TAG, "startRecording: ", )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if(state) {
            mediaRecorder?.apply {
                stop()
                release()
            }
            state = false
        } else {
            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIsPauseAvailable(): Boolean {
        val isAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        if (isAvailable) {
            binding.buttonPauseRecording.visibility = View.VISIBLE
        } else {
            binding.buttonPauseRecording.visibility = View.GONE
        }
        return isAvailable
    }

    @SuppressLint("SetTextI18n")
    private fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(state) {
                with(binding) {
                    if(!recordingStopped) {
                        Toast.makeText(this@MainActivity,"Stopped!", Toast.LENGTH_SHORT).show()
                        mediaRecorder?.pause()
                        recordingStopped = true
                        buttonPauseRecording.text = "CONTINUAR"
                    } else resumeRecording()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Toast.makeText(this@MainActivity,"Resume!", Toast.LENGTH_SHORT).show()
            mediaRecorder?.resume()
            binding.buttonPauseRecording.text = "PAUSA"
            recordingStopped = false
        }
    }
    //endregion
}