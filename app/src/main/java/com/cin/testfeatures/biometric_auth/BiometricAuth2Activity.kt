package com.cin.testfeatures.biometric_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.databinding.ActivityBiometricAuthBinding

class BiometricAuth2Activity: AppCompatActivity() {
    private lateinit var binding: ActivityBiometricAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        useBiometrics()
    }

    private fun initBinding() {
        binding = ActivityBiometricAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun useBiometrics() = binding.buttonSecret.setOnClickListener { listenerButtonSecret() }

    private fun listenerButtonSecret() {
        val biometric = Biometric(this)
        if (biometric.checkBiometricSupport()) {
            biometric.showBiometricPrompt(
                "Title","Subtitle","Description",null
            ) { type ->
                when(type) {
                    Biometric.BiometricType.SUCCEEDED -> succededBiometrics()
                    Biometric.BiometricType.ERROR -> errorBiometrics()
                    Biometric.BiometricType.FAILED -> failedBiometrics()
                }
            }
        } else notifyUser("Not support Biometrics")
    }

    private fun succededBiometrics() = startActivity(Intent(this, SecretActivity::class.java))

    private fun errorBiometrics() {
        notifyUser("ERROR BIOMETRICS!!!!")
    }

    private fun failedBiometrics() {
        notifyUser("FAILED BIOMETRICS!!!!")
    }

    private fun notifyUser(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}