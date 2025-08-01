package com.mobeiwsq.engine_project.file

import android.app.Activity
import android.content.Intent

object FileSelectManager {
    // 请求码
    const val FILE_SELECT_REQUEST_CODE = 10086

    // 文件选择回调接口
    interface OnFileSelectListener {
        fun onFileSelected(filePath: String)
        fun onCanceled()
        fun onPermissionDenied() {}
    }

    // 回调接口
    private var listener: OnFileSelectListener? = null

    // 从Activity启动文件选择器
    fun open(activity: Activity, listener: OnFileSelectListener, allowedFileTypes: ArrayList<String?>? = null,) {
        FileSelectManager.listener = listener
        if (PermissionUtils.hasAllFileAccessPermission(activity)) {
            launchFilePicker(activity, allowedFileTypes)
        } else {
            PermissionUtils.requestAllFileAccessPermission(activity)
        }
    }


    // 启动文件选择器
    fun launchFilePicker(activity: Activity, allowedFileTypes: ArrayList<String?>?) {
        val intent = Intent(activity, FileSelectActivity::class.java).apply {
            // 传递允许的文件类型列表
            putStringArrayListExtra(
                FileSelectActivity.EXTRA_ALLOWED_FILE_TYPES,
                allowedFileTypes
            )
        }
        activity.startActivityForResult(intent, FILE_SELECT_REQUEST_CODE)
    }

    // 处理权限请求结果
    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        activity: Activity,
        allowedFileTypes: ArrayList<String?>? = null
    ) {
        if (PermissionUtils.handleStoragePermissionResult(requestCode, grantResults)) {
            launchFilePicker(activity, allowedFileTypes)
        } else {
            listener?.onPermissionDenied()
        }
    }

    // 处理Activity结果
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_SELECT_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val filePath = data?.getStringExtra("file_path")
                    filePath?.let { listener?.onFileSelected(it) }
                }

                Activity.RESULT_CANCELED -> {
                    listener?.onCanceled()
                }
            }
            // 清除监听器，避免内存泄漏
            listener = null
        }
    }


}