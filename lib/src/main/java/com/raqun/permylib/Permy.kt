package com.raqun.permylib

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment


object Permy {

    /*
     * Asks permissions if needed
     */
    fun askPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int,
        onPermissionsNotGranted: ((@ParameterName(name = "permissions") Array<String>, requestCode: Int) -> Unit)? = null,
        onAllPermissionsGranted: ((@ParameterName(name = "permissions") Array<String>, requestCode: Int) -> Unit)? = null
    ) {

        /*
        * Filter not granted permissions
        */
        val notGrantedPermissions = checkPermissions(
            activity.baseContext,
            requestCode,
            permissions,
            onAllPermissionsGranted
        )

        /*
         * If not granted permissions is not empty invoke onPermissionsNotGranted if provided
         * or ask permissions by default
         */
        if (!notGrantedPermissions.isNullOrEmpty()) {
            if (onPermissionsNotGranted != null) {
                onPermissionsNotGranted.invoke(notGrantedPermissions, requestCode)
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode)
            }
        }
    }

    /*
     * Asks permissions if needed
     */
    fun askPermissions(
        fragment: Fragment,
        permissions: Array<String>,
        requestCode: Int,
        onPermissionsNotGranted: ((@ParameterName(name = "permissions") Array<String>, requestCode: Int) -> Unit)? = null,
        onAllPermissionsGranted: ((@ParameterName(name = "permissions") Array<String>, requestCode: Int) -> Unit)? = null
    ) {
        /*
         * Filter not granted permissions
         */
        val notGrantedPermissions = checkPermissions(
            fragment.requireContext(),
            requestCode,
            permissions,
            onAllPermissionsGranted
        )

        /*
         * If not granted permissions is not empty invoke onPermissionsNotGranted if provided
         * or ask permissions by default
         */
        if (!notGrantedPermissions.isNullOrEmpty()) {
            if (onPermissionsNotGranted != null) {
                onPermissionsNotGranted.invoke(notGrantedPermissions, requestCode)
            } else {
                fragment.requestPermissions(permissions, requestCode)
            }
        }
    }

    /*
     * Checks if there're not granted permissions and return if
     */
    fun checkPermissions(
        context: Context,
        requestCode: Int,
        permissions: Array<String>,
        onAllPermissionsGranted: ((@ParameterName(name = "permissions") Array<String>, requestCode: Int) -> Unit)? = null
    ): Array<String>? {
        if (!OsUtil.hasM()) {
            /*
             * By default pre Android M all permissions are defined in Manifest
             * and granted by installing application
             */
            onAllPermissionsGranted?.invoke(permissions, requestCode)
            return null
        }

        /*
         * Checks if any of the requested permissions granted before.
         */
        val notGrantedPermissions = filterNotGrantedPermissions(permissions, context)
        if (notGrantedPermissions.isEmpty()) {
            onAllPermissionsGranted?.invoke(permissions, requestCode)
            return null
        }

        return notGrantedPermissions
    }

    /*
     * Filters not granted permissions
     */
    fun filterNotGrantedPermissions(
        permissions: Array<String>,
        context: Context
    ): Array<String> {
        return if (OsUtil.hasM()) {
            permissions.filter {
                PermissionChecker.checkSelfPermission(
                    context,
                    it
                ) != PermissionChecker.PERMISSION_GRANTED
            }.toTypedArray()
        } else {
            emptyArray()
        }
    }
}


