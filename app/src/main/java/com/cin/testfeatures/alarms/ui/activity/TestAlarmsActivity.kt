package com.cin.testfeatures.alarms.ui.activity

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.alarms.model.AlarmModel
import com.cin.testfeatures.alarms.service.AlarmBroadcastReceiver
import com.cin.testfeatures.alarms.ui.adapter.AdapterTestAlarms
import com.cin.testfeatures.alarms.utils.TimePickerUtil
import com.cin.testfeatures.alarms.viewmodel.TestAlarmsViewModel
import com.cin.testfeatures.databinding.ActivityTestAlarmsBinding
import com.cin.testfeatures.databinding.AlertAlermBinding
import java.util.Calendar

/**
 * Necessary:
 * Add permission com.android.alarm.permission.SET_ALARM and SCHEDULE_EXACT_ALARM
 * Add broadcast This class will be responsible for receiving the alarm activation signal.
 * AlarmManager
 * Pending Intent
 * Configure Alarm
 * */
class TestAlarmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestAlarmsBinding
    private lateinit var adapter: AdapterTestAlarms
    private val viewModel by viewModels<TestAlarmsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initObjects()
        initObservers()
        initListeners()
    }

    private fun initBinding() {
        binding = ActivityTestAlarmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initObjects() {
        adapter = AdapterTestAlarms(this)
        binding.recyclerAlarm.adapter = adapter
    }

    private fun initObservers() {

    }

    private fun initListeners() {
        binding.buttonAddAlarm.setOnClickListener {
            createAlertTime()
        }
    }

    private fun createAlertTime() {
        val bindingAlertTime = AlertAlermBinding.inflate(layoutInflater)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(bindingAlertTime.root)

        alertDialogBuilder.setPositiveButton("Agregar Alarma") { dialog, _ ->
            createAlert(
                TimePickerUtil.getTimePickerHour(bindingAlertTime.pickerTime),
                TimePickerUtil.getTimePickerMinute(bindingAlertTime.pickerTime)
            )
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun createAlert(timePickerHour: Int, timePickerMinute: Int) {
        try {
            val alert = AlarmModel(
                System.currentTimeMillis(),
                "Alarm test title",
                timePickerHour,
                timePickerMinute
            )
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmBroadcastReceiver::class.java)
            intent.putExtra("alarm", alert)

            val alarmPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    this, alert.id.toInt(), intent, PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getBroadcast(
                    this,
                    alert.id.toInt(),
                    intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alert.hour)
                set(Calendar.MINUTE, alert.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmPendingIntent
                )
            }

            viewModel.listAlarms.add(alert)
            adapter.populateAlarms(viewModel.listAlarms)
            Toast.makeText(this, "Alerta creada", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al crear alerta", Toast.LENGTH_LONG).show()
        }
    }

}