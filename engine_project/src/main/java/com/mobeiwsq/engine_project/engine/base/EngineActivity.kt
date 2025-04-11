package com.mobeiwsq.engine_project.engine.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.engine.core.SWITCHER_NEW_INTENT
import com.mobeiwsq.engine_project.engine.core.openPageWithNewFragmentManager
import com.mobeiwsq.engine_project.engine.core.safelyFinishActivity
import com.mobeiwsq.engine_project.logger.PageLog

/**
 * Activity基类
 *
 * @author : mobeiwsq
 * @since :  2025/4/2 17:48
 */

open class EngineActivity(@LayoutRes contentLayoutId: Int = 0) :
    AppCompatActivity(contentLayoutId) {
    private val layoutResID = contentLayoutId

    lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutResID != 0) {
            rootView = layoutInflater.inflate(layoutResID, null)
        } else {
            rootView = getBaseLayout()
        }



        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount == 1) {
                safelyFinishActivity(this@EngineActivity, false)
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }


        // 设置 FrameLayout 为内容视图
        setContentView(rootView)

        //处理新跳转的activity
        initNewIntent(intent)
    }

    /**
     * 获取新跳转的activity的数据
     */
    private fun initNewIntent(intent: Intent) {
        try {
            val pageInfo = intent.getParcelableExtra(SWITCHER_NEW_INTENT, PageInfo::class.java)
            if (pageInfo != null) {
                openPageWithNewFragmentManager(
                    supportFragmentManager, pageName = pageInfo.name, animations = null, bundle = Bundle(),
                    addToBackStack = true,
                )
            }

        } catch (e: Exception) {
            PageLog.e(e)
            finish()
        }


    }

    /**
     * 设置根布局
     *
     * @return 根布局
     */
    protected fun getBaseLayout(): View {
        val baseLayout = FrameLayout(this)
        val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        baseLayout.id = R.id.fragment_container
        baseLayout.layoutParams = params
        return baseLayout
    }


    /**
     *onKeyDown事件，如果fragment中处理了则执行fragment中的
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val activeFragment = getActiveFragment()
        var isHandled = false
        activeFragment?.let {
            isHandled = activeFragment.onKeyDown(keyCode, event)
        }
        return isHandled || super.onKeyDown(keyCode, event)
    }


    /**
     * 触摸事件，如果fragment中处理了则执行fragment中的
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val activeFragment = getActiveFragment()
        var isHandled = false
        activeFragment?.let {
            isHandled = activeFragment.dispatchTouchEvent(ev)
        }
        return isHandled || super.dispatchTouchEvent(ev)
    }

    /**
     * 获得当前活动fragment
     *
     * @return 当前活动Fragment对象
     */
    private fun getActiveFragment(): EngineFragment<*>? {
        if (isFinishing) {
            return null
        }
        val fragmentManager = supportFragmentManager
        fragmentManager?.let {
            val count = fragmentManager.backStackEntryCount
            if (count > 0) {
                val tag = fragmentManager.getBackStackEntryAt(count - 1).name
                return fragmentManager.findFragmentByTag(tag) as EngineFragment<*>
            }
        }
        return null
    }
}