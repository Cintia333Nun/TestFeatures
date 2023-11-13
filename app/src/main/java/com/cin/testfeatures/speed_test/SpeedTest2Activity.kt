package com.cin.testfeatures.speed_test

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.R
import com.cin.testfeatures.databinding.ActivitySpeedometerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.Buffer
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class SpeedTest2Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySpeedometerBinding
    private val scope by lazy { CoroutineScope(SupervisorJob()) }
    private var testFileUrl: String = ""
    private var uploadScriptUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        loadData()
        initListeners()
    }

    private fun loadData() {
        try {
            val config = Properties()
            val configInputStream = resources.openRawResource(R.raw.config_test_speed)
            config.load(configInputStream)

            testFileUrl = config.getProperty("test_file_url")
            uploadScriptUrl = config.getProperty("upload_script_url")

            if (!testFileUrl.matches("^https?://.*$".toRegex())) {
                throw Exception("La URL del archivo de prueba es inválida.")
            }

            if (!uploadScriptUrl.matches("^https?://.*$".toRegex())) {
                throw Exception("La URL del script de carga es inválida.")
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
        /*
        if (testFileUrl.trim().isEmpty() && uploadScriptUrl.trim().isEmpty()) {
            changeViewLoadTestSpeed(false, "URLs no cargadas")
            binding.startSpeedometer.visibility = View.GONE
        } else startTest()*/
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
             val nc = cm.getNetworkCapabilities(cm.activeNetwork)
             nc?.let {
                 val downSpeed = (nc.linkDownstreamBandwidthKbps)/1000
                 val upSpeed = (nc.linkUpstreamBandwidthKbps)/1000
                 val midSpeed = (downSpeed + upSpeed) / 2
                 binding.uploadSpeed.text = String.format("%.2f Mbps", upSpeed.toFloat())
                 binding.downloadSpeed.text = String.format("%.2f Mbps", downSpeed.toFloat())
                 binding.speedometer.speedTo(midSpeed.toFloat())
                 changeViewLoadTestSpeed(false, "ok")
             }
        } else {
            println("VERSION.SDK_INT < M")
        }

    }

    private fun changeViewLoadTestSpeed(status: Boolean, message: String = "") {
        binding.startSpeedometer.visibility = if (status) View.GONE else View.VISIBLE
        binding.startSpeedometerLoad.visibility = if (status) View.VISIBLE else View.GONE

        if (!status && message.lowercase() != "ok") {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            binding.downloadSpeedDraw.speedTo(0f)
            binding.uploadSpeedDraw.speedTo(0f)
            binding.speedometer.speedTo(0f)

        }
    }

    private fun startTest() {
        scope.launch(Dispatchers.IO) {
            try {
                val client = getClientSecureTest()
                val downloadSpeedMbps = downloadSpeedTest(client, testFileUrl, 1000000)
                val uploadSpeedMbps = uploadSpeedTest(client, uploadScriptUrl)
                val averageSpeedMbps = (downloadSpeedMbps + uploadSpeedMbps) / 2
                println("Download speed: $downloadSpeedMbps Mbps")
                println("Upload speed: $uploadSpeedMbps Mbps")
                println("Average speed: $averageSpeedMbps Mbps")

                scope.launch(Dispatchers.Main) {
                    binding.uploadSpeed.text = String.format("%.2f Mbps", uploadSpeedMbps)
                    binding.downloadSpeed.text = String.format("%.2f Mbps", downloadSpeedMbps)
                    binding.speedometer.speedTo(averageSpeedMbps.toFloat())
                    changeViewLoadTestSpeed(false, "ok")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getClientSecureTest(): OkHttpClient {
        // Obtener un objeto SSLContext
        val sslContext = SSLContext.getInstance("TLS")

        // Crear un objeto TrustManager personalizado
        val trustManager = @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // No se requiere ninguna acción adicional para este ejemplo
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Validar los certificados presentados por el servidor
                // en función de los criterios específicos del proyecto
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf() // Devolver un array vacío para este ejemplo
            }
        }

        // Configurar el objeto SSLContext con el TrustManager personalizado
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        // Obtener un objeto SSLSocketFactory a partir del SSLContext configurado
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory, trustManager)
            .build()
    }

    @Throws(IOException::class)
    fun downloadSpeedTest(client: OkHttpClient, url: String, fileSize: Long): Double {
        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), ByteArray(fileSize.toInt()))

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        val call = client.newCall(request)

        var startTime = 0L
        var endTime = 0L
        var fileSize = 0L

        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val body = response.body
                if (body != null) {
                    startTime = System.nanoTime()
                    val source = body.source()
                    val dir = File(filesDir, "SpeedTest")
                    val file = File(dir, "1MB.zip")
                    val sink = file.sink().buffer()
                    var readBytes: Long
                    val buffer = Buffer()

                    if (!dir.exists()) dir.mkdirs()

                    while (source.read(buffer, 8192).also { readBytes = it } != -1L) {
                        sink.write(buffer, readBytes)
                        fileSize += readBytes
                        buffer.clear()
                    }
                    sink.flush()
                    sink.close()
                    endTime = System.nanoTime()
                    body.close()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val elapsedTime = endTime - startTime
        val uploadSpeedMbps = (fileSize * 8) / (elapsedTime / 1000.0) / 1_000_000.0
        val fileSizeMB = fileSize / 1_000_000.0
        Log.d("SpeedTest2Activity", "Upload speed: $uploadSpeedMbps Mbps")
        Log.d("SpeedTest2Activity", "File size: $fileSizeMB MB")
        return  uploadSpeedMbps
    }

    @Throws(IOException::class)
    fun uploadSpeedTest(client: OkHttpClient, url: String): Double {
        val dir = File(filesDir, "SpeedTest")
        val file = File(dir, "1MB.zip")
        val fileRequestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, fileRequestBody)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val startNs = System.nanoTime()

        val response = client.newCall(request).execute()

        val endNs = System.nanoTime()

        val responseCode = response.code

        if (!response.isSuccessful) {
            throw RuntimeException("Unexpected response code: $responseCode")
        }

        val responseSize = response.body?.contentLength() ?: 0L
        val elapsedTimeMs = (endNs - startNs) / 1e6
        val transferRateBit = responseSize * 8 / (elapsedTimeMs / 1000.0)

        return transferRateBit / 1_000_000
    }
}