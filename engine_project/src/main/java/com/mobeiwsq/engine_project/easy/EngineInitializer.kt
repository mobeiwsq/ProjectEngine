package com.mobeiwsq.engine_project.easy

import android.content.Context
import androidx.startup.Initializer

/**
 * AppStartup默认初始化
 *
 * @author : mobeiwsq
 * @since :  2025/5/20 16:37
 */
class EngineInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        app = context
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}