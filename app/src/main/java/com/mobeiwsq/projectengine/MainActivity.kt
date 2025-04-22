package com.mobeiwsq.projectengine

import android.os.Bundle
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.core.openPage


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