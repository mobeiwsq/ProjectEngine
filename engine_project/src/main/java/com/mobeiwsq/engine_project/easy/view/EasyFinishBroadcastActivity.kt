package com.mobeiwsq.engine_project.easy.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mobeiwsq.engine_project.easy.app
import java.io.Serializable

/**
 * 关闭全局activity
 *
 * @author : mobeiwsq
 * @since :  2025/5/21 08:44
 */
open class EasyFinishBroadcastActivity(contentLayoutId: Int = 0) : AppCompatActivity(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcast()
    }

    /** 发送给所有界面的广播 */
    open var receiver: BroadcastReceiver? = null

    protected open fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("FinishBroadcastActivity")
        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val flag = intent.getSerializableExtra("finish_skip")
                    if (flag != null && this@EasyFinishBroadcastActivity.javaClass == flag) return
                    finish()
                }
            }
        }
        receiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, intentFilter)
        }
    }

    /**
     * 注销广播
     */
    protected open fun unregisterBroadcast() {
        receiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            receiver = null
        }
    }

    companion object {
        /**
         * 发送关闭全部界面的广播
         * @param skip 被忽略的界面
         */
        @JvmStatic
        @JvmOverloads
        fun finishAll(skip: Serializable? = null) {
            val intent = Intent().setAction("FinishBroadcastActivity")
            skip?.let {
                intent.putExtra("finish_skip", skip)
            }
            LocalBroadcastManager.getInstance(app).sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }
}