package com.mobeiwsq.engine_project.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.engine_project.view.title.TitleBarUtils

/**
* Fragment基类
*
* @author : mobeiwsq
* @since :  2025/4/8 15:41
*/

abstract class EngineFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {

        private val layoutRes = contentLayoutId

    /**
     * 页面名
     */
    var mPageName: String = ""

    protected lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        initTitleBar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        DataBindingUtil.bind<B>(view)
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

    /**
     * 初始化标题，可进行重写
     */
    open fun initTitleBar(): TitleBar? {
        return TitleBarUtils.addTitleBar(
           binding.root as ViewGroup, mPageName,
            View.OnClickListener {
                Log.d("initTitleBar", "initTitleBar: 11111111")
            })
    }
}