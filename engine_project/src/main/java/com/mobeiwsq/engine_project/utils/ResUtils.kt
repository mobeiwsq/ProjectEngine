package com.mobeiwsq.engine_project.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.mobeiwsq.engine_project.EngineConfig

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

    /**
     * 获取资源图片【和主题有关】
     *
     * @param resId 图片资源id
     * @return 资源图片
     */
    fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId)
        }
        return AppCompatResources.getDrawable(context, resId)
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

    /**
     * 获取资源图片
     *
     * @param resId 图片资源id
     * @return 资源图片
     */
    @Deprecated("")
    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return EngineConfig.app.getDrawable(resId)
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param resId   资源id
     * @return 字符串
     */
    fun getString(context: Context, @StringRes resId: Int): String {
        return context.resources.getString(resId)
    }

    /**
     * 获取ColorStateList值
     *
     * @param context 上下文
     * @param resId   资源id
     * @return ColorStateList值
     */
    fun getColors(context: Context, @ColorRes resId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context, resId)
    }

    /**
     * 获取ColorStateList属性（兼容?attr属性）
     *
     * @param context        上下文
     * @param typedArray     样式属性数组
     * @param styleableResId 样式资源ID
     * @return ColorStateList
     */
    fun getColorStateListAttrRes(
        context: Context?,
        typedArray: TypedArray,
        @StyleableRes styleableResId: Int
    ): ColorStateList? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return typedArray.getColorStateList(styleableResId)
        } else {
            val resourceId = typedArray.getResourceId(styleableResId, -1)
            if (resourceId != -1) {
                return AppCompatResources.getColorStateList(context!!, resourceId)
            }
        }
        return null
    }
}