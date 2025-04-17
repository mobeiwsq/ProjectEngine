package com.mobeiwsq.engine_project.engine.core

/**
 * 页面切换相关工具类
 *
 * @author : mobeiwsq
 * @since :  2025/4/10 14:38
 */

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.EngineConfig
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.Utils

/**
 * 新跳转的activity的flag
 */
val SWITCHER_NEW_INTENT = "switcher_new_intent"

/**
 * 页面跳转核心函数之一
 * 添加并打开一个Fragment
 *
 * @param fragmentManager FragmentManager管理类
 * @param pageName        页面名
 * @param bundle          参数
 * @param animations      动画类型
 * @param addToBackStack  是否添加到返回栈
 * @return 打开的Fragment对象
 */
fun openPageWithNewFragmentManager(
    fragmentManager: FragmentManager,
    pageName: String,
    bundle: Bundle,
    animations: IntArray?,
    addToBackStack: Boolean
) :EngineFragment<*>?{
    val fragment:EngineFragment<*>
    val corePage = EngineConfig.mPageMap[pageName]
    if (corePage == null) {
        PageLog.d("Page:$pageName is null")
        return null
    }
    fragment = try {
        getFragment(corePage)
    } catch (e: Exception) {
        PageLog.e("Fragment instantiation failed for ${corePage.mClazz}", e)
        return null
    }

    fragment.apply {
        arguments = bundle
        mPageName = pageName
    }

    val fragmentTransaction = fragmentManager.beginTransaction()
    if (animations != null && animations.size >= 4) {
        fragmentTransaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
    }
    fragmentManager.findFragmentById(R.id.fragment_container)?.let {
        fragmentTransaction.hide(it)
    }

    fragmentTransaction.add(R.id.fragment_container, fragment, pageName)
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(pageName)
    }
    fragmentTransaction.commitAllowingStateLoss()
    return fragment
}

/**
 * 获取fragment
 */
fun getFragment(corePage: SwitcherInfo): EngineFragment<*> {
    return Class.forName(corePage.mClazz)
        .getDeclaredConstructor()
        .newInstance() as EngineFragment<*>
}

/**
 * 打开一个新的activity来承载fragment
 * 根据newActivity来判断
 */
fun startNewActivity(activity: EngineActivity, context: Context, pageInfo: PageInfo) {
    try {
        EngineConfig.mContainActivityClassName?.let {

            val intent = Intent(context, Class.forName(it))
            intent.putExtra(SWITCHER_NEW_INTENT, pageInfo)
            activity.startOpenActivity(intent)
            val animations = convertAnimations(pageInfo.anim)
            if (animations != null && animations.size >= 2) {
                activity.overridePendingTransition(animations[0], animations[1])
            }
        }

    } catch (e: Exception) {
        PageLog.e(e)
    }
}

/**
 * 页面跳转
 */

fun Activity.startOpenActivity(intent: Intent?) {
    if (intent == null) {
        PageLog.e("[startActivity failed]: intent == null")
        return
    }
    if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
        try {
            this.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            PageLog.e(e)
        }
    } else {
        PageLog.e("[resolveActivity failed]: " + (if (intent.component != null) intent.component else intent.action) + " do not register in manifest")
    }
}

/**
 *弹出fragment或者关闭activity
 */
fun popOrFinishActivity(activity: EngineActivity) {
    if (activity.isFinishing) {
        return
    }
    if (activity.supportFragmentManager.backStackEntryCount > 1) {
        if (isMainThread()) {
            popBackStackImmediateSafety(activity.supportFragmentManager)
        } else {
            activity.runOnUiThread {
                popBackStackImmediateSafety(activity.supportFragmentManager)
            }
        }
    } else {
        safelyFinishActivity(activity, true)
    }

}

/**
 * 结束activity，显示动画
 */
fun safelyFinishActivity(activity: EngineActivity, showAnimation: Boolean) {
    try {
        activity.finish()
        if (showAnimation) {
            applyExitAnimation(activity)
        }

    } catch (e: IllegalStateException) {
        PageLog.e("Activity already finishing: ${e.message}")
    }
}

/**
 * 安全退出当前的fragment
 */
fun popBackStackImmediateSafety(manager: FragmentManager) {
    try {
        manager.popBackStackImmediate()
    } catch (e: java.lang.Exception) {
        PageLog.e(e)
    }
}


/**
 * 是否是主线程
 *
 * @return 是否是主线程
 */
fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

/**
 * 应用退出动画（使用预定义动画资源）
 */
fun applyExitAnimation(activity: EngineActivity) {
    val animations = intArrayOf(
        R.anim.xpage_zoom_in,
        R.anim.xpage_zoom_out,
        R.anim.xpage_zoom_in,
        R.anim.xpage_zoom_out
    )

    try {
        activity.overridePendingTransition(
            animations[2],
            animations[3]
        )
    } catch (e: Resources.NotFoundException) {
        PageLog.e("Animation resources not found: ${e.message}")
    }
}

/**
 * 获取页面信息,检测页面是否添加@Page修饰
 */
fun getPage(clazz: Class<*>): Page {
    return Utils.checkNotNull(
        clazz.getAnnotation(Page::class.java),
        "Page == null，请检测页面是否漏加 @Page 进行修饰！"
    )
}

/**
 * 获取pageInfo并设置params和设置动画
 */
fun createPageInfo(clazz: Class<*>): PageInfo {
    val page = getPage(clazz)
    val pageInfo = PageInfo(
        page.name.takeIf { it.isNotEmpty() } ?: clazz.simpleName, clazz
    )
    return pageInfo.apply {
        if (page.params.isNotEmpty()) setParams(page.params)
        page.anim?.let {
            setAnim(it)
        }
    }
}

/**
 * 设置动画效果
 */
fun convertAnimations(coreAnim: CoreAnim): IntArray? {
    if (coreAnim === CoreAnim.present) {
        return intArrayOf(
            R.anim.xpage_push_in_down,
            R.anim.xpage_push_no_anim,
            R.anim.xpage_push_no_anim,
            R.anim.xpage_push_out_down
        )
    } else if (coreAnim === CoreAnim.fade) {
        return intArrayOf(R.anim.xpage_alpha_in, R.anim.xpage_alpha_out, R.anim.xpage_alpha_in, R.anim.xpage_alpha_out)
    } else if (coreAnim === CoreAnim.slide) {
        return intArrayOf(
            R.anim.xpage_slide_in_right,
            R.anim.xpage_slide_out_left,
            R.anim.xpage_slide_in_left,
            R.anim.xpage_slide_out_right
        )
    } else if (coreAnim === CoreAnim.zoom) {
        return intArrayOf(R.anim.xpage_zoom_in, R.anim.xpage_zoom_out, R.anim.xpage_zoom_in, R.anim.xpage_zoom_out)
    }
    return null
}