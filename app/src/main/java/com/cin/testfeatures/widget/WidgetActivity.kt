package com.cin.testfeatures.widget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.databinding.ActivityWidgetBinding

class WidgetActivity : AppCompatActivity() {
    private lateinit var bindin: ActivityWidgetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        bindin = ActivityWidgetBinding.inflate(layoutInflater)
        setContentView(bindin.root)
    }
}