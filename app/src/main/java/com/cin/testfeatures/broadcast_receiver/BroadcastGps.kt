package com.cin.testfeatures.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.Toast

class BroadcastGps : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) enableGPSTask(ctx)
            else disableGPSTask(ctx)
        }
    }

    private fun enableGPSTask(context: Context) {
        Toast.makeText(context, "GPS activado", Toast.LENGTH_SHORT).show()
    }

    private fun disableGPSTask(context: Context) {
        Toast.makeText(context, "GPS desactivado", Toast.LENGTH_SHORT).show()
    }
}