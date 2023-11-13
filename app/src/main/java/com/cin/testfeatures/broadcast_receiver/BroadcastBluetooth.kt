package com.cin.testfeatures.broadcast_receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastBluetooth: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bluetoothExtra = intent?.getIntExtra(
            BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR
        ) ?: return

        context?.let { ctx ->
            when(bluetoothExtra) {
                BluetoothAdapter.STATE_OFF -> isOffTask(ctx)
                BluetoothAdapter.STATE_TURNING_ON -> isTurnOnTask(ctx)
                BluetoothAdapter.STATE_ON -> isOnTask(ctx)
                BluetoothAdapter.STATE_TURNING_OFF -> isTurnOffTask(ctx)
            }
        }
    }

    private fun isOffTask(context: Context) {
        Toast.makeText(context, "Bluetooth desactivado", Toast.LENGTH_SHORT).show()
    }

    private fun isTurnOnTask(context: Context) {
        Toast.makeText(context, "Bluetooth cambia activado", Toast.LENGTH_SHORT).show()
    }

    private fun isOnTask(context: Context) {
        Toast.makeText(context, "Bluetooth activado", Toast.LENGTH_SHORT).show()
    }

    private fun isTurnOffTask(context: Context) {
        Toast.makeText(context, "Bluetooth desactivado", Toast.LENGTH_SHORT).show()
    }
}