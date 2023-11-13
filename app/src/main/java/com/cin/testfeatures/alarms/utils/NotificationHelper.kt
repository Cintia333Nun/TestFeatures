package com.cin.testfeatures.alarms.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cin.testfeatures.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NotificationHelper(private val context: Context) {
    @SuppressLint("MissingPermission")
    fun createNotification(title: String?, message: String?) {
        val expandedView = RemoteViews(
            context.packageName,
            R.layout.layout_remminder
        )
        expandedView.setTextViewText(R.id.title, title)
        expandedView.setTextViewText(R.id.description, message)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(
                    CHANNEL_ID, "Recordatorio",
                    NotificationManager.IMPORTANCE_HIGH
                )
                val manager = context
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(mChannel)
            }
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(uri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomContentView(expandedView)
                .setCustomBigContentView(expandedView)
                .build()
            NotificationManagerCompat.from(context).notify(createID(), notification)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //Utils.freeMemory()
        }
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
    }

    companion object {
        private const val CHANNEL_ID = "Recordatorio_de_cobranza"
    }
}