package com.cin.testfeatures.broadcast_receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cin.testfeatures.databinding.ActivityBroadcastReceiver2Binding

class BroadcastReceiver2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityBroadcastReceiver2Binding
    //region BROADCASTRECEIVER-TEST
    private var broadcastReceiverBattery = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            batteryLevel?.let { value ->
                binding.batteryValue.text = value.toString()
            }
        }
    }
    private var broadcastReceiverWifi = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val statusWifi = intent?.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )
            statusWifi?.let { value ->
                val status = when(value) {
                    WifiManager.WIFI_STATE_ENABLED -> " Activado"
                    WifiManager.WIFI_STATE_DISABLED -> " Desactivado"
                    WifiManager.WIFI_STATE_UNKNOWN -> " Error"
                    else -> "***"
                }
                binding.wifiValue.text = status
            }
        }
    }
    private var broadcastReceiverAirplane = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isAirPlaneModeActive = intent?.getBooleanExtra(
                BroadcastAirPlaneMode.KEY_STATUS_AIR_PLANE_MODE, false
            )
            isAirPlaneModeActive?.let { value ->
                binding.airplaneValue.text = if (value) " Activado" else " Desactivado"
            }
        }
    }

    private val broadcastBluetooth by lazy { BroadcastBluetooth() }
    private val broadcastGps by lazy { BroadcastGps() }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        registerReceivers()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceivers()
    }

    private fun initBinding() {
        binding = ActivityBroadcastReceiver2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //region BROADCASTRECEIVER-TEST
    private fun registerReceivers() {
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also { intent ->
            registerReceiver(broadcastReceiverAirplane, intent)
        }
        IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION).also { intent ->
            registerReceiver(broadcastReceiverWifi, intent)
        }
        IntentFilter(Intent.ACTION_BATTERY_CHANGED).also { intent ->
            registerReceiver(broadcastReceiverBattery, intent)
        }
        IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED).also { intent ->
            registerReceiver(broadcastBluetooth, intent)
        }
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION).also { intent ->
            registerReceiver(broadcastGps, intent)
        }
    }

    private fun unregisterReceivers() {
        unregisterReceiver(broadcastReceiverAirplane)
        unregisterReceiver(broadcastReceiverWifi)
        unregisterReceiver(broadcastReceiverBattery)
        unregisterReceiver(broadcastBluetooth)
        unregisterReceiver(broadcastGps)
    }

    private fun initListeners() {
        with(binding) {
            buttonRegisterReceivers.setOnClickListener {
                registerReceivers()
                Toast.makeText(
                    this@BroadcastReceiver2Activity,
                    "Suscribir transmisiones",
                    Toast.LENGTH_SHORT
                ).show()
            }
            buttonUnregisterReceivers.setOnClickListener {
                unregisterReceivers()
                Toast.makeText(
                    this@BroadcastReceiver2Activity,
                    "Dar de baja transmisiones",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    //endregion
}