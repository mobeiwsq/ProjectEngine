package com.mobeiwsq.projectengine

import android.os.Bundle
import android.util.Log
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.base.EngineDataBindActivity
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding


//class MainActivity : EngineDataBindActivity<ActivityMainBinding>(R.layout.activity_main) {
//
//    override fun initView() {
//    }
//
//    override fun initData() {
//    }
//
//    override fun initListeners() {
//        Log.d("AAAAAA", "Button reference: ${binding.button}")
//        binding.button.setOnClickListener {
//            Log.d("AAAAAA", "initListeners: 11111111")
//        }
//    }
//
//}

class MainActivity : EngineActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(MainFragment::class.java)
    }
}