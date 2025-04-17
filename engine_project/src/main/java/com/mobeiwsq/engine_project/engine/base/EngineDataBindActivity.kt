package com.mobeiwsq.engine_project.engine.base

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class EngineDataBindActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    EngineActivity(contentLayoutId) {

    lateinit var binding: B
    override fun initRootView(layoutResID: Int) {
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        init()
        rootView = binding.root
    }

    private fun init() {
        try {
            initView()
            initData()
            initListeners()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListeners()

}