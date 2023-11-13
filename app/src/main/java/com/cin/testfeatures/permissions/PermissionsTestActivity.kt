package com.cin.testfeatures.permissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cin.testfeatures.databinding.ActivityPermissionsTestBinding

class PermissionsTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionsTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissionsLibrary()
    }

    private fun checkPermissionsLibrary() {
        val permissionsLibrary = PermissionsLibrary(this)

        // TEST A13
        /*
        val checkPermissionCamera = permissionsLibrary.checkPermissionCamera()
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkPermissionCamera = $checkPermissionCamera")
        */

        /*
        val checkPermissionAudio = permissionsLibrary.checkPermissionAudio()
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkPermissionAudio = $checkPermissionAudio")
        */

        /*
        val checkPermissionAudio = permissionsLibrary.checkPermissionsLocation()
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkPermissionAudio = $checkPermissionAudio")
        */

        /*
        val checkGeneralPermission = permissionsLibrary.checkGeneralPermission(
            Manifest.permission.ACCESS_FINE_LOCATION, "Funcionalidad ejemplo"
        )
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkGeneralPermission = $checkGeneralPermission")
        */
        
        /*
        val checkPermissionsApp = permissionsLibrary.checkPermissionsApp()
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkPermissionsApp = $checkPermissionsApp")
        */

        /*
        val checkDefaultPermissionsApp = permissionsLibrary.checkDefaultPermissionsApp()
        Log.i("PermissionsLibrary", "checkPermissionsLibrary: checkDefaultPermissionsApp = $checkDefaultPermissionsApp")
         */
    }
}