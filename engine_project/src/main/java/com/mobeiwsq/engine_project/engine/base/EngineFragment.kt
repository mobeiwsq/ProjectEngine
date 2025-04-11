package com.mobeiwsq.engine_project.engine.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobeiwsq.engine_project.engine.core.popToBack
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.KeyBoardUtils.onTouchDown
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
                popToBack()
            })
    }

    /**
     *将Activity中onKeyDown在Fragment中实现，
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    /**
     * 将Activity中dispatchTouchEvent在Fragment中实现，
     *
     * @param event 点击事件
     * @return 是否处理
     */
    open fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == KeyEvent.ACTION_DOWN) {
                onTouchDown(event,activity)
            }
        }
        return false
    }

    open fun onFragmentResult(resultCode:Int,intent: Intent){
        PageLog.d("onFragmentResult for EngineFragment resultCode : $resultCode")
    }

}