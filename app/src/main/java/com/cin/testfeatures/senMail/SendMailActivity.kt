package com.cin.testfeatures.senMail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cin.testfeatures.databinding.ActivitySendMailBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SendMailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendMailBinding
    private val scope by lazy { CoroutineScope(SupervisorJob()) }
    private var signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.let { handleSignInResult(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scope.launch(Dispatchers.IO) {
            val context = applicationContext
            val gmailSender = SendEmail(context)
            val recipientEmail = "youremail@gmail.com"
            gmailSender.sendEmail(recipientEmail)
        }
    }

    private fun handleSignInResult(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Toast.makeText(this,"Inicio de sesión exitoso, puedes acceder a la cuenta con account",Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Toast.makeText(this,"Error en el inicio de sesión, maneja el error",Toast.LENGTH_SHORT).show()
        }
    }
}