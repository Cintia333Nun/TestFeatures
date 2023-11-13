package com.cin.testfeatures.cipher_encryption

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.BuildConfig
import com.cin.testfeatures.databinding.ActivityCipherEncryptBinding

class CipherEncryptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCipherEncryptBinding
    private var consoleText = StringBuilder()
    private val textTest = "AFIRME"
    private var textCrypt = ""
    private var textDeCrypt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        try {
            testCrypt()
            testDeCrypt()
            printConsole()
        } catch (e:Exception) {
            Log.e("CRYPT", "onCreate: ", e)
        }
    }

    private fun initBinding() {
        binding = ActivityCipherEncryptBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun testCrypt() {
        textCrypt = textTest.encrypt(BuildConfig.DATA_LOADER)
        consoleText.append("Key: ${BuildConfig.DATA_LOADER}\n")
        consoleText.append("Text test: $textTest \n")
        consoleText.append("Crypt text: $textCrypt")
    }

    private fun testDeCrypt() {
        textDeCrypt = textCrypt.decrypt(BuildConfig.DATA_LOADER)
        consoleText.append("DeCrypt text: $textDeCrypt \n")
    }

    private fun printConsole() {
        binding.console.text = consoleText.toString()
    }
}