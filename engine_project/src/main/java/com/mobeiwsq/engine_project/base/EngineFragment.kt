package com.mobeiwsq.engine_project.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobeiwsq.engine_project.logger.PageLog

/**
* Fragment基类
*
* @author : mobeiwsq
* @since :  2025/4/8 15:41
*/

abstract class EngineFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {

    /**
     * 页面名
     */
    var mPageName: String? = null

    val binding: B
        get() {
            return DataBindingUtil.bind(requireView())!!
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DataBindingUtil.bind<B>(view)
        PageLog.d("====Fragment.onCreated====$mPageName")

        try {
            initView()
            initData()
            initListeners()
        } catch (e: Exception) {
            Log.e("engine_project", "Engine init failure", e)
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListeners()
}