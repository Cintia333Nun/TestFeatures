package com.cin.testfeatures.broadcast_receiver

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.databinding.ActivityBroadcastReceiverBinding

class BroadcastReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBroadcastReceiverBinding
    private lateinit var broadcastReceiver: BroadcastAirPlaneMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    private fun initBinding() {
        binding = ActivityBroadcastReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun registerReceiver() {
        broadcastReceiver = BroadcastAirPlaneMode()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also { intent ->
            registerReceiver(broadcastReceiver, intent)
        }
    }
}