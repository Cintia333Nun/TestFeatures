package com.cin.testfeatures.permissions

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo.PROTECTION_DANGEROUS
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionsLibrary(private val activity: Activity) {
    companion object {
        const val TAG = "PermissionsLibrary"
        @SuppressLint("StaticFieldLeak")
        private var instance: PermissionsLibrary? = null

        /**
         * En esta clase usar el patron singleton para poder
         * comprobar los permisos necesarios antes de usar una
         * funcionalidad o comprobar todos los permisos requeridos
         *
         * @param activity Actividad desde donde se consultaran los permisos
         * */
        fun getInstance(activity: Activity): PermissionsLibrary {
            if (instance == null) instance = PermissionsLibrary(activity)
            return instance as PermissionsLibrary
        }

        private const val ALWAYS = true

        @SuppressLint("InlinedApi")
        val listAllDefaultPermissions = hashMapOf(
            READ_PHONE_STATE to ALWAYS,

            RECORD_AUDIO to ALWAYS,
            CAMERA to ALWAYS,

            BLUETOOTH to ALWAYS,
            BLUETOOTH_SCAN to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S),
            BLUETOOTH_CONNECT to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S),

            WRITE_EXTERNAL_STORAGE to (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q),
            WRITE_CALENDAR to ALWAYS,

            ACCESS_BACKGROUND_LOCATION to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q),
            ACCESS_FINE_LOCATION to ALWAYS,
            ACCESS_COARSE_LOCATION to ALWAYS,

            USE_FINGERPRINT to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M),
            USE_BIOMETRIC to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P),

            POST_NOTIFICATIONS to (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU),
        )
    }

    /**
     * REGION CHECK PERMISSIONS APP
     * En esta region se encuentran metodos que se podrian usar
     * */
    //region CHECK PERMISSIONS APP
    /**
     * LOS PERMISOS SON OBTENIDOS "DINAMICAMENTE"
     * Comprueba si la aplicacion cuenta con los permisos necesarios declarados en el manifiesto,
     * en caso de no contar con ellos los solicita al usuario.
     *
     * SE NECESITA
     * ->Comprobar el orden de los permisos en el manifiesto,esto puede afectar a que permisos
     * peligrosos no se obtendran en la lista que retorna:
     * packageInfo.requestedPermissions
     * Como: READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE (uso malintencionado)
     *
     * -> Agregar el min y max sdk necesario en los atributos de la etiqueta uses-permission
     *
     * @return Retorna si la aplicacion cuenta con todos los permisos necesarios QUE SE PUEDEN
     * SOLICITAR EN TIEMPO DE EJECUCION "true"
     * */
    fun checkPermissionsApp(): Boolean {
        val deniedPermissions = getDeniedPermissions()
        return checkPermissionsList(deniedPermissions)
    }

    /**
     * LOS PERMISOS A COMPROBAR ESTAN DEFINIDOS EN LA LIBRERIA
     * Comprueba si la aplicacion cuenta con los permisos necesarios declarados,
     * en caso de no contar con ellos los solicita al usuario.
     *
     * SE NECESITA
     * -> Comprobar que se encuentren los permisos definidos en duro en la libreria en el manifiesto
     * -> Modificar Permisos en duro en caso de que se modifiquen los permisos del manifiesto
     * -> Comprobar que todos los permisos en duro se usen, de lo contrario comentar los que no sean
     * necesarios
     * -> Al agregarse un permiso leer docuemntacion para definir el rango de uso en los
     * niveles de api
     *
     * @return Retorna si la aplicacion cuenta con todos los permisos necesarios QUE SE PUEDEN
     * SOLICITAR EN TIEMPO DE EJECUCION "true"
     * */
    fun checkDefaultPermissionsApp(): Boolean {
        val permissionCheckApiLevel = getListAllDefaultPermissions()
        return checkPermissionsList(permissionCheckApiLevel)
    }

    /**
     *
     * */
    private fun getListAllDefaultPermissions(): Array<String> {
        val arrayList = arrayListOf<String>()
        for (item in listAllDefaultPermissions) {
            if (item.value) arrayList.add(item.key)
        }
        return arrayList.toTypedArray()
    }

    /**
     *
     * */
    private fun checkPermissionsList(listPermissions: Array<String>): Boolean {
        val permissionsWithAlert = mutableListOf<String>()
        val permissionsWithOutAlert = mutableListOf<String>()

        return if (listPermissions.isNotEmpty()) {
            Log.i(TAG, "checkPermissionsList: permission count = ${listPermissions.size}")
            listPermissions.forEach { permission ->
                val isNecessaryShowRequest = isNecesaryShowRequest(permission)
                Log.i(TAG, "checkPermissionsList: isNecesaryShowRequest = $isNecessaryShowRequest")

                if (isNecessaryShowRequest) permissionsWithAlert.add(permission)
                else permissionsWithOutAlert.add(permission)
            }
            if (permissionsWithOutAlert.isNotEmpty()) {
                requestPermissions(permissionsWithOutAlert.toTypedArray())
            }
            permissionsWithAlert.forEach { permission ->
                createAlerterNeedUserPermission(permission,permission)
            }
            false
        } else true
    }
    //endregion

    /**
     * REGION CHECK PERMISSIONS DENIED AT RUN TIME
     * Existen varios permisos que se pueden denegar después de un tiempo:
     * Ubicacion
     * Contactos
     * Camara
     * Microfono
     * Por cuestiones de seguridad para el usuario
     *
     * Comprobar antes de usar una funcionalidad que los permisos necesarios fueran concedidos
     * */
    //region PERMISSIONS DENIED AT RUN TIME AND DANGEROUS
    /**
     * Comprueba que todos los permisos referentes a la ubicacion fueran concedidos,
     * en caso de que los permisos no fueran concedidos solicitar al usuario mediante
     * una vista explicandole los pasos a seguir para avilitarlos
     *
     * @return Retorna si la aplicacion cuenta con todos los permisos necesarios
     * para la ubicacion en segundo plano.
     * */
    fun checkPermissionsLocation(): Boolean {
        return if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
            val backgroundLocation = checkPermissionIsGranted(ACCESS_BACKGROUND_LOCATION)
            val coarseLocation = checkPermissionIsGranted(ACCESS_COARSE_LOCATION)
            val fineLocation = checkPermissionIsGranted(ACCESS_FINE_LOCATION)
            Log.i(TAG, "checkPermissionsLocation: backgroundLocation = $backgroundLocation")
            Log.i(TAG, "checkPermissionsLocation: coarseLocation = $coarseLocation")
            Log.i(TAG, "checkPermissionsLocation: fineLocation = $fineLocation")

            val isAppLocationPermissionGranted = backgroundLocation && coarseLocation
            val preciseLocationAllowed = fineLocation && coarseLocation
            val isAllLocationPermissionGranted = isAppLocationPermissionGranted && preciseLocationAllowed
            Log.i(TAG, "checkPermissionsLocation: isAppLocationPermissionGranted = $isAppLocationPermissionGranted")
            Log.i(TAG, "checkPermissionsLocation: preciseLocationAllowed = $preciseLocationAllowed")
            Log.i(TAG, "checkPermissionsLocation: isAllLocationPermissionGranted = $isAllLocationPermissionGranted")

            return if (!isAllLocationPermissionGranted) {
                createAlerterNeedUserPermissionLocation()
                false
            } else true
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
            val backgroundLocation = checkPermissionIsGranted(ACCESS_BACKGROUND_LOCATION)
            val coarseLocation = checkPermissionIsGranted(ACCESS_COARSE_LOCATION)
            val isAppLocationPermissionGranted = backgroundLocation && coarseLocation
            Log.i(TAG, "checkPermissionsLocation: backgroundLocation = $backgroundLocation")
            Log.i(TAG, "checkPermissionsLocation: coarseLocation = $coarseLocation")
            Log.i(TAG, "checkPermissionsLocation: isAppLocationPermissionGranted = $isAppLocationPermissionGranted")

            return if (!isAppLocationPermissionGranted) {
                createAlerterNeedUserPermissionLocation()
                false
            } else true
        } else true
    }

    /**
     * Comprueba que el permiso para usar la camara del dispositivo fuera concedido.
     *
     *  @return Retorna si la aplicacion cuenta con el permiso para acceder a la camara
     * */
    fun checkPermissionCamera(): Boolean {
        val camera = checkPermissionIsGranted(CAMERA)
        Log.i(TAG, "checkPermissionCamera: camera = $camera")

        return if(!camera) {
            createAlerterNeedUserPermission(
                CAMERA,
                "La captura de evidencias en las visitas"
            )
            false 
        } else true
    }

    /**
     * Comprueba que el permiso para usar el microfono del dispositivo fuera concedido.
     *
     *  @return Retorna si la aplicacion cuenta con el permiso para acceder al microfono
     * */
    fun checkPermissionAudio(): Boolean {
        val recordAudio = checkPermissionIsGranted(RECORD_AUDIO)
        Log.i(TAG, "checkPermissionAudio: recordAudio = $recordAudio")

        return if(!recordAudio) {
            createAlerterNeedUserPermission(
                RECORD_AUDIO,
                "El asistente MC"
            )
            false
        } else true
    }

    /**
     * Comprueba que el permiso ingresado fuera concedido.
     *
     *  @param permission
     *  @param descriptionPermission
     *  @param feature
     *  @return Retorna si la aplicacion cuenta con el permiso para acceder al microfono
     * */
    fun checkGeneralPermission(
        permission: String, feature:String
    ): Boolean {
        val permissionIsGranted = checkPermissionIsGranted(permission)
        Log.i(TAG, "checkGeneralPermission: permissionIsGranted = $permissionIsGranted")

        return if(!permissionIsGranted) {
            createAlerterNeedUserPermission(
                permission,
                feature
            )
            false
        } else true
    }
    //endregion

    /**
     * REGION PERMISSIONS METHODS
     * Los permisos en android son las autorizaciones que se le da a una aplicacion para acceder
     * a algun recurso del dispositivo.
     * Los permisos se pueden otorgar indivividuales o en grupos y dependiendo de la
     * politica de privacidad y los cambios en los niveles de API estos pueden variar.
     * */
    //region PERMISSIONS METHODS
    /**
     * Comprueba si el permisos fue concedido por el usuario
     *
     * @param permission Permiso a verificar
     * @sample CAMERA
     * @return Si el permiso es concedido retorna "true"
     * */
    private fun checkPermissionIsGranted(permission: String): Boolean {
        val check = ContextCompat.checkSelfPermission(
            activity, permission
        ) == PackageManager.PERMISSION_GRANTED
        Log.i(TAG, "checkPermissionIsGranted: $check")
        return check
    }

    /**
     * Comprueba si el permiso requiere mostrar al usuario en la IU una descripcion del uso del
     * permiso por seguridad.
     *
     * @param permission Permiso a verificar
     * @return Si el permiso requiere crear un mensaje en la IU retorna "true"
     * */
    private fun isNecesaryShowRequest(permission: String): Boolean {
        val check = shouldShowRequestPermissionRationale(activity, permission)
        Log.i(TAG, "permissionWasDenied: $check")
        return check
    }

    /**
     * Solicita permisos al usuario en tiempo de ejecucion
     *
     * @param permissions Lista de permisos a verificar
     * @param requestCode Lista de permisos a verificar
     * */
    private fun requestPermissions(permissions: Array<String>, requestCode: Int = 1) {
        permissions.forEach { permission ->
            Log.i(TAG, "requestPermissions: permission = $permission")
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    /**
     * Obtine todos los permisos definidos en el manifiesto,
     * se guardan los permisos denegados por el usuatio dependiendo
     * si se pueden solicitar en tiempo de ejecucion y el nivel de proteccion del permiso.
     *
     * @return Retorna todos los permisos por conceder, si la lista esta vacia ya todos los
     * permisos necesarios fueron concedidos
     * */
    private fun getDeniedPermissions(): Array<String> {
        val deniedPermissions = mutableListOf<String>()
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity.packageManager.getPackageInfo(
                    activity.packageName,
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
                )
            } else {
                @Suppress("DEPRECATION")
                activity.packageManager.getPackageInfo(
                    activity.packageName,
                    PackageManager.GET_PERMISSIONS
                )
            }
            val requestedPermissions = packageInfo.requestedPermissions
            requestedPermissions?.forEach { permission ->
                Log.i(TAG, ":::::::::::::::::::::: NEW PERMISSION $permission ::::::::::::::::::::::::: ")
                if (!checkPermissionIsGranted(permission)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val protectionLevel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            activity.packageManager.getPermissionInfo(permission, 0).protection
                        } else {
                            @Suppress("DEPRECATION")
                            activity.packageManager.getPermissionInfo(permission, 0).protectionLevel
                        }
                        Log.i(TAG, "getDeniedPermissions: protectionLevel = $protectionLevel")
                        val isDangerous = protectionLevel == PROTECTION_DANGEROUS
                        Log.i(TAG, "getDeniedPermissions: isDangerous = $isDangerous")

                        if (isDangerous) {
                            Log.i(TAG, "getDeniedPermissions: add permission = $permission")
                            deniedPermissions.add(permission)
                        }  else Log.i(TAG, "getDeniedPermissions: not add permission = $permission")
                    } else deniedPermissions.add(permission)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "getDeniedPermissions: ", e)
            e.printStackTrace()
        }

        return deniedPermissions.toTypedArray()
    }

    /**
     * Muestra al usuario una alerta con la descripcion del servicio o ejecuta una "interfaz"
     * con una accion que permita conceder el permiso necesario
     *
     * @param permission Permiso a verificar
     * @sample CAMERA
     * @return Si el permiso es concedido retorna "true"
     * */
    private fun createAlerterNeedUserPermission(
        permission: String, feature: String = ""
    ) {
        try {
            val isNecesaryShowRequest = isNecesaryShowRequest(permission)
            Log.i(TAG, "createAlerterNeedUserPermission: isNecesaryShowRequest = $isNecesaryShowRequest")

            if (isNecesaryShowRequest) {
                val title = "Permiso requerido"
                val text = "$feature requiere el permiso $permission para funcionar correctamente."

                val alert = AlertDialog.Builder(activity)
                alert.setTitle(title)
                alert.setMessage(text)
                alert.setCancelable(false)
                alert.setPositiveButton("Aceptar") { _, _ ->
                    requestPermissions(arrayOf(permission))
                }
                alert.show()
            } else requestPermissions(arrayOf(permission))
        } catch (e: Exception) {
            Log.e(TAG, "createAlerterNeedUserPermission: ", e)
        }
    }

    private fun createAlerterNeedUserPermissionLocation() {
        val title = "Permiso requerido"
        val text = "La monitoreo backoffice requiere el permiso de ubicación en segundo plano para funcionar correctamente."

        val alert = AlertDialog.Builder(activity)
        alert.setTitle(title)
        alert.setMessage(text)
        alert.setCancelable(false)
        alert.setPositiveButton("Aceptar") { _, _ -> goToActLocationPermission() }
        alert.show()
    }

    private fun goToActLocationPermission() {
        Toast.makeText(activity, "Ir a vista de ubicacion", Toast.LENGTH_SHORT).show()
    }
    //endregion
}