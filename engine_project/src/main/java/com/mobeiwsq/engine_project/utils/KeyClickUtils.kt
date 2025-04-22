package com.mobeiwsq.engine_project.utils

import android.os.Handler
import android.os.Looper
import com.mobeiwsq.engine_project.logger.PageLog
import kotlin.system.exitProcess

/**
* 按键工具类
*
* @author : mobeiwsq
* @since :  2025/4/10 09:40
*/

object KeyClickUtils {

    private var isDoubleClickPending = false
    private val handler = Handler(Looper.getMainLooper())
    private val resetStateTask = Runnable { isDoubleClickPending = false }

    /**
     * 双击返回键
     */
    fun back2Click(){
        back2Click(1000)
    }

    /**
     * 双击返回键
     * @param intervalMillis 默认1秒内
     */
    fun back2Click(intervalMillis: Long) {
        handler.removeCallbacks(resetStateTask) // 关键优化点

        if (isDoubleClickPending) {
            exitApp()
            isDoubleClickPending = false
        } else {
            isDoubleClickPending = true
            PageLog.d("Press again to exit")
            handler.postDelayed(resetStateTask, intervalMillis)
        }
    }

    /**
     * TODO退出应用，需要修改
     */
    private fun exitApp(){
        exitProcess(0)
    }

}