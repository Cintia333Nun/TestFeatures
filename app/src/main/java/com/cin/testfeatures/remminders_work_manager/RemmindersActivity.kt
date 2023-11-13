package com.cin.testfeatures.remminders_work_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.cin.testfeatures.R
import com.cin.testfeatures.remminders_work_manager.ReminderWorker.KEY_MESSAGE
import com.cin.testfeatures.remminders_work_manager.ReminderWorker.KEY_TITLE
import java.util.*
import java.util.concurrent.TimeUnit

class RemmindersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remminders)

        var chosenYear = 0
        var chosenMonth = 0
        var chosenDay = 0
        var chosenHour = 0
        var chosenMin = 0

        val descriptionText = findViewById<EditText>(R.id.editText)
        val button = findViewById<Button>(R.id.setBtn)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val today = Calendar.getInstance()

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            chosenYear = year
            chosenMonth = month
            chosenDay = day
        }

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            chosenHour = hour
            chosenMin  = minute
        }

        button.setOnClickListener {
            val userSelectedDateTime =Calendar.getInstance()
            userSelectedDateTime.set(chosenYear, chosenMonth, chosenDay, chosenHour , chosenMin)

            val todayDateTime = Calendar.getInstance()

            val delayInSeconds = (userSelectedDateTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L)

            val delayInMilliSeconds = (userSelectedDateTime.timeInMillis) - (todayDateTime.timeInMillis);

            //createWorkRequest(descriptionText.text.toString(), delayInSeconds)
            createWorkRequestJava(descriptionText.text.toString(), delayInMilliSeconds)

            Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createWorkRequest(message: String,timeDelayInSeconds: Long  ) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to "Reminder",
                "message" to message,
            )
            )
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }

    private fun createWorkRequestJava(message: String,timeDelayInSeconds: Long  ) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(
                KEY_TITLE to "Reminder",
                KEY_MESSAGE to message,
            )
            )
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }
}