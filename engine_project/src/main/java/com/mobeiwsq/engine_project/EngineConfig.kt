package com.mobeiwsq.engine_project

import android.annotation.SuppressLint
import android.content.Context
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.core.SwitcherInfo
import com.mobeiwsq.engine_project.utils.Utils

/**
 * Engine初始化类
 *
 * @author : mobeiwsq
 * @since :  2025/4/8 11:42
 */

@SuppressLint("StaticFieldLeak")
object EngineConfig {

    /**
     * 全局applicationContext
     */
    lateinit var app: Context

    /**
     * 页面配置所在的包名
     */
    val PAGE_CONFIG_PACKAGE_NAME = "com.mobeiwsq.page.config"

    /**
     * 页面配置生成类的类后缀名
     */
    val PAGE_CONFIG_CLASS_NAME_SUFFIX = "PageConfig"

    /**
     * 页面信息list
     */
    val pageInfos = mutableListOf<PageInfo>()

    /**
     * 保存page的map
     */
    val mPageMap: MutableMap<String, SwitcherInfo> = HashMap<String, SwitcherInfo>()


    /**
     * 默认初始化
     */
    fun initialize(
        context: Context? = null,
        config: AutoConfiguration.() -> Unit = {}
    ) {
        context?.let { app = context }
        val auConfig = AutoConfiguration()
        auConfig.config()
        auConfig.registerPages(app)
    }

    /**
     * 获取页面信息,检测页面是否添加@Page修饰
     */
    fun getPage(clazz: Class<*>): Page {
        return Utils.checkNotNull(
            clazz.getAnnotation(Page::class.java),
            "Page == null，请检测页面是否漏加 @Page 进行修饰！"
        )
    }

}