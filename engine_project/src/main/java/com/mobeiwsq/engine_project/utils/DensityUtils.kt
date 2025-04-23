package com.mobeiwsq.engine_project.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.max

/**
* 屏幕密度工具类
* 
* @author : mobeiwsq
* @since :  2025/4/17 10:53
*/

object DensityUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 底部导航条是否开启
     *
     * @param context 上下文
     * @return 底部导航条是否显示
     */
    fun isNavigationBarExist(context: Context): Boolean {
        return getNavigationBarHeight(context) > 0
    }

    /**
     * 获取系统底部导航栏的高度
     *
     * @param context 上下文
     * @return 系统状态栏的高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        val windowManager = if (context is Activity) {
            context.windowManager
        } else {
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        if (windowManager == null) {
            return 0
        }
        val defaultDisplay = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(realDisplayMetrics)
        }
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        defaultDisplay.getMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        if (realHeight - displayHeight > 0) {
            return realHeight - displayHeight
        }
        return max((realWidth - displayWidth).toDouble(), 0.0).toInt()
    }
}