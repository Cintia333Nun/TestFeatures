package com.cin.testfeatures.speed_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.databinding.ActivitySpeedometerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import java.net.URLConnection

class SpeedometerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpeedometerBinding
    private val scope by lazy { CoroutineScope(SupervisorJob()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initSpeedometer()
    }

    private fun initBinding() {
        binding = ActivitySpeedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initSpeedometer() {
        binding.speedometer.speedTo(0f)
        scope.launch(Dispatchers.IO) {
            val downloadSpeed = downloadSpeed()
            val uploadSpeed = uploadSpeed()
            val speed = (downloadSpeed + uploadSpeed) / 2
            scope.launch(Dispatchers.Main) {
                binding.uploadSpeed.text = String.format("%.2f Mbps", uploadSpeed.toFloat())
                binding.downloadSpeed.text = String.format("%.2f Mbps", downloadSpeed.toFloat())
                binding.speedometer.speedTo(speed.toFloat())
            }
        }
    }


    private fun downloadSpeed(): Double {
        var speed = 0.0
        try {
            val url = URL("http://ipv4.download.thinkbroadband.com/1MB.zip")
            val connection: URLConnection = url.openConnection()
            connection.connect()
            val inputStream = BufferedInputStream(url.openStream())
            val startTime = System.currentTimeMillis()
            var downloaded = 0
            var bytesIn = 0
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer).also { bytesIn = it } != -1) {
                downloaded += bytesIn
                val endTime = System.currentTimeMillis()
                val totalTime = (endTime - startTime) / 1000.0
                speed = (downloaded / totalTime) / 1024.0
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return speed
    }

    private fun uploadSpeed(): Double {
        var speed = 0.0
        try {
            val url = URL("http://ipv4.upload.thinkbroadband.com:8080/upload.php")
            val connection: URLConnection = url.openConnection()
            connection.doOutput = true
            val startTime = System.currentTimeMillis()
            val outputStream = connection.getOutputStream()
            val buffer = ByteArray(1024)
            var bytesIn = 0
            var uploaded = 0
            while (uploaded < 1024 * 1024) {
                outputStream.write(buffer)
                bytesIn = buffer.size
                uploaded += bytesIn
                val endTime = System.currentTimeMillis()
                val totalTime = (endTime - startTime) / 1000.0
                speed = (uploaded / totalTime) / 1024.0
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return speed
    }
}
