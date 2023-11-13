package com.cin.testfeatures.update_manager

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.cin.testfeatures.BuildConfig
import com.cin.testfeatures.databinding.ActivityUpdateAppManagerBinding
import java.io.File


class UpdateAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAppManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObjects()
        //checkForUpdates()
        if (checkPermissionsInstallAppUpdates()) downloadApp()
    }

    /*private fun checkForUpdates() {
        //ADD VIEWMODEL
        val dataSource = UpdateAppDataSource(RetrofitClient().getAppUpdateApi())
        dataSource.checkForAppUpdates(
            BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE.toString(),
            File(filesDir,"update.apk"), ::installAppUpdates
        )
    }*/

    private fun downloadApp() {
        //ADD VIEWMODEL
        val dataSource = UpdateAppDataSource(RetrofitClient().getAppUpdateApi())
        dataSource.download(
            File(filesDir,"update.apk"),
            ::installAppUpdates
        )
    }

    private fun initObjects() {
        binding = ActivityUpdateAppManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun checkPermissionsInstallAppUpdates(): Boolean {
        var isOk: Boolean

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isOk =false
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        } else isOk = true

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isOk = isOk.and(false)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else isOk = isOk.and(true)

        return isOk
    }

    private fun installAppUpdates(isDownload: Boolean, file: File?) {
        if (isDownload) {
            file?.let {
                if (file.exists()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    println(file.path)
                    intent.setDataAndType(
                        uriFromFile(applicationContext, file),
                        "application/vnd.android.package-archive"
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    try {
                        applicationContext.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }

                } else Toast.makeText(applicationContext, "installing", Toast.LENGTH_LONG).show()
            } ?: Toast.makeText(this, "fail download", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(applicationContext, "fail download", Toast.LENGTH_LONG).show()
    }

    private fun uriFromFile(context: Context, file: File): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        } else Uri.fromFile(file)
    }
}