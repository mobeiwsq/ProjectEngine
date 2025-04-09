package com.mobeiwsq.engine_project.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.*;
import androidx.appcompat.content.res.AppCompatResources;

public class Utils {
    public static <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    public static int resolveDimension(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getDimensionPixelSize(0, fallback);
        } finally {
            a.recycle();
        }
    }

    /**
     * 获取dimes值，返回的是【4舍5入】的值
     *
     * @param resId
     * @return
     */
    public static int getDimensionPixelSize(Context context, @DimenRes int resId) {
        return getResources(context).getDimensionPixelSize(resId);
    }

    /**
     * 获取resources对象
     *
     * @return
     */
    private static Resources getResources(Context context) {
        return context.getResources();
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr) {
        return resolveBoolean(context, attr, false);
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr, boolean fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getBoolean(0, fallback);
        } finally {
            a.recycle();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = getResources(context).getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     *
     * @param context
     * @param typedArray
     * @param index
     * @return
     */
    public static Drawable getDrawableAttrRes(Context context, TypedArray typedArray, @StyleableRes int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return typedArray.getDrawable(index);
        } else {
            int resourceId = typedArray.getResourceId(index, -1);
            if (resourceId != -1) {
                return AppCompatResources.getDrawable(context, resourceId);
            }
        }
        return null;
    }

    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static Drawable resolveDrawable(Context context, @AttrRes int attr) {
        return resolveDrawable(context, attr, null);
    }

    public static Drawable resolveDrawable(
            Context context,
            @AttrRes int attr,
            @SuppressWarnings("SameParameterValue") Drawable fallback) {
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            Drawable drawable = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = array.getDrawable(0);
            } else {
                int id = array.getResourceId(0, -1);
                if (id != -1) {
                    drawable = AppCompatResources.getDrawable(context, id);
                }
            }
            if (drawable == null && fallback != null) {
                drawable = fallback;
            }
            return drawable;
        } finally {
            array.recycle();
        }
    }

    /**
     * 获取svg资源图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        return AppCompatResources.getDrawable(context, resId);
    }
}
