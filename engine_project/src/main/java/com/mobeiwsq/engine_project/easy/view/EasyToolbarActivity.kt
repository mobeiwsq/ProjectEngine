package com.mobeiwsq.engine_project.easy.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobeiwsq.engine_project.R
import com.mobeiwsq.engine_project.easy.utils.immersive
import com.mobeiwsq.engine_project.easy.utils.statusPadding

/**
 * 在项目layout目录下创建`layout_engine_toolbar.xml`可以覆写标题栏布局
 */
abstract class EasyToolbarActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    EasyActivity<B>(contentLayoutId) {

    lateinit var actionbar: FrameLayout
    lateinit var actionLeft: TextView
    lateinit var actionRight: TextView
    lateinit var actionTitle: TextView

    /**
     * 构建一个Toolbar
     */
    open fun onCreateToolbar(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_engine_toolbar, container, false)
    }

    override fun setTitle(title: CharSequence?) {
        if (this::actionTitle.isInitialized) actionTitle.text = title ?: return
    }

    override fun setTitle(titleId: Int) {
        this.title = getString(titleId)
    }

    fun setActionRight(title: CharSequence?) {
        if (this::actionRight.isInitialized) actionRight.text = title ?: return
    }

    @SuppressLint("InflateParams")
    override fun setContentView(layoutResId: Int) {
        val contentView = layoutInflater.inflate(R.layout.activity_engine_toolbar, null, false)
        setContentView(contentView)
        val container = contentView as ViewGroup
        val toolbar = onCreateToolbar(layoutInflater, container)
        DataBindingUtil.bind<ViewDataBinding>(toolbar)
        container.addView(toolbar)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResId, container, true)
        rootView = binding.root
        (findViewById<View>(R.id.actionbar) as? FrameLayout)?.let { actionbar = it }
        (findViewById<View>(R.id.actionTitle) as? TextView)?.let { actionTitle = it }
        (findViewById<View>(R.id.actionLeft) as? TextView)?.let { actionLeft = it }
        (findViewById<View>(R.id.actionRight) as? TextView)?.let { actionRight = it }
        if (this::actionLeft.isInitialized) actionLeft.setOnClickListener { onBack(it) }
        immersive()
        actionbar.statusPadding()
        init()
    }

    open fun onBack(v: View) {
        finishTransition()
    }
}