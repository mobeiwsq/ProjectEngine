package com.mobeiwsq.engine_project.utils

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
* 软键盘相关工具类
*
* @author : mobeiwsq
* @since :  2025/4/10 11:15
*/
object KeyBoardUtils {
    /**
     *处理默认点击事件，默认处理隐藏输入法
     */
    fun onTouchDown(event: MotionEvent,activity: Activity?) {
        activity?.let {
            if (isShouldHideInput(it.window, event)) {
                it.currentFocus?.let { view ->
                    hideSoftInPut(view)
                }

            }
        }
        return
    }

    /**
     * 是否需要隐藏软键盘
     */
    fun isShouldHideInput(window: Window?, event: MotionEvent): Boolean {
        window?.let {
            if (isInSide(it, it.currentFocus, event) || !isSoftInPutDisplayed(window)) {
                return false
            }
            return true
        }
        return false
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInPut(currentFocus: View) {
        currentFocus.clearFocus()
        //关闭软键盘
        val imm = currentFocus.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 判断点击坐标是否在EditText内部
     */
    private fun isInSide(window: Window, currentFocus: View?, ev: MotionEvent): Boolean {
        currentFocus?.let {
            val location = intArrayOf(0, 0)
            //获取当前EditText坐标
            currentFocus.getLocationInWindow(location)
            //上下左右
            val left = location[0]
            val top = location[1]
            val right = left + currentFocus.width
            val bottom = top + currentFocus.height
            //点击坐标是否在其内部
            return (ev.x >= left && ev.x <= right && ev.y > top && ev.y < bottom)
        }
        return false
    }

    /**
     * 软键盘是否需要显示
     */
    private fun isSoftInPutDisplayed(window: Window): Boolean {
        return ViewCompat.getRootWindowInsets(window.decorView)
            ?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
    }
}