package com.cin.testfeatures.biometric_auth

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.cin.testfeatures.databinding.ActivityBiometricAuthBinding

class BiometricAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBiometricAuthBinding
    private var cancellationSignal: CancellationSignal? = null
    private val  authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication Success!")
                    startActivity(Intent(this@BiometricAuthActivity, SecretActivity::class.java))
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initObjects()
    }

    private fun initBinding() {
        binding = ActivityBiometricAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("NewApi")
    private fun initObjects() {
        binding.buttonSecret.setOnClickListener{
            if (checkBiometricSupport()) {
                val biometricPrompt : BiometricPrompt = BiometricPrompt.Builder(this)
                    .setTitle("Title")
                    .setSubtitle("Authenticaion is required")
                    .setDescription("Fingerprint Authentication")
                    .setNegativeButton("Cancel", this.mainExecutor) {_,_ -> }
                    .build()

                biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
            }
        }
    }

    private fun notifyUser(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener { notifyUser("Authentication was cancelled by the user") }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if(!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        if (
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) !=
            PackageManager.PERMISSION_GRANTED ) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}