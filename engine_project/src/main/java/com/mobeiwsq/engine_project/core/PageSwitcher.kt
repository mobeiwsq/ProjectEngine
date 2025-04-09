package com.mobeiwsq.engine_project.core

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.EngineConfig
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.base.EngineActivity
import com.mobeiwsq.engine_project.base.EngineFragment
import com.mobeiwsq.engine_project.logger.PageLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    val page = EngineConfig.getPage(clazz)
    val pageInfo = PageInfo(if (TextUtils.isEmpty(page.name)) clazz.simpleName else page.name, clazz)
    val pageName = pageInfo.name
    if (!TextUtils.isEmpty(page.params[0])) {
        pageInfo.setParams(page.params)
    }
    pageInfo.setAnim(page.anim)
    PageLog.d("page------$page-------pageInfo---$pageInfo")

    if (newActivity) {

    } else {
        openPageWithNewFragmentManager(supportFragmentManager,pageName,bundle,addToBackStack)
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

    val page = EngineConfig.getPage(clazz)
    val pageInfo = PageInfo(if (TextUtils.isEmpty(page.name)) clazz.simpleName else page.name, clazz)
    val pageName = pageInfo.name
    if (!TextUtils.isEmpty(page.params[0])) {
        pageInfo.setParams(page.params)
    }
    pageInfo.setAnim(page.anim)
    PageLog.d("page------$page-------pageInfo---$pageInfo")

    if (newActivity) {

    } else {
        openPageWithNewFragmentManager(requireActivity().supportFragmentManager,pageName,bundle,addToBackStack)
    }
}

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
    fragmentManager:FragmentManager,
    pageName:String,
    bundle: Bundle,
    addToBackStack: Boolean
){
    val corePage = EngineConfig.mPageMap[pageName]
    if (corePage == null){
        PageLog.d("Page:$pageName is null")
        return
    }
    val fragment = try {
        Class.forName(corePage.mClazz)
            .getDeclaredConstructor()
            .newInstance() as EngineFragment<*>
    } catch (e: Exception) {
        PageLog.e("Fragment instantiation failed for ${corePage.mClazz}", e)
        return
    }

    fragment.apply {
        arguments = bundle
        mPageName = pageName
    }

    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentManager.findFragmentById(R.id.fragment_container)?.let {
        fragmentTransaction.hide(it)
    }
    fragmentTransaction.add(R.id.fragment_container,fragment,pageName)
    if (addToBackStack){
        fragmentTransaction.addToBackStack(pageName)
    }
    fragmentTransaction.commitAllowingStateLoss()
}