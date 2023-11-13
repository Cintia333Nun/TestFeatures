package com.cin.testfeatures.speed_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.ActivitySpeedometerBinding
import com.example.internet_speed_testing.InternetSpeedBuilder
import com.example.internet_speed_testing.ProgressionModel
import okhttp3.*
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * SPEED TEST
 * **/
class SpeedTest3Activity : AppCompatActivity() {
    companion object {
        const val TAG = "SpeedTest3Activity"
        const val TIMER_WAIT = 6
        const val TIMER_SECOND = 1000L
    }
    private lateinit var binding: ActivitySpeedometerBinding
    private var testFileUrl: String = ""
    private val listDownloadSamples = mutableListOf<Int>()
    private val listUploadSamples = mutableListOf<Int>()
    private var countTimer = TIMER_WAIT
    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        loadData()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.purge()
        System.gc()
    }

    private fun loadData() {
        try {
            val config = Properties()
            val configInputStream = resources.openRawResource(R.raw.config_test_speed)
            config.load(configInputStream)

            testFileUrl = config.getProperty("test_file_url")

            if (!testFileUrl.matches("^https?://.*$".toRegex())) {
                throw Exception("La URL del archivo de prueba es inválida.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "URLs no cargadas", Toast.LENGTH_SHORT).show()
            binding.startSpeedometer.visibility = View.GONE
        }
    }

    private fun initBinding() {
        binding = ActivitySpeedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
        binding.startSpeedometer.setOnClickListener { initSpeedometer() }
    }

    @SuppressLint("SetTextI18n")
    private fun initSpeedometer() {
        changeViewLoadTestSpeed(true)
        binding.downloadSpeedDraw.speedTo(0f)
        binding.uploadSpeedDraw.speedTo(0f)
        binding.speedometer.speedTo(0f)

        if (testFileUrl.trim().isEmpty()) {
            changeViewLoadTestSpeed(false)
        } else startTestLib2()
    }

    private fun changeViewLoadTestSpeed(status: Boolean) {
        binding.startSpeedometer.visibility = if (status) View.GONE else View.VISIBLE
        binding.startSpeedometerLoad.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun startTestLib2() {
        listDownloadSamples.clear()
        listUploadSamples.clear()
        countTimer = TIMER_WAIT
        timer.cancel()
        timer = getTimer()
        listenerSpeedTest()
    }

    private fun listenerSpeedTest() {
        val builder = InternetSpeedBuilder(this@SpeedTest3Activity)
        builder.setOnEventInternetSpeedListener(object : InternetSpeedBuilder.OnEventInternetSpeedListener{
            override fun onDownloadProgress(count: Int, progressModel: ProgressionModel) {
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressDownload : ${progressModel.progressDownload}%")
                val div =  BigDecimal("1000000")
                val mbpsDownload = progressModel.downloadSpeed.divide(div).toFloat()
                binding.downloadSpeedDraw.speedTo(progressModel.progressDownload)
                binding.speedometer.speedTo(mbpsDownload)
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] downloadSpeed : $mbpsDownload Mbps")
            }

            override fun onTotalProgress(count: Int, progressModel: ProgressionModel) {
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressDownload : ${progressModel.progressDownload}%")
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressUpload : ${progressModel.progressUpload}%")
                val div =  BigDecimal("1000000")
                val mbpsDownload = progressModel.downloadSpeed.divide(div).toFloat()
                val mbpsUpload = progressModel.uploadSpeed.divide(div).toFloat()

                listDownloadSamples.add(mbpsDownload.toInt())
                if (mbpsUpload != 0F) listUploadSamples.add(mbpsUpload.toInt())

                countTimer = TIMER_WAIT

                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] downloadSpeed total: $mbpsDownload Mbps")
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] uploadSpeed total: $mbpsUpload Mbps")
            }

            override fun onUploadProgress(count: Int, progressModel: ProgressionModel) {
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressUpload : ${progressModel.progressUpload}%")
                val div =  BigDecimal("1000000")
                val mbpsUpload = progressModel.uploadSpeed.divide(div).toFloat()
                binding.uploadSpeedDraw.speedTo(progressModel.progressUpload)
                binding.speedometer.speedTo(mbpsUpload)
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] uploadSpeed : $mbpsUpload Mbps")
            }
        })
        builder.start("https://dev.mcnoc.mx/soft/sample-pdf-download-10-mb.pdf", 2)
    }

    @SuppressLint("SetTextI18n")
    private fun getTimer(): Timer {
        return fixedRateTimer("Timer Speed Test", false, 0L, TIMER_SECOND) {
            if (countTimer == 0) {
                println(":::SPEED TEST LIB2::: CONTADOR FINALIZADO")
                runOnUiThread {
                    binding.downloadSpeedDraw.setSpeedAt(0F)
                    binding.uploadSpeedDraw.setSpeedAt(0F)
                    binding.speedometer.setSpeedAt(0F)
                    binding.uploadSpeed.text =  "${getMod(listUploadSamples)} Mbps"
                    binding.downloadSpeed.text = "${getMod(listDownloadSamples)} Mbps"
                    listUploadSamples.clear()
                    listDownloadSamples.clear()
                    changeViewLoadTestSpeed(false)
                }
                cancel()
                System.gc()
            } else {
                countTimer -= 1
                println(":::SPEED TEST LIB2::: El contador es ahora $countTimer.")
            }
        }
    }

    private fun getMod(samples:  List<Int>): Int? {
        val numF = mutableMapOf<Int, Int>()
        for (sample in samples) {
            if (numF.containsKey(sample)) {
                numF[sample] = numF[sample]!! + 1
            } else numF[sample] = 1
        }
        return numF.maxByOrNull { it.value }?.key
    }

}