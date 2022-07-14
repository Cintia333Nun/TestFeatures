package com.cin.testfeatures.recorder_audio

import android.Manifest.permission.RECORD_AUDIO
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cin.testfeatures.databinding.ActivityAudioRecordTestBinding
import java.io.File
import java.io.IOException

class AudioRecordTestActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityAudioRecordTestBinding
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var mStartRecording = true
    private var player: MediaPlayer? = null
    private var mStartPlaying = true
    private var permissions: Array<String> = arrayOf(RECORD_AUDIO)
    private var permissionToRecordAccepted = false

    /*private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) ActivityCompat.requestPermissions(this, permissions,0)
        //else initListeners()
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else false
        //if (!permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        recorder = (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else MediaRecorder()
        ).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) //MICRO
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) //
            setOutputFile(fileName) //PATH
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) //

            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val filePathRoot = File(filesDir.absolutePath,"RECORDING_TEST")
        filePathRoot.mkdirs()
        fileName = filePathRoot.absolutePath + "/recording.mp3"
        Log.i(TAG, "onCreate -> fileName: $fileName")
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        checkPermissionRecorder()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        File(filesDir.absolutePath,"RECORDING_TEST").deleteRecursively()
    }

    private fun checkPermissionRecorder(): Boolean {
        return if (
            ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, RECORD_AUDIO)) {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Es necesario grabar audio");
                alertBuilder.setMessage("EXPLICACION");
                alertBuilder.setPositiveButton(
                    "ACEPTAR"
                ) { _, _ ->
                    //requestPermissionLauncher.launch(RECORD_AUDIO)
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                }
                alertBuilder.create().show()
                false
            } else {
                Toast.makeText(this,"AudioPermissionActivity show",Toast.LENGTH_LONG).show()
                //START -> AudioPermissionActivity
                false
            }
        } else true
    }

    private fun initListeners() {
        with(binding) {
            buttonRecord.setOnClickListener {
                if (checkPermissionRecorder()) {
                    onRecord(mStartRecording)
                    buttonRecord.text = when (mStartRecording) {
                        true -> "PAUSAR"
                        false -> "GRABAR"
                    }
                    mStartRecording = !mStartRecording
                }
            }

            buttonPlay.setOnClickListener {
                if (checkPermissionRecorder()) {
                    onPlay(mStartPlaying)
                    buttonPlay.text = when (mStartPlaying) {
                        true -> "PAUSAR"
                        false -> "REPRODUCIR"
                    }
                    mStartPlaying = !mStartPlaying
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
}