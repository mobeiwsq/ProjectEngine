package com.mobeiwsq.projectengine

import android.app.Application
import com.mobeiwsq.engine_project.EngineConfig

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        EngineConfig.initialize(this) {
            setDebug(true)
        }
    }
}