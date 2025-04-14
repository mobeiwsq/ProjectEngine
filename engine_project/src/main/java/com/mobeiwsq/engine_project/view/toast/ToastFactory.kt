package com.mobeiwsq.engine_project.view.toast

import android.content.Context
import android.widget.Toast

interface ToastFactory {
    companion object DEFAULT : ToastFactory {
        override fun onCreate(context: Context, msg: CharSequence, duration: Int): Toast? {
            return Toast.makeText(context, msg, duration)
        }

    }

    fun onCreate(
        context: Context,
        msg: CharSequence,
        duration: Int
    ): Toast?
}