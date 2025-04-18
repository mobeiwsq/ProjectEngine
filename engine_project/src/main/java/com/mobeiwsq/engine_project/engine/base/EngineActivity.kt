package com.mobeiwsq.engine_project.engine.base

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.engine.core.*
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.KeyBoardUtils.hideSoftInPut
import com.mobeiwsq.engine_project.utils.KeyBoardUtils.isShouldHideInput
import com.mobeiwsq.engine_project.utils.KeyBoardUtils.onTouchDown
import java.lang.ref.WeakReference

/**
 * Activity基类
 *
 * @author : mobeiwsq
 * @since :  2025/4/2 17:48
 */

open class EngineActivity(@LayoutRes contentLayoutId: Int = 0) :
    AppCompatActivity(contentLayoutId), SwitcherListener {
    private val layoutResID = contentLayoutId

    lateinit var rootView: View

    /**
    ◦ 当前activity的引用
     */
    var mCurrentActivity: WeakReference<EngineActivity>? = null


    companion object {
        /**
        ▪ 应用中所有XPageActivity的引用
         */
        val sActivities: ArrayList<WeakReference<EngineActivity>> = ArrayList<WeakReference<EngineActivity>>()

        /**
        ▪ 返回最上层的activity实例
         */
        fun getTopActivity(): EngineActivity? {
            val size = sActivities.size
            if (size >= 1) {
                val ref: WeakReference<EngineActivity> = sActivities[size - 1]
                return ref.get()
            }
            return null
        }
    }

    open fun initRootView(layoutResID: Int) {
        rootView = layoutInflater.inflate(layoutResID, null)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutResID != 0) {
            initRootView(layoutResID)
        } else {
            rootView = getBaseLayout()
        }

        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount == 1) {
                safelyFinishActivity(this@EngineActivity, false)
            } else {
                supportFragmentManager.popBackStack()
            }
        }


        // 设置 FrameLayout 为内容视图
        setContentView(rootView)

        if (getIsAddActivityToStack()) {
            //当前activity弱引用
            mCurrentActivity = WeakReference(this)
            //当前activity增加到activity列表中
            mCurrentActivity?.let {
                sActivities.add(it)
            }
            //打印所有activity情况
            printAllActivities()
        }

        //处理新跳转的activity
        initNewIntent(intent)
    }

    /**
     * 打印，调试用
     */
    fun printAllActivities() {
        PageLog.d("------------XPageActivity print all------------activities size:" + sActivities.size)
        for (ref in sActivities) {
            val item: EngineActivity? = ref.get()
            if (item != null) {
                PageLog.d(item.toString())
            }
        }
    }

    /**
     * 获取是否将activity添加到堆栈中
     *
     * @return `true` :添加<br></br> `false` : 不添加
     */
    open fun getIsAddActivityToStack(): Boolean {
        return true
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
        if (activeFragment == null) {
            if (ev?.action == KeyEvent.ACTION_DOWN) {
                if (isShouldHideInput(window, ev)) {
                    currentFocus?.let { view ->
                        hideSoftInPut(view)
                    }
                }
            }
        } else {
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

    override fun openPageForResult(
        pageInfo: PageInfo,
        resultCode: Int,
        newActivity: Boolean,
        bundle: Bundle,
        fragment: EngineFragment<*>
    ): Fragment? {
        if (newActivity) {

        } else {
            val animations = convertAnimations(pageInfo.anim)
            // 要获取返回数据时，addToBackStack默认为true
            val frg =
                openPageWithNewFragmentManager(supportFragmentManager, pageInfo.name, bundle, animations, true)
            frg?.let {
                val opener = fragment
                frg.setFragmentFinishListener(object : EngineFragment.OnFragmentFinishListener {
                    override fun onFragmentResult(resultCode: Int, intent: Intent) {
                        opener.onFragmentResult(resultCode, intent)
                    }
                })
            }
            return frg
        }
        return null
    }
}