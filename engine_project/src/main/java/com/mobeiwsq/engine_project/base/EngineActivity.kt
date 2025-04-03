package com.mobeiwsq.engine_project.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.core.CorePageManager
import com.mobeiwsq.engine_project.core.CoreSwitchBean
import com.mobeiwsq.engine_project.core.CoreSwitcher
import com.mobeiwsq.engine_project.logger.PageLog
import java.lang.ref.WeakReference

/**
 * Activity基类
 *
 * @author : mobeiwsq
 * @since :  2025/4/2 17:48
 */

open class EngineActivity(contentLayoutId: Int = 0) : AppCompatActivity(contentLayoutId), CoreSwitcher {

    /**
     * 应用中所有XPageActivity的引用
     */
    private val sActivities: ArrayList<WeakReference<EngineActivity>?> = ArrayList()

    /**
     * 当前activity的引用
     */
    private var mCurrentActivity: WeakReference<EngineActivity>? = null

    /**
     * 记录首个CoreSwitchBean，用于页面切换
     */
    private var mFirstCoreSwitchBean: CoreSwitchBean? = null

    /**
     * 主线程Handler
     */
    private var mHandler: Handler? = null

    /**
     * ForResult 的fragment
     */
    private var mFragmentForResult: XPageFragment? = null

    /**
     * 请求码，必须大于等于0
     */
    private var mFragmentRequestCode = -1


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView()
        // 处理新开activity的情况
        if (savedInstanceState != null) {
            //恢复数据，需要用注解SaveWithActivity
            // TODO 待定
//            loadActivitySavedData(savedInstanceState)
        }

        // 获取主线程handler
        mHandler = Handler(mainLooper)

        if (getIsAddActivityToStack()) {
            mCurrentActivity = WeakReference<EngineActivity>(this)
            //当前activity增加到activity列表中
            sActivities.add(mCurrentActivity)
            //打印所有activity情况
            printAllActivities()
        }

        // 处理新开的activity的跳转
        init(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 退出当前页面，如果只有一个fragment时会关闭activity
     */
    override fun popPage() {
        popOrFinishActivity()
    }

    /**
     * 是否位于栈顶
     *
     * @param fragmentTag fragment的tag
     * @return 指定Fragment是否位于栈顶
     */
    override fun isFragmentTop(fragmentTag: String?): Boolean {
        val size = sActivities.size
        if (size > 0) {
            val reference = sActivities[size - 1]
            val activity = reference?.get()
            if (activity != null && activity == this) {
                val manager = activity.supportFragmentManager
                if (manager != null) {
                    val count = manager.backStackEntryCount
                    if (count >= 1) {
                        val enter: FragmentManager.BackStackEntry = manager.getBackStackEntryAt(count - 1)
                        return fragmentTag.equals(enter.name, ignoreCase = true);
                    }
                }
            }
        }
        return false
    }

    /**
     * 查找fragment
     *
     * @param pageName page的名字
     * @return 是否找到对应Fragment
     */
    override fun findPage(pageName: String?): Boolean {
        val size = sActivities.size
        var hasFind = false
        for (j in size - 1 downTo 0) {
            val reference = sActivities[j]
            if (reference != null) {
                val activity = reference.get()
                if (activity == null) {
                    PageLog.d("item is null")
                    continue
                }
                val manager = activity.supportFragmentManager
                val count = manager.backStackEntryCount
                for (i in count - 1 downTo 0) {
                    val name = manager.getBackStackEntryAt(i).name
                    if (name != null && name.equals(pageName, ignoreCase = true)) {
                        hasFind = true
                        break
                    }
                }
                if (hasFind) {
                    break
                }
            }

        }
        return hasFind
    }

    /**
     * 弹出并用bundle刷新数据，在onFragmentDataReset中回调
     *
     * @param page page的名字
     * @return 跳转到对应的fragment的对象
     */
    override fun gotoPage(page: CoreSwitchBean?): Fragment? {
        if (page == null) {
            PageLog.e("page name is empty")
            return null
        }
        val pageName = page.pageName
        if (!findPage(pageName)) {
            PageLog.d("Be sure you have the right pageName$pageName")
            return openPage(page)
        }

        val size = sActivities.size
        for (i in size - 1 downTo 0) {
            val ref = sActivities[i]
            if (ref != null) {
                val item = ref.get()
                if (item == null) {
                    PageLog.d("item null")
                    continue
                }
                val findInActivity = popFragmentInActivity(pageName, page.bundle, item)
                if (findInActivity) {
                    break
                } else {
                    // 找不到就弹出
                    item.finish()
                }
            }
        }
        return null
    }


    /**
     * 根据SwitchBean打开一个新的fragment
     *
     * @param page CoreSwitchBean对象
     * @return 打开的Fragment对象
     */
    override fun openPage(page: CoreSwitchBean?): Fragment? {
        if (page == null) {
            return null
        }
        val addToBackStack = page.isAddToBackStack
        val newActivity = page.isNewActivity
        val bundle = page.bundle

        val animations = page.anim
        if (newActivity) {
            startActivity(page)
            return null
        } else {
            val pageName = page.pageName
            return CorePageManager.getInstance()
                .openPageWithNewFragmentManager(supportFragmentManager, pageName, bundle, animations, addToBackStack)
        }
    }


    /**
     * 根据SwitchBean切换fragment
     *
     * @param page CoreSwitchBean对象
     * @return 打开的Fragment对象
     */
    override fun changePage(page: CoreSwitchBean?): Fragment? {
        if (page == null) {
            return null
        }
        val addToBackStack = page.isAddToBackStack
        val newActivity = page.isNewActivity
        val bundle = page.bundle

        val animations = page.anim
        if (newActivity) {
            startActivity(intent)
            return null
        } else {
            val pageName = page.pageName
            return CorePageManager.getInstance()
                .openPageWithNewFragmentManager(supportFragmentManager, pageName, bundle, animations, addToBackStack)
        }
    }

    /**
     * 移除无用fragment
     *
     * @param fragmentLists 移除的fragment列表
     */
    override fun removeUnlessFragment(fragmentLists: MutableList<String>?) {
        if (isFinishing) {
            return
        }
        val manager = supportFragmentManager
        if (manager != null) {
            val transaction = manager.beginTransaction()
            if (fragmentLists != null) {
                for (tag in fragmentLists) {
                    val fragment = manager.findFragmentByTag(tag)
                    if (fragment != null) {
                        transaction.remove(fragment)
                    }
                }

                transaction.commitAllowingStateLoss()
                val count = manager.backStackEntryCount
                if (count == 0) {
                    finish()
                }

            }
        }
    }

    /**
     * 给BaseFragment调用
     *
     * @param page     CoreSwitchBean对象
     * @param fragment 要求返回结果的BaseFragment对象
     * @return 打开的fragment对象
     */
    override fun openPageForResult(page: CoreSwitchBean?, fragment: XPageFragment): Fragment? {
        if (page != null) {
            if (page.isNewActivity) {
                PageLog.d("openPageForResult start new activity-----" + fragment.pageName)
                mFragmentForResult = fragment
                mFragmentRequestCode = page.requestCode
                startActivityForResult(page)
                return null
            } else {
                val packageName = page.pageName
                val bundle = page.bundle
                val animations = page.anim
                val addToBackStack = page.isAddToBackStack
                val frg = CorePageManager.getInstance().openPageWithNewFragmentManager(
                    supportFragmentManager,
                    packageName,
                    bundle,
                    animations,
                    addToBackStack
                )
                if (frg == null) {
                    return null
                }
                val opener = fragment
                frg.requestCode = page.requestCode
                frg.setFragmentFinishListener { requestCode, resultCode, intent ->
                    opener.onFragmentResult(
                        requestCode,
                        resultCode,
                        intent
                    )
                }
                return frg
            }
        } else {
            PageLog.d("openPageForResult.SwitchBean is null")
        }
        return null
    }

    /**
     * @param page CoreSwitchBean对象
     */

    private fun startActivityForResult(page: CoreSwitchBean) {
        try {
            val intent = Intent(this, page.containActivityClazz)
            intent.putExtra(CoreSwitchBean.KEY_SWITCH_BEAN, page)
            intent.putExtra(CoreSwitchBean.KEY_START_ACTIVITY_FOR_RESULT, true)
            this.startActivityForResult(intent, page.requestCode)

            val animations = page.anim
            if (animations != null && animations.size >= 2) {
                // TODO 已弃用
                this.overridePendingTransition(animations[0], animations[1])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                super.startActivityForResult(intent, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
                PageLog.e(e)
            }
        } else {
            PageLog.e("[resolveActivity failed]: " + (if (intent.component != null) intent.component else intent.action) + " do not register in manifest")
        }
    }


    private fun popOrFinishActivity() {
        if (isFinishing) {
            return
        }
        // 关闭当前的fragment
        if (supportFragmentManager.backStackEntryCount > 1) {
            if (isMainThread()) {
                popBackStackImmediateSafety()
            } else {
                runOnUiThread {
                    popBackStackImmediateSafety()
                }
            }
        } else {
            finishActivity(this, true)
        }
    }


    /**
     * 关闭当前的activity，是否显示动画
     */
    private fun finishActivity(activity: EngineActivity, showAnimation: Boolean) {
        activity.finish()
        if (getIsAddActivityToStack()) {
            sActivities.remove(mCurrentActivity)
        }
        if (showAnimation) {
            var animations: IntArray? = null
            if (activity.mFirstCoreSwitchBean != null && activity.mFirstCoreSwitchBean?.anim != null) {
                animations = activity.mFirstCoreSwitchBean?.anim
            }

            if (animations != null && animations.size > 4) {
                overridePendingTransition(animations[2], animations[3])
                // TODO 弃用方法替代，后面可以在测试
//                val options = ActivityOptions.makeCustomAnimation(
//                    activity,
//                    animations[2],
//                    animations[3]
//                )
//                activity.finishAfterTransition()
            }
        }
    }

    /**
     * 获取是否将activity添加到堆栈中
     *
     * @return {@code true} :添加<br> {@code false} : 不添加
     */
    private fun getIsAddActivityToStack(): Boolean {
        return true
    }

    private fun popBackStackImmediateSafety() {
        try {
            supportFragmentManager.popBackStackImmediate()
        } catch (e: Exception) {
            PageLog.e(e)
        }
    }

    /**
     * 判断当前线程是否在主线程当中
     */
    private fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    /**
     * 初始化intent
     *
     * @param newIntent Intent对象
     */
    private fun init(intent: Intent) {
        try {
            val page: CoreSwitchBean? = intent.getParcelableExtra(CoreSwitchBean.KEY_SWITCH_BEAN)
            val startActivityForResult = intent.getBooleanExtra(CoreSwitchBean.KEY_START_ACTIVITY_FOR_RESULT, false)
            mFirstCoreSwitchBean = page
            if (page != null) {
                var fragment: XPageFragment? = null
                val addToBackStack = page.isAddToBackStack
                val pageName = page.pageName
                val bundle = page.bundle
                fragment = CorePageManager.getInstance()
                    .openPageWithNewFragmentManager(supportFragmentManager, pageName, bundle, null, addToBackStack)
                if (fragment != null) {
                    if (startActivityForResult) {
                        fragment.requestCode = page.requestCode
                        fragment.setFragmentFinishListener(XPageFragment.OnFragmentFinishListener { requestCode, resultCode, intent ->
                            this@EngineActivity.setResult(
                                resultCode,
                                intent
                            )
                        })
                    } else {
                        finish()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            PageLog.e(e)
            finish()
        }
    }

    /**
     * 打印，调试用
     */
    open fun printAllActivities() {
        PageLog.d("------------EngineActivity print all------------activities size:" + sActivities.size)
        for (ref in sActivities) {
            if (ref != null) {
                val item = ref.get()
                if (item != null) {
                    PageLog.d(item.toString())
                }
            }
        }
    }

    /**
     * 设置根布局
     */
    open fun setContentView() {
        val rootView: View? = getCustomRootView()
        if (rootView != null) {
            setContentView(rootView)
        } else {
            setContentView(getBaseLayout())
        }
    }

    /**
     * 设置根布局
     *
     * @return 根布局
     */
    open fun getBaseLayout(): View {
        val baseLayout = FrameLayout(this)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        baseLayout.id = R.id.fragment_container
        baseLayout.layoutParams = params
        return baseLayout
    }

    /**
     * 获取自定义根布局
     *
     * @return 自定义根布局
     */
    open fun getCustomRootView(): View? {
        return null
    }

    /**
     * 当前activity中弹fragment
     *
     * @param pageName     page的名字
     * @param bundle       传递的参数
     * @param findActivity 当前activity
     * @return 是否弹出成功
     */
    open fun popFragmentInActivity(pageName: String?, bundle: Bundle, findActivity: EngineActivity?): Boolean {
        if (pageName == null || findActivity == null || findActivity.isFinishing) {
            return false
        }
        val fragmentManager = findActivity.supportFragmentManager
        if (fragmentManager != null) {
            val frg = fragmentManager.findFragmentByTag(pageName)
            if (frg != null && frg is XPageFragment) {
                if (fragmentManager.backStackEntryCount > 1 && mHandler != null) {
                    mHandler?.postDelayed({
                        try {
                            fragmentManager.popBackStack(pageName, 0)
                        } catch (e: java.lang.Exception) {
                            PageLog.e(e)
                        }
                    }, getPopBackDelay().toLong())
                }
                frg.onFragmentDataReset(bundle)
                return true
            }
        }
        return false
    }

    /**
     * @return 弹出延迟时间
     */
    open fun getPopBackDelay(): Int {
        return 100
    }

    /**
     * 根据SwitchPage打开activity
     *
     * @param page CoreSwitchBean对象
     */
    private fun startActivity(page: CoreSwitchBean) {
        try {
            val intent = Intent(this, page.containActivityClazz)
            intent.putExtra(CoreSwitchBean.KEY_SWITCH_BEAN, page)
            this.startActivity(intent)
            val animations = page.anim
            if (animations != null && animations.size >= 2) {
                overridePendingTransition(animations[0], animations[1])
            }

        } catch (e: Exception) {
            e.printStackTrace()
            PageLog.e(e)
        }
    }

    override fun startActivity(intent: Intent?) {
        if (intent == null) {
            PageLog.e("[startActivity failed]: intent == null")
            return
        }
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                super.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                PageLog.e(e)
            }
        } else {
            PageLog.e("[resolveActivity failed]: " + (if (intent.component != null) intent.component else intent.action) + " do not register in manifest")
        }
    }


    //========================= fragment的打开方式 ==============================//
    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    fun <T : XPageFragment?> openPage(clazz: Class<T>?): T? {
        val page = CoreSwitchBean(clazz)
        return openPage(page) as T?
    }
}