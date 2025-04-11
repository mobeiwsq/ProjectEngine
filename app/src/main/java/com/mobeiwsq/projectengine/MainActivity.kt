package com.mobeiwsq.projectengine

import android.os.Bundle
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.core.openPage


class MainActivity : EngineActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(MainFragment::class.java)
    }

}