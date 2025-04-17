package com.mobeiwsq.engine_project.utils

import android.content.Context

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
}