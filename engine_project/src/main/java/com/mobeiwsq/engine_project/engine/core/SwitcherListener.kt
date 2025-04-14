package com.mobeiwsq.engine_project.engine.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mobeiwsq.annotation.model.PageInfo
import com.mobeiwsq.engine_project.engine.base.EngineFragment
/**
* 页面跳转接口
*
* @author : mobeiwsq
* @since :  2025/4/14 09:01
*/

interface SwitcherListener {

    /**
     * 页面跳转，支持跨Activity进行传递数据
     *
     * @param fragment BaseFragment对象
     * @return 打开的页面Fragment对象
     */
    fun openPageForResult(
        pageInfo: PageInfo, resultCode: Int,
        newActivity: Boolean = false,
        bundle: Bundle = Bundle(), fragment: EngineFragment<*>
    ): Fragment?
}