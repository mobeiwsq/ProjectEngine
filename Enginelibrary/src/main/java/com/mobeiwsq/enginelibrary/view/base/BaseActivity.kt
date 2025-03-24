package com.mobeiwsq.enginelibrary.view.base

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobeiwsq.enginelibrary.view.utils.KeyBoardUtils

/**
 * @ClassName : BaseActivity
 * @Description : activity基类
 * @Author : mobeiwsq
 * @Date: 2025/3/24  14:32
 */
abstract class BaseActivity <B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    FinishBroadcastActivity(contentLayoutId), OnClickListener {

    lateinit var binding: B
    lateinit var rootView: View

    private var onTouchEvent: (MotionEvent.() -> Boolean)? = null

    override fun setContentView(layoutResId: Int) {
        rootView = layoutInflater.inflate(layoutResId, null)
        setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)!!
        init()
    }

    open fun init() {
        try {
            initView()
            initData()
            initListener()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
    override fun onClick(v: View) {}


    /**
     * 触摸事件
     * @param block 返回值表示是否拦截事件
     */
    fun onTouchEvent(block: MotionEvent.() -> Boolean) {
        onTouchEvent = block
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val activityFragment: BaseFragment<*>? = getActivityFragment<B>()
        var isHandled = false
        if (activityFragment!=null){
            isHandled = activityFragment.dispatchTouchEvent(ev)
        } else{
            if (ev.action == MotionEvent.ACTION_DOWN) {
                onTouchDownAction(ev)
            }
        }

        return isHandled || super.dispatchTouchEvent(ev)
    }

    /**
     * 处理向下点击事件【默认在这里做隐藏输入框的处理，不想处理的话，可以重写该方法】
     *
     * @param ev 点击事件
     */

    open fun onTouchDownAction(ev: MotionEvent){
        if (KeyBoardUtils.isShouldHideInput(window,ev)){
            hideCurrentPageSoftInput()
        }
    }

    /**
     * 隐藏当前页面弹起的输入框【可以重写这里自定义自己隐藏输入框的方法】
     */
    fun hideCurrentPageSoftInput(){
        KeyBoardUtils.hideSoftInput(currentFocus)
    }

    fun requireActivity(): AppCompatActivity {
        return this
    }

    fun <B : ViewDataBinding> getActivityFragment(): BaseFragment<*>? {
        if (this.isFinishing){
            return null
        }
        val manager = supportFragmentManager
        val count =manager.backStackEntryCount
        if (count>0){
            val tag = manager.getBackStackEntryAt(count-1).name
            return manager.findFragmentByTag(tag) as? BaseFragment<*>
        }
        return null
    }


    /**
     * 关闭界面
     */
    fun finishTransition() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            super.finish()
        }
    }

}