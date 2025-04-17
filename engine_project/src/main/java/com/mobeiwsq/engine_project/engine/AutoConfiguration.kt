package com.mobeiwsq.engine_project.engine

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.annotation.utils.GsonUtils
import com.mobeiwsq.engine_project.EngineConfig
import com.mobeiwsq.engine_project.engine.core.SwitcherInfo
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.ClassUtils
import java.lang.reflect.Method

/**
 * 初始化时页面配置相关
 *
 * @author : mobeiwsq
 * @since :  2025/4/8 14:17
 */

class AutoConfiguration {

    fun registerPages(context: Context) {
        var classSet: Set<String>? = null
        try {
            classSet = ClassUtils.getClassNames(context, EngineConfig.PAGE_CONFIG_PACKAGE_NAME)
        } catch (e: Exception) {
            PageLog.e(e)
        }
        classSet?.let {
            EngineConfig.pageInfos.clear()
            for (className in classSet) {
                if (className.endsWith(EngineConfig.PAGE_CONFIG_CLASS_NAME_SUFFIX)) {
                    try {
                        val pageInfo = getPagesByClass(Class.forName(className))
                        EngineConfig.pageInfos.addAll(pageInfo)
                    } catch (e: Exception) {
                        PageLog.e(e)
                    }
                }
            }

            PageLog.d("pageInfos-----${EngineConfig.pageInfos}")
        }
        val gson = Gson()
        val pageJson = gson.toJson(EngineConfig.pageInfos)
        val pages: List<SwitcherInfo> = GsonUtils.fromJson(pageJson, object : TypeToken<List<SwitcherInfo?>?>() {
        }.type)
        PageLog.d("pageInfos  pages-----${pages}")

        for (page in pages) {
            if (TextUtils.isEmpty(page.mName) && TextUtils.isEmpty(page.mClazz)) {
                continue
            }
            EngineConfig.mPageMap[page.mName] = page
        }
    }

    private fun getPagesByClass(clazz: Class<*>): List<PageInfo> {
        return try {
            // 获取单例对象
            val getInstanceMethod: Method = clazz.getDeclaredMethod("getInstance")
            getInstanceMethod.isAccessible = true
            val instance = getInstanceMethod.invoke(null)

            // 获取页面信息
            val getPagesMethod: Method = clazz.getDeclaredMethod("getPages")
            getPagesMethod.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            getPagesMethod.invoke(instance) as List<PageInfo>
        } catch (e: Exception) {
            throw Exception("Error getting pages by class", e)
        }
    }

    /**
     * 调试日志输出
     */
    fun setDebug(isDebug: Boolean) {
        PageLog.debug(isDebug)
    }
}