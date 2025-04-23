
package com.mobeiwsq.engine_project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * 组件工具类
 *
 */
public final class WidgetUtils {

    /**
     * 根据上下文获取Activity
     *
     * @param context 上下文
     * @return Activity
     */
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        }
        return null;
    }

}
