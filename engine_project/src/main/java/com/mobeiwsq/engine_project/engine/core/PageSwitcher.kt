package com.mobeiwsq.engine_project.engine.core

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mobeiwsq.engine_project.engine.base.EngineActivity
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.logger.PageLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 页面跳转操作
 *
 * @author : mobeiwsq
 * @since :  2025/4/8 15:54
 */
fun EngineActivity.openPage(
    clazz: Class<*>,
    addToBackStack: Boolean = true,
    newActivity: Boolean = false,
    bundle: Bundle = Bundle(),
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) = lifecycleScope.launch(dispatcher) {

    val pageInfo = createPageInfo(clazz)
    val animations = convertAnimations(pageInfo.anim)
    PageLog.d("pageInfo---$pageInfo")

    if (newActivity) {
        startNewActivity(this@openPage, this@openPage, pageInfo)
    } else {
        withContext(Dispatchers.Main.immediate) {
            openPageWithNewFragmentManager(supportFragmentManager, pageInfo.name, bundle, animations, addToBackStack)
        }
    }
}


/**
 * 页面跳转操作
 *
 * @author : mobeiwsq
 * @since :  2025/4/8 15:54
 */
fun EngineFragment<*>.openPage(
    clazz: Class<*>,
    addToBackStack: Boolean = true,
    newActivity: Boolean = false,
    bundle: Bundle = Bundle(),
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) = lifecycleScope.launch(dispatcher) {
    val pageInfo = createPageInfo(clazz)
    val animations = convertAnimations(pageInfo.anim)
    PageLog.d("pageInfo---$pageInfo")

    if (!isAdded || isDetached) {
        PageLog.w("Fragment state invalid: isAdded=$isAdded, isDetached=$isDetached")
        return@launch
    }

    val fragmentManager = requireActivity().supportFragmentManager


    if (newActivity) {
        startNewActivity(requireActivity() as EngineActivity, requireContext(), pageInfo)
    } else {
        withContext(Dispatchers.Main.immediate) {
            openPageWithNewFragmentManager(
                fragmentManager,
                pageInfo.name,
                bundle,
                animations,
                addToBackStack
            )
        }
    }
}


/**
 * 弹出栈顶的Fragment。如果Activity中只有一个Fragment时，Activity也退出。
 */
fun EngineFragment<*>.popToBack() {
    popToBack(null, null)
}

/**
 * 如果在fragment栈中找到，则跳转到该fragment中去，否则弹出栈顶
 *
 * @param pageName 页面名
 * @param bundle   参数
 */
fun EngineFragment<*>.popToBack(pageName: String?, bundle: Bundle?) {
//    pageName?.let {
//        if (isFindPage(pageName)) {
//            openPage(pageName)
//            return
//        }
//    }

    (requireActivity() as? EngineActivity)?.let { activity ->
        if (activity.isFinishing) return
        popOrFinishActivity(activity)
    }

}



