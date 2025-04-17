package com.mobeiwsq.engine_project.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources

/**
* 获取res中的资源
*
* @author : mobeiwsq
* @since :  2025/4/17 11:05
*/

object ResUtils {

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     *
     * @param context        上下文
     * @param typedArray     样式属性数组
     * @param styleableResId 样式资源ID
     * @return Drawable
     */
    fun getDrawableAttrRes(context: Context, typedArray: TypedArray, @StyleableRes styleableResId: Int): Drawable? {
        val resourceId = typedArray.getResourceId(styleableResId, -1)
        if (resourceId != -1) {
            return AppCompatResources.getDrawable(context, resourceId)
        }
        return null
    }

    /**
     * 获取svg资源图片
     *
     * @param context 上下文
     * @param resId   图片资源id
     * @return svg资源图片
     */
    fun getVectorDrawable(context: Context, @DrawableRes resId: Int): Drawable? {
            return context.getDrawable(resId)
    }
}