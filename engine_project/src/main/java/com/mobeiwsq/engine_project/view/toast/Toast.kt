package com.mobeiwsq.engine_project.view.toast

import android.os.Handler
import android.os.Looper
import com.mobeiwsq.engine_project.EngineConfig

/**
 * toast入口，直接调用即可
 * @param msg 显示内容
 * @param duration 显示时间，默认为0，可设置为1
 */
fun toast(
    msg: CharSequence?,
    duration: Int = 0,
) {
    showToast(msg, duration)
}

private fun showToast(
    msg: CharSequence?,
    duration: Int,
) {
    msg ?: return
    ToastConfig.toast?.cancel()
    runMain {
        ToastConfig.toast = ToastConfig.toastFactory.onCreate(EngineConfig.app, msg, duration)
        ToastConfig.toast?.show()
    }
}

fun runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}