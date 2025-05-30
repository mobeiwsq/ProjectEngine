package com.mobeiwsq.engine_project.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import androidx.annotation.*;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Utils {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

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
    public static int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param spValue SP值
     * @return 像素值
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
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

    /**
     * View设备背景
     *
     * @param context 上下文
     * @param view    控件
     * @param resId   资源id
     */
    public static void setBackground(Context context, View view, int resId) {
        if (view == null) {
            return;
        }
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
        view.setBackground(bd);
    }

    /**
     * 释放图片资源
     *
     * @param view 控件
     */
    public static void recycleBackground(View view) {
        Drawable d = view.getBackground();
        //别忘了把背景设为null，避免onDraw刷新背景时候出现used a recycled bitmap错误
        view.setBackgroundResource(0);
        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) d).getBitmap();
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
            }
        }
        if (d != null) {
            d.setCallback(null);
        }
    }

    /**
     * 检查是否为空指针
     *
     * @param object
     * @param hint
     */
    public static void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    public static boolean isNullOrEmpty(@Nullable CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 计算状态栏高度 getStatusBarHeight
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return getStatusBarHeight();
        }
        return getInternalDimensionSize(context.getResources(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    /**
     * 计算状态栏高度 getStatusBarHeight
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("you should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("you should init first");
    }

    /**
     * 类型强转
     *
     * @param object 需要强转的对象
     * @param clazz  需要强转的类型
     * @param <T>
     * @return 类型强转结果
     */
    public static <T> T cast(final Object object, Class<T> clazz) {
        return clazz != null && clazz.isInstance(object) ? (T) object : null;
    }
}
