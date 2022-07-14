package com.cin.testfeatures.recorder_audio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cin.testfeatures.databinding.ActivityRecorderBinding

class RecorderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecorderBinding
    private var recorderWork: RecorderWork? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecorderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configInstructions()
        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        initRecorderWork()
    }

    private fun initRecorderWork() {
        if (checkPermissionsRecorder()) {
            binding.permissionRecordUi.interfacePermissions.visibility = View.GONE
            if (recorderWork == null) {
                recorderWork = RecorderWork(this)
                recorderWork!!.initMediaRecorder()
                recorderWork!!.startRecording()
            }
            checkIsMicAvailable()
        } else {
            binding.permissionRecordUi.interfacePermissions.visibility = View.VISIBLE
            checkPermissions()
            recorderWork = null
        }
    }

    override fun onPause() {
        super.onPause()
        recorderWork?.let { record ->
            if (record.checkPauseIsAvailable()) record.pauseRecording()
        }
    }

    override fun onResume() {
        super.onResume()
        recorderWork?.let { record ->
            if (record.checkPauseIsAvailable()) record.resumeRecording()
        }
    }

    override fun onDestroy() {
        if (recorderWork != null) {
            recorderWork!!.stopRecording()
            //recorderWork!!.removeRecords()
        }
        super.onDestroy()
    }

    private fun configInstructions() {
        with(binding.permissionRecordUi) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                pasoDosDescription.visibility = View.GONE
                pasoDosTxt.visibility = View.GONE
                pasoDosImage.visibility = View.GONE
                pasoTresTxt.text = "2"
                pasoCuatroTxt.text = "3"
                pasoCincoTxt.text = "4"
            }
        }
    }

    private fun checkPermissions() {
        if (!checkPermissionsRecorder()) {
            if (
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.RECORD_AUDIO
                )
            ) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            binding.permissionRecordUi.buttonStart.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }

    private fun checkPermissionsRecorder(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkIsMicAvailable() {
        recorderWork?.let { record ->
            if (record.state) {
                binding.mic.visibility = View.VISIBLE
                binding.micOff.visibility = View.GONE
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO), 0
            )
        } else initRecorderWork()
    }
}