package com.cin.testfeatures.animations_activity_transitions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.databinding.ActivitySecondAnimBinding

class SecondAnimActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondAnimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivitySecondAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}