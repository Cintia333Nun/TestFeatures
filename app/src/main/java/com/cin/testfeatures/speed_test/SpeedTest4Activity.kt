package com.cin.testfeatures.speed_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.ActivitySpeedometerBinding
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import kotlinx.coroutines.*
import okhttp3.*
import java.math.BigDecimal
import java.util.*

/**
 * SPEED TEST
 * **/
class SpeedTest4Activity : AppCompatActivity() {
    companion object {
        const val TAG = "SpeedTest3Activity"
    }
    private lateinit var binding: ActivitySpeedometerBinding
    private val scope by lazy { CoroutineScope(SupervisorJob()) }
    private var testFileUrl: String = ""
    private val listDownloadSamples = mutableListOf<Int>()
    private val listUploadSamples = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        loadData()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
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
        listenerSpeedTest()
    }

    private fun listenerSpeedTest() {
        val speedTestSocketDownload =  SpeedTestSocket()
        val speedTestSocketUpload =  SpeedTestSocket()

        speedTestSocketDownload.addSpeedTestListener(object : ISpeedTestListener {
            @SuppressLint("SetTextI18n")
            override fun onCompletion(report: SpeedTestReport) {
                println(":::SPEED TEST LIB2::: CONTADOR FINALIZADO")
                scope.launch(Dispatchers.Main) {
                    binding.downloadSpeedDraw.setSpeedAt(0F)
                    binding.downloadSpeed.text = "${getMod(listDownloadSamples)} Mbps"
                    listDownloadSamples.clear()
                }
                /*speedTestSocketUpload.startUpload(
                    "falta servicio", 1000000
                )*/
            }

            override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                Log.w(TAG, ":::SPEED TEST LIB2::: ERROR: $errorMessage")
                scope.launch(Dispatchers.Main) {
                    changeViewLoadTestSpeed(false)
                    Toast.makeText(
                        this@SpeedTest4Activity,
                        "Ocurrio un error al valorar la descarga",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                val div =  BigDecimal("1000000")
                val mbpsDownload = report.transferRateBit.divide(div).toFloat()
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressDownload : ${report.progressPercent}%")

                scope.launch(Dispatchers.Main) {
                    binding.downloadSpeedDraw.speedTo(report.progressPercent)
                    binding.speedometer.speedTo(mbpsDownload)
                }

                listDownloadSamples.add(mbpsDownload.toInt())
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] downloadSpeed total: $mbpsDownload Mbps")
            }
        })

        speedTestSocketUpload.addSpeedTestListener(object : ISpeedTestListener {
            @SuppressLint("SetTextI18n")
            override fun onCompletion(report: SpeedTestReport) {
                println(":::SPEED TEST LIB2::: CONTADOR FINALIZADO")
                scope.launch(Dispatchers.Main) {
                    binding.uploadSpeedDraw.setSpeedAt(0F)
                    binding.speedometer.setSpeedAt(0F)
                    binding.uploadSpeed.text =  "${getMod(listUploadSamples)} Mbps"
                    listUploadSamples.clear()
                    changeViewLoadTestSpeed(false)
                }
                System.gc()
            }

            override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                Log.w(TAG, ":::SPEED TEST LIB2::: ERROR: $errorMessage")
                scope.launch(Dispatchers.Main) {
                    changeViewLoadTestSpeed(false)
                    Toast.makeText(
                        this@SpeedTest4Activity,
                        "Ocurrio un error al valorar la subida",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                val div =  BigDecimal("1000000")
                val mbpsUpload = report.transferRateBit.divide(div).toFloat()
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] progressUpload : ${report.progressPercent}%")

                scope.launch(Dispatchers.Main) {
                    binding.uploadSpeedDraw.speedTo(report.progressPercent)
                    binding.speedometer.speedTo(mbpsUpload)
                }

                if (mbpsUpload != 0F) listUploadSamples.add(mbpsUpload.toInt())
                Log.v(TAG, ":::SPEED TEST LIB2::: [PROGRESS] uploadSpeed: $mbpsUpload Mbps")
            }
        })

        scope.launch(Dispatchers.IO) {
            speedTestSocketDownload.startDownload(
                "https://dev.mcnoc.mx/soft/sample-pdf-download-10-mb.pdf"
            )
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