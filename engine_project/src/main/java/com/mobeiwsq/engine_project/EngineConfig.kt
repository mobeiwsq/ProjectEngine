package com.mobeiwsq.engine_project

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.engine.AutoConfiguration
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.core.SwitcherInfo
import io.github.inflationx.calligraphy3.TypefaceUtils

/**
 * Engine初始化类
 *
 * @author : mobeiwsq
 * @since :  2025/4/8 11:42
 */

@SuppressLint("StaticFieldLeak")
object EngineConfig {

    private var sDefaultFontAssetPath = ""

    /**
     * 全局applicationContext
     */
    lateinit var app: Context

    /**
     * 默认容器activity
     */
    var mContainActivityClassName: String? = EngineActivity::class.java.canonicalName

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

    fun init(context: Application){
        app = context
    }

    /**
     * @return 默认字体的存储位置
     */
    fun getDefaultFontAssetPath(): String {
        return sDefaultFontAssetPath
    }

    /**
     * @return 设置默认字体的位置
     */
    fun setDefaultFontAssetPath(path: String) {
        sDefaultFontAssetPath = path
    }

    /**
     * @param fontPath 字体路径
     * @return 获取默认字体
     */
    fun getDefaultTypeface(fontPath: String?): Typeface? {
        var fontPath1 = fontPath
        if (TextUtils.isEmpty(fontPath)) {
            fontPath1 = sDefaultFontAssetPath
        }
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(app.assets, fontPath)
        }
        return null
    }

}