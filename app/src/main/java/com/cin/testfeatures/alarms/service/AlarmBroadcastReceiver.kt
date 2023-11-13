package com.cin.testfeatures.alarms.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cin.testfeatures.alarms.model.AlarmModel
import com.cin.testfeatures.alarms.utils.NotificationHelper

class AlarmBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TAG", ":::AlarmBroadcastReceiver::: onReceive: ")
        val alarm: AlarmModel? = intent?.getParcelableExtra("alarm")
        if (alarm != null) {
            val id: Long = alarm.id
            val title: String = alarm.title

            Toast.makeText(context, "Alarma recibida", Toast.LENGTH_SHORT).show()
            NotificationHelper(context!!).createNotification(
                "Alarma con id $id",
                title
            )
        }
    }
}