package com.cranked.androidfileconverter.utils.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {
    fun checkPermissions(activity: AppCompatActivity, permissions: Array<String>,resultCode:Int): Boolean {
        var permissionsList = arrayListOf<String>()
        permissions.forEach {
            if (!hasPermission(activity, it)
            ) {
                permissionsList.add(it)
            }
        }
        if (!permissionsList.isEmpty()) {
            val permission = permissionsList.toArray().map { it as String }.toTypedArray()
            ActivityCompat.requestPermissions(
                activity,
                permission,
                resultCode
            )
        } else
            return true
        return false
    }

    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}