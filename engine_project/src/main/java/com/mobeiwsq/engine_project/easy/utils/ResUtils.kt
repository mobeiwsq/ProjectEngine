package com.mobei.engine.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.StyleableRes

/**
 * 获取res中的资源
 *
 * @author : mobeiwsq
 * @since :  2025/5/27 16:44
 */
object ResUtils {

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     */
    fun getDrawableAttrRes(
        context: Context, typedArray: TypedArray,
        @StyleableRes styleableResId: Int
    ): Drawable? {
        return typedArray.getDrawable(styleableResId)
    }

    /**
     * 获取dimes值，返回的是【4舍5入取整】的值
     *
     * @param context 上下文
     * @param resId   资源id
     * @return dimes值【4舍5入取整】
     */
    fun getDimensionPixelSize(context: Context, @DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelSize(resId)
    }
}