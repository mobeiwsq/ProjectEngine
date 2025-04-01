package com.mobeiwsq.engine_project;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Author: YFS(893145181@qq.com)
 * Time:2022/7/12 11:41
 */
public class MButterKnife {
    /**
     * 生成类的类名结尾：例如MainActivity 生成 MainActivity_ViewBinding
     */
    public static final String TAG_NAME = "_ViewBinding";

    public static void bind(Activity activity) {
        createBindClassInstance(activity).initView(activity, activity);
    }


    /**
     * 根据生成的全类名，反射创建对象
     *
     * @param object
     * @return
     */
    private static IViewInjector createBindClassInstance(Object object) {
        String className = object.getClass().getName() + TAG_NAME;
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor(object.getClass());
//               return (IViewInjector) clazz.newInstance();
            return (IViewInjector) constructor.newInstance(object);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {

            e.printStackTrace();
        }
        return null;
    }


}
