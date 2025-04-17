package com.mobeiwsq.projectengine

import android.util.Log
import com.mobeiwsq.engine_project.engine.base.EngineDataBindActivity
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding


class MainActivity : EngineDataBindActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
        Log.d("AAAAAA", "Button reference: ${binding.button}")
        binding.button.setOnClickListener {
            Log.d("AAAAAA", "initListeners: 11111111")
        }
    }

}