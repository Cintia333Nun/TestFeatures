package com.cin.testfeatures.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastAirPlaneMode: BroadcastReceiver() {
    companion object {
        const val KEY_STATUS_AIR_PLANE_MODE = "state"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirPlaneModeActive = intent?.getBooleanExtra(
            KEY_STATUS_AIR_PLANE_MODE, true
        ) ?: return

        if(isAirPlaneModeActive) {
            Toast.makeText(context, "Modo avión activo", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Modo avión desactivado", Toast.LENGTH_SHORT).show()
        }
    }
}