package com.mobeiwsq.engine_project.view.toast

import android.annotation.SuppressLint
import android.widget.Toast

/**
* toast类
*
* @author : mobeiwsq
* @since :  2025/4/14 15:18
*/

@SuppressLint("StaticFieldLeak")
object ToastConfig {

    internal var toast: Toast? = null

    var toastFactory: ToastFactory = ToastFactory

    fun cancel() {
        toast?.cancel()
    }

}