package com.mobeiwsq.engine_project.base

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.mobeiwsq.engine_project.R

/**
 * Activity基类
 *
 * @author : mobeiwsq
 * @since :  2025/4/2 17:48
 */

abstract class EngineActivity(@LayoutRes contentLayoutId: Int = 0) :
    AppCompatActivity(contentLayoutId) {
    private val layoutResID = contentLayoutId

    lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutResID!=0){
            rootView = layoutInflater.inflate(layoutResID, null)
        } else {
            rootView = getBaseLayout()
        }

        // 设置 FrameLayout 为内容视图
        setContentView(rootView)
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
        baseLayout.background = getDrawable(R.color.red)
        return baseLayout
    }
}