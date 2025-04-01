package com.mobeiwsq.projectengine

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobeiwsq.annotation.OnClick
import com.mobeiwsq.engine_project.MButterKnife


class MainActivity : AppCompatActivity() {

    @JvmField
    var tv1: TextView? = null

    @JvmField
    var tv2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //绑定
//        MButterKnife.bind(this)
        tv1?.text = "绑定成功！"
        tv2?.text = "^_^"
    }

    @OnClick
    fun btnClick(v: View) {
        when (v.id) {
            R.id.btn_1 -> {
                Toast.makeText(this, "点击按钮11", Toast.LENGTH_SHORT).show()
            }

            R.id.btn_2 -> {
                Toast.makeText(this, "点击按钮22", Toast.LENGTH_SHORT).show()
            }
        }
    }
//
//    @OnClick(R.id.btn_3)
//    fun btnClick2(v: View) {
//        when (v.id) {
//            R.id.btn_3 -> {
//                Toast.makeText(this, "点击按钮33", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}