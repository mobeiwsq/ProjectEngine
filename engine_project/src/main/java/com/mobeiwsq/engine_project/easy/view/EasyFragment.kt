package com.mobeiwsq.engine_project.easy.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mobei.engine.utils.KeyBoardUtils.onTouchDown

abstract class EasyFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId) {


    // 懒加载绑定（仅在视图存在时有效）
    private var _binding: B? = null
    val binding: B
        get() = _binding ?: throw IllegalStateException("视图已销毁，无法访问binding")

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListeners()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DataBindingUtil.bind<B>(view)

        _binding = DataBindingUtil.bind(view)
            ?: throw IllegalStateException("fragment DataBinding绑定失败，请检查布局文件是否正确")

        try {
            initView()
            initData()
            initListeners()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    @Deprecated("使用OnBackPressedCallback处理返回键", ReplaceWith("registerOnBackPressedCallback()"))
    open fun onBackPressed(): Boolean {
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

    override fun onDestroyView() {
        _binding = null  // 清除绑定防止泄漏
        super.onDestroyView()
    }
}
