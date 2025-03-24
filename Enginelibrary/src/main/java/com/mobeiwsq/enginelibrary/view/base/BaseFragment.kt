package com.mobeiwsq.enginelibrary.view.base

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobeiwsq.enginelibrary.view.utils.KeyBoardUtils

/**
 * @ClassName : BaseFragment
 * @Description : fragment基类
 * @Author : mobeiwsq
 * @Date: 2025/3/24  14:34
 */
abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId), OnClickListener {

    val binding: B
        get() {
            return DataBindingUtil.bind(requireView())!!
        }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
    override fun onClick(v: View) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DataBindingUtil.bind<B>(view)

        try {
            initView()
            initData()
            initListener()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    @Deprecated("建议使用onBackPressedDispatcher", ReplaceWith("requireActivity().onBackPressedDispatcher"))
    open fun onBackPressed(): Boolean {
        return false
    }

    /**
     * 将Activity中dispatchTouchEvent在Fragment中实现，
     *
     * @param ev 点击事件
     * @return 是否处理
     */
    fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            onTouchDownAction(ev)
        }
        return false
    }

    /**
     * 处理向下点击事件【默认在这里做隐藏输入框的处理，不想处理的话，可以重写该方法】
     *
     * @param ev 点击事件
     */

    open fun onTouchDownAction(ev: MotionEvent){
        if (activity == null){
            return
        }
        if (KeyBoardUtils.isShouldHideInput(activity!!.window,ev)){
            hideCurrentPageSoftInput()
        }
    }

    /**
     * 隐藏当前页面弹起的输入框【可以重写这里自定义自己隐藏输入框的方法】
     */
    fun hideCurrentPageSoftInput(){
        if (activity == null){
            return
        }
        KeyBoardUtils.hideSoftInput(activity!!.currentFocus)
    }

}