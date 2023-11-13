package com.cin.testfeatures.animations_activity_transitions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.databinding.ActivityFirstAnimBinding

class FirstAnimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstAnimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configAnimProgrammatically()
        initBinding()
        initListener()
    }

    private fun configAnimProgrammatically() {
        window.enableTransitions(true)
        window.configEnterExitAnim(
            isEnableAnim = true,
            overlap = true,
            TypeAnimEnterExit.FADE
        )
    }

    private fun initBinding() {
        binding = ActivityFirstAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initListener() {
        binding.buttonTransition.setOnClickListener { activityTransition() }
    }

    private fun activityTransition() {
        val intent = Intent(this, SecondAnimActivity::class.java)
        transition(isEnableAnim = true, intent)
    }
}










