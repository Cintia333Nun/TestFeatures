package com.cin.testfeatures.senMail

import android.content.Context
import android.util.Log
import com.cin.testfeatures.R
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Message
import java.io.ByteArrayOutputStream
import java.util.Properties
import javax.mail.Message.RecipientType
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class SendEmail(private val context: Context) {
    private val SCOPES = listOf("https://www.googleapis.com/auth/gmail.send")
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    fun sendEmail(destination: String) {
        val credential = getGoogleCredentials()
        val service = createGmailService(credential)

        val randomCode = generateRandomCode()

        val emailContent = "Your verification code is: $randomCode"
        val subject = "Verification Code"

        val mimeMessage = createMimeMessage("me", destination, subject, emailContent)
        val rawMessage = createRawMessage(mimeMessage)

        try {
            service.users().messages().send("me", rawMessage).execute()
            Log.d("GmailSender", "Email sent successfully")
        } catch (e: Exception) {
            Log.e("GmailSender", "Error sending email: ${e.message}")
        }
    }

    private fun getGoogleCredentials(): GoogleCredential {
        return GoogleCredential.fromStream(context.resources.openRawResource(R.raw.client_secret))
            .createScoped(SCOPES)
    }

    private fun createGmailService(credential: GoogleCredential): Gmail {
        return Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
            .setApplicationName("YourAppName")
            .build()
    }

    private fun generateRandomCode(): String {
        return "123456"
    }

    private fun createMimeMessage(
        from: String,
        to: String,
        subject: String,
        bodyText: String
    ): MimeMessage {
        val properties = Properties()
        val session: Session = Session.getDefaultInstance(properties, null)

        val mimeMessage = MimeMessage(session)
        mimeMessage.setFrom(InternetAddress(from))
        mimeMessage.addRecipient(RecipientType.TO, InternetAddress(to))
        mimeMessage.subject = subject

        val multipart = MimeMultipart()
        val messageBodyPart = MimeBodyPart()
        messageBodyPart.setText(bodyText)
        multipart.addBodyPart(messageBodyPart)

        mimeMessage.setContent(multipart)
        return mimeMessage
    }

    private fun createRawMessage(mimeMessage: MimeMessage): Message {
        val byteArrayOutputStream = ByteArrayOutputStream()
        mimeMessage.writeTo(byteArrayOutputStream)
        val rawMessage = byteArrayOutputStream.toByteArray()
        return Message().apply {
            raw = android.util.Base64.encodeToString(rawMessage, android.util.Base64.URL_SAFE)
        }
    }
}
