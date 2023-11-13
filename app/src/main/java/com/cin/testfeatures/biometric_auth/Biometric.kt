package com.cin.testfeatures.biometric_auth

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Biometric (private val activity: AppCompatActivity) {
    private val TAG = this.javaClass::class.java.simpleName

    enum class BiometricType {SUCCEEDED, ERROR, FAILED}

    fun checkBiometricSupport(): Boolean {
        val keyguardManager : KeyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if(!keyguardManager.isKeyguardSecure) {
            Log.i(TAG, " :::BiometricSupport::: Fingerprint hs not been enabled in settings.")
            return false
        }
        if (
            ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.USE_BIOMETRIC) !=
            PackageManager.PERMISSION_GRANTED ) {
            Log.i(TAG, " :::BiometricSupport::: Fingerprint hs not been enabled in settings.")
            return false
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometricManager = BiometricManager.from(activity)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.i(TAG, " :::BiometricSupport::: Not exist hardware")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.i(TAG, " :::BiometricSupport::: Unavailable hardware")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.i(TAG, " :::BiometricSupport::: Add biometric key")
                    activity.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                    false
                }
                else -> false
            }
        } else false
    }

    private fun setBiometricPromptInfo(
        title: String,
        subtitle: String,
        description: String
    ): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText("Cancel")

        return builder.build()
    }

    private fun initBiometricPrompt(
        listener: (BiometricType) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.i(TAG, " :::BiometricSupport::: Authentication error")
                listener.invoke(BiometricType.ERROR)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.i(TAG, " :::BiometricSupport::: Authentication failed")
                listener.invoke(BiometricType.FAILED)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.invoke(BiometricType.SUCCEEDED)
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }

    fun showBiometricPrompt(
        title: String = "Biometric Authentication",
        subtitle: String = "Enter biometric credentials to proceed.",
        description: String = "Input your Fingerprint or FaceID to ensure it's you!",
        cryptoObject: BiometricPrompt.CryptoObject? = null,
        listener: (BiometricType) -> Unit
    ) {
        val promptInfo = setBiometricPromptInfo(
            title,
            subtitle,
            description
        )

        val biometricPrompt = initBiometricPrompt(listener)
        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }
}