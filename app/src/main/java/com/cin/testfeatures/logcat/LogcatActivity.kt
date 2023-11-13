package com.cin.testfeatures.logcat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cin.testfeatures.R
import tony.com.logmodel.LogWindow


class LogcatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logcat)
        val logWindow = LogWindow(this, application)
        logWindow.creatLogView()

    }
}