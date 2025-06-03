package com.mobeiwsq.engine_project.easy

import android.view.View
import androidx.databinding.BindingAdapter

object DataBindingComponent {
    @BindingAdapter("paddingStart", "paddingEnd", requireAll = false)
    @JvmStatic
    fun setPaddingHorizontal(v: View, start: View?, end: View?) {
        v.post {
            val startFinal = (start?.width ?: 0) + v.paddingStart
            val endFinal = (end?.width ?: 0) + v.paddingEnd
            v.setPaddingRelative(startFinal, v.paddingTop, endFinal, v.paddingBottom)
        }
    }
}