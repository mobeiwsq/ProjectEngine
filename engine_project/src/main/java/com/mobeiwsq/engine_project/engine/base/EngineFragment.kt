package com.mobeiwsq.engine_project.engine.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobeiwsq.engine_project.engine.core.SwitcherListener
import com.mobeiwsq.engine_project.engine.core.createPageInfo
import com.mobeiwsq.engine_project.engine.core.popToBack
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.KeyBoardUtils.onTouchDown
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.engine_project.view.title.TitleBarUtils
import java.lang.ref.WeakReference

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
                onTouchDown(event, activity)
            }
        }
        return false
    }

    open fun onFragmentResult(resultCode: Int, intent: Intent) {
        PageLog.d("onFragmentResult for EngineFragment resultCode : $resultCode")
    }

    /**
     * 页面跳转返回的监听接口
     */
    interface OnFragmentFinishListener {
        /**
         * 页面跳转返回的回调接口
         *
         * @param requestCode 请求码
         * @param resultCode  结果码
         * @param intent      返回的数据
         */
        fun onFragmentResult(resultCode: Int, intent: Intent)
    }

    /**
     * 页面跳转返回的监听接口
     */
    var mFragmentFinishListener: OnFragmentFinishListener? = null

    /**
     * 设置该接口用于返回结果
     *
     * @param listener OnFragmentFinishListener对象
     */
    fun setFragmentFinishListener(listener: OnFragmentFinishListener) {
        mFragmentFinishListener = listener
    }

    /**
     * 设置openPageForResult打开的页面的返回结果
     *
     * @param resultCode 返回结果码
     * @param intent     返回的intent对象
     */
    fun setFragmentResult(resultCode: Int, intent: Intent) {
        mFragmentFinishListener?.let {
            it.onFragmentResult(resultCode, intent)
        }
    }


    /**
     * 打开fragment并请求获得返回值,并设置是否在新activity中打开
     *
     * @param newActivity 是否新开activity
     * @param bundle      参数
     * @param resultCode 返回码
     * @return 打开的fragment对象
     */
    fun openPageForResult(
        clazz: Class<*>,
        resultCode: Int,
        newActivity: Boolean = false,
        bundle: Bundle = Bundle()
    ) {
        val pageCoreSwitcher = getSwitcher()
        if (pageCoreSwitcher != null) {
            val pageInfo = createPageInfo(clazz)
            pageCoreSwitcher.openPageForResult(pageInfo, resultCode, newActivity, bundle, this)
        } else {
            PageLog.d("pageSwitcher is null")
            return
        }
    }

    /**
     * openPageForResult接口，用于传递返回结果
     */
    private var mPageCoreSwitcher: SwitcherListener? = null

    /**
     * 所在activity
     */
    private val mAttachContext: WeakReference<Context>? = null

    /**
     * 得到页面切换Switcher
     *
     * @return 页面切换Switcher
     */
    fun getSwitcher(): SwitcherListener? {
        synchronized(this@EngineFragment) {
            // 加强保护，保证pageSwitcher 不为null
            if (mPageCoreSwitcher == null) {
                val context: Context? = getAttachContext()
                if (context is SwitcherListener) {
                    mPageCoreSwitcher = context
                }
                if (mPageCoreSwitcher == null) {
                    val topActivity: EngineActivity? = EngineActivity.getTopActivity()
                    PageLog.d("getSwitcher------$topActivity")
                    if (topActivity != null) {
                        mPageCoreSwitcher = topActivity
                    }
                }
            }
        }
        return mPageCoreSwitcher
    }

    fun getAttachContext(): Context? {
        if (mAttachContext != null) {
            return mAttachContext.get()
        }
        return null
    }

    override fun onDestroyView() {
        mFragmentFinishListener = null
        super.onDestroyView()
    }

}