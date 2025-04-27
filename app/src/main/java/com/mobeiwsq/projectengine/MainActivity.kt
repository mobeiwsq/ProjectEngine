package com.mobeiwsq.projectengine

import android.os.Bundle
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.projectengine.view.fragment.MainFragment

class MainActivity : EngineActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(MainFragment::class.java)
    }
}