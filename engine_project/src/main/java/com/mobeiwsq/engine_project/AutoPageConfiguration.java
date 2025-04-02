package com.mobeiwsq.engine_project;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.mobeiwsq.annotation.model.PageInfo;
import com.mobeiwsq.engine_project.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AutoPageConfiguration implements PageConfiguration {

    private static String TAG = "swn_mm";

    /**
     * 页面配置所在的包名
     */
    private static final String PAGE_CONFIG_PACKAGE_NAME = "com.mobeiwsq.page.config";

    /**
     * 页面配置生成类的类后缀名
     */
    private static final String PAGE_CONFIG_CLASS_NAME_SUFFIX = "PageConfig";

    @Override
    public List<PageInfo> registerPages(Context context) {
        List<PageInfo> pageInfos = new ArrayList<>();
        Set<String> classSet = null;
        try {
            classSet = ClassUtils.getClassNames(context, PAGE_CONFIG_PACKAGE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (classSet != null) {
            for (String className : classSet) {
                if (className != null && className.endsWith(PAGE_CONFIG_CLASS_NAME_SUFFIX)) {
                    try {
                        pageInfos.addAll(getPagesByClass(Class.forName(className)));
                    } catch (Exception e) {
                        Log.d(TAG, "registerPages: "+e);
                    }
                }
            }
        }
        return pageInfos;
    }

    private List<PageInfo> getPagesByClass(Class<?> clazz) throws Exception {
        // 获取单例对象
        Method getInstanceMethod = clazz.getDeclaredMethod("getInstance");
        getInstanceMethod.setAccessible(true);
        Object instance = getInstanceMethod.invoke(null);
        // 获取页面信息
        Method getPagesMethod = clazz.getDeclaredMethod("getPages");
        getPagesMethod.setAccessible(true);
        return (List<PageInfo>) getPagesMethod.invoke(instance);
    }
}