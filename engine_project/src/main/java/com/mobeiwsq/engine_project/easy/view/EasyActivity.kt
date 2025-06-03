package com.mobeiwsq.engine_project.easy.view

import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobei.engine.utils.KeyBoardUtils.hideSoftInPut
import com.mobei.engine.utils.KeyBoardUtils.isShouldHideInput
import com.mobeiwsq.engine_project.easy.utils.immersive

/**
 * activity基类
 *
 * @author : mobeiwsq
 * @since :  2025/5/21 09:34
 */
abstract class EasyActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    EasyFinishBroadcastActivity(contentLayoutId) {
    lateinit var binding: B
    lateinit var rootView: View

    //    // 默认触摸事件
    private var onTouchEvent: (MotionEvent.() -> Boolean)? = {
        val activeFragment = getActiveFragment()
        var isHandled = false
        if (activeFragment == null) {
            if (action == KeyEvent.ACTION_DOWN) {
                if (isShouldHideInput(window, this)) {
                    window.currentFocus?.let { view ->
                        hideSoftInPut(view)
                    }
                }
            }
        } else {
            isHandled = activeFragment.dispatchTouchEvent(this)
        }

        isHandled || super.dispatchTouchEvent(this)
    }


    override fun setContentView(layoutResId: Int) {
        rootView = layoutInflater.inflate(layoutResId, null)
        super.setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)
            ?: throw IllegalStateException("DataBinding绑定失败，请检查布局文件是否正确")
        initStatusBar()
        init()
    }

    // 透明状态栏
    open fun initStatusBar() {
        immersive()
    }

    open fun init() {
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


    /**
     * 关闭界面
     */
    fun finishTransition() {
        finishAfterTransition()
    }

    /**
     * 触摸事件
     * @param block 返回值表示是否拦截事件
     */
    fun onTouchEvent(block: MotionEvent.() -> Boolean) {
        onTouchEvent = block
    }

    /**
     * 触摸事件
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val b = super.dispatchTouchEvent(event)
        return onTouchEvent?.invoke(event) ?: b
    }

    /**
     * 获得当前活动fragment
     *
     * @return 当前活动Fragment对象
     */
    private fun getActiveFragment(): EasyFragment<*>? {
        if (isFinishing) {
            return null
        }
        val fragmentManager = supportFragmentManager
        fragmentManager.let {
            val count = fragmentManager.backStackEntryCount
            if (count > 0) {
                val tag = fragmentManager.getBackStackEntryAt(count - 1).name
                return fragmentManager.findFragmentByTag(tag) as EasyFragment<*>
            }
        }
        return null
    }
}