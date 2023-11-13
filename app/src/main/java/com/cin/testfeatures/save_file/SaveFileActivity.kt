package com.cin.testfeatures.save_file

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cin.testfeatures.databinding.ActivitySaveFileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SaveFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveFileBinding
    private val resultPickFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        this::saveFileInInternalStorage
    )
    private val scope by lazy { CoroutineScope(SupervisorJob()) }

    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        file?.delete()
    }

    private fun initBinding() {
        binding = ActivitySaveFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListeners() {
       with(binding) {
           buttonPickFile.setOnClickListener { intentSaveFile() }
           buttonShowFile.setOnClickListener { intentShowFile() }
       }
    }

    private fun intentSaveFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        resultPickFile.launch(intent)
    }

    private fun intentShowFile() {
        if (file != null) {
            val uri = FileProvider.getUriForFile(
                this, "${packageName}.provider", file!!
            )

            uri.path?.let {
                getIntentDataAndType(it)?.let { dataAndType ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, dataAndType)
                    intent.putExtra(Intent.EXTRA_TITLE, title)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                    else Toast.makeText(
                        this, "Ninguna aplicación disponible para abrir el archivo", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getIntentDataAndType(filePath: String): String? {
        if (filePath.isEmpty()) return null
        var ext = ""
        val i = filePath.lastIndexOf('.')
        if (i > 0) {
            ext = filePath.substring(i + 1)
        }
        var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        mimeType = mimeType ?: "*/*"
        return mimeType
    }

    private fun saveFileInInternalStorage(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uriFile ->
                uriFile.lastPathSegment?.let {
                    getFileFromUri(uriFile, it.split("/").last())
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri, fileName: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val file = File(filesDir, fileName)
                this@SaveFileActivity.file = file
                scope.launch(Dispatchers.Main) {
                    binding.buttonPickFile.text = fileName
                }

                contentResolver.openInputStream(uri).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int

                        while (inputStream?.read(buffer).also { length = it ?: 0 } != -1) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}