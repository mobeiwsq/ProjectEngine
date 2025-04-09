package com.mobeiwsq.engine_project.view.title

import android.content.Context
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.utils.Utils

/**
 * 导航栏工具
 *
 * @author : mobeiwsq
 * @since :  2025/4/9 17:22
 */

object TitleBarUtils {

    /**
     * 动态添加TitleBar
     */
    fun addTitleBar(viewGroup: ViewGroup, title: String, listener: OnClickListener): TitleBar {
        val titleBar = initTitleBar(viewGroup.context, title, listener)
        viewGroup.addView(titleBar, 0)
        return titleBar
    }

    /**
     * 动态生成TitleBar
     */
    private fun initTitleBar(context: Context, title: String, listener: OnClickListener): TitleBar {
        val titleBar = TitleBar(context)
        val titleBarParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleBar.layoutParams = titleBarParams
        initTitleBarStyle(titleBar, title, listener)
        return titleBar
    }

    /**
     * 初始化title的样式
     *
     * @param titleBar
     * @param title
     * @param listener
     * @return
     */
    private fun initTitleBarStyle(titleBar: TitleBar, title: String, listener: OnClickListener): TitleBar {
        // 未在样式文件中指定actionbar_navigation_back的话,使用默认的返回图标
        titleBar.setLeftImageDrawable(
            Utils.resolveDrawable(
                titleBar.context, R.attr.actionbar_navigation_back,
                Utils.getVectorDrawable(titleBar.context, R.drawable.ic_navigation_back_white)
            )
        )
            .setLeftClickListener(listener)
            .setTitle(title)
        return titleBar
    }
}