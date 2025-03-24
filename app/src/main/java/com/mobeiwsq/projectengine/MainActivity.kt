package com.mobeiwsq.projectengine

import android.view.MotionEvent
import com.mobeiwsq.enginelibrary.view.base.BaseActivity
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}