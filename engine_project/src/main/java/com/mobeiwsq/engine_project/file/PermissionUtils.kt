package com.mobeiwsq.engine_project.file

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobeiwsq.engine_project.view.toast.toast

object PermissionUtils {
    // 存储权限请求码
    private const val STORAGE_PERMISSION_REQUEST_CODE = 1001
    // 管理外部存储权限的请求码（用于启动设置页面的回调）
    const val MANAGE_STORAGE_REQUEST_CODE = 1002

    /**
     * 检查是否有访问所有文件的权限（包括PDF、ZIP等）
     */
    fun hasAllFileAccessPermission(activity: Activity): Boolean {
        return when {
            // Android 11+：需要MANAGE_EXTERNAL_STORAGE权限
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                Environment.isExternalStorageManager()
            }
            // Android 6.0-10：需要传统的读写权限
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(
                            activity,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
            }
            // Android 6.0以下：默认有权限
            else -> true
        }
    }

    /**
     * 请求访问所有文件的权限
     */
    fun requestAllFileAccessPermission(activity: Activity) {
        when {
            // Android 11+：引导用户到系统设置页面开启权限
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                toast("请打开文件管理权限")
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = android.net.Uri.parse("package:${activity.packageName}")
                }
                activity.startActivityForResult(intent, MANAGE_STORAGE_REQUEST_CODE)
            }
            // Android 6.0-10：请求传统的读写权限
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * 处理传统权限请求结果（Android 6.0-10）
     */
    fun handleStoragePermissionResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode != STORAGE_PERMISSION_REQUEST_CODE) return false
        if (grantResults.isEmpty()) return false

        // 检查读写权限是否都被授予
        return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }
}
