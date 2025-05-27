package com.mobeiwsq.engine_project.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.*;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import com.mobeiwsq.engine_project.R;
import com.mobeiwsq.engine_project.view.dialog.GravityEnum;

public class ThemeUtils {

    /**
     * 获取主题色
     *
     * @param context 上下文
     * @return 主题色
     */
    @ColorInt
    public static int getMainThemeColor(Context context) {
        return resolveColor(context, R.attr.colorAccent, getColor(context, R.color.config_color_main_theme));
    }

    @ColorInt
    public static int getColor(Context context, @ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
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

    public static int resolveDimension(Context context, @AttrRes int attr) {
        return resolveDimension(context, attr, -1);
    }

    public static int resolveDimension(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getDimensionPixelSize(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static void setBackgroundCompat(View view, Drawable d) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(d);
        } else {
            view.setBackground(d);
        }
    }

    public static GravityEnum resolveGravityEnum(
            Context context, @AttrRes int attr, GravityEnum defaultGravity) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            switch (a.getInt(0, gravityEnumToAttrInt(defaultGravity))) {
                case 1:
                    return GravityEnum.CENTER;
                case 2:
                    return GravityEnum.END;
                default:
                    return GravityEnum.START;
            }
        } finally {
            a.recycle();
        }
    }

    private static int gravityEnumToAttrInt(GravityEnum value) {
        switch (value) {
            case CENTER:
                return 1;
            case END:
                return 2;
            default:
                return 0;
        }
    }

    public static String resolveString(Context context, @AttrRes int attr, String defaultValue) {
        TypedValue v = new TypedValue();
        context.getTheme().resolveAttribute(attr, v, true);
        String value = (String) v.string;
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    public static ColorStateList getActionTextStateList(Context context, int newPrimaryColor) {
        final int fallBackButtonColor =
                ThemeUtils.resolveColor(context, android.R.attr.textColorPrimary);
        if (newPrimaryColor == 0) {
            newPrimaryColor = fallBackButtonColor;
        }
        int[][] states =
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, // disabled
                        new int[]{} // enabled
                };
        int[] colors = new int[]{ThemeUtils.adjustAlpha(newPrimaryColor, 0.4f), newPrimaryColor};
        return new ColorStateList(states, colors);
    }

    @ColorInt
    public static int adjustAlpha(
            @ColorInt int color, @SuppressWarnings("SameParameterValue") float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @ColorInt
    public static int getDisabledColor(Context context) {
        final int primaryColor = resolveColor(context, android.R.attr.textColorPrimary);
        final int disabledColor = isColorDark(primaryColor) ? Color.BLACK : Color.WHITE;
        return adjustAlpha(disabledColor, 0.3f);
    }

    public static boolean isColorDark(@ColorInt int color) {
        double darkness =
                1
                        - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color))
                        / 255;
        return darkness >= 0.5;
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

    // Get the specified color resource, creating a ColorStateList if the resource
    // points to a color value.
    public static ColorStateList getActionTextColorStateList(Context context, @ColorRes int colorId) {
        final TypedValue value = new TypedValue();
        context.getResources().getValue(colorId, value, true);
        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return getActionTextStateList(context, value.data);
        } else {

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                //noinspection deprecation
                return context.getResources().getColorStateList(colorId);
            } else {
                return context.getColorStateList(colorId);
            }
        }
    }

    // Try to resolve the colorAttr attribute.
    public static ColorStateList resolveActionTextColorStateList(
            Context context, @AttrRes int colorAttr, ColorStateList fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{colorAttr});
        try {
            final TypedValue value = a.peekValue(0);
            if (value == null) {
                return fallback;
            }
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT
                    && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                return getActionTextStateList(context, value.data);
            } else {
                final ColorStateList stateList = a.getColorStateList(0);
                if (stateList != null) {
                    return stateList;
                } else {
                    return fallback;
                }
            }
        } finally {
            a.recycle();
        }
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr, boolean fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getBoolean(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr) {
        return resolveBoolean(context, attr, false);
    }

    public static <T> boolean isIn(@NonNull T find, @Nullable T[] ary) {
        if (ary == null || ary.length == 0) {
            return false;
        }
        for (T item : ary) {
            if (item.equals(find)) {
                return true;
            }
        }
        return false;
    }

    public static float resolveFloat(Context context, int attrRes, float defaultValue) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrRes});
        try {
            return a.getFloat(0, defaultValue);
        } finally {
            a.recycle();
        }
    }

    public static int getColorFromAttrRes(int attrRes, int defaultValue, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrRes});
        try {
            return a.getColor(0, defaultValue);
        } finally {
            a.recycle();
        }
    }
    public static int resolveInt(Context context, int attrRes, int defaultValue) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrRes});
        try {
            return a.getInt(0, defaultValue);
        } finally {
            a.recycle();
        }
    }

}
