package com.mobeiwsq.projectengine

import android.util.Log
import com.mobeiwsq.engine_project.easy.view.EasyToolbarActivity
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding
import com.mobeiwsq.projectengine.ui.AddInspectionDialog

class MainActivity : EasyToolbarActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        setActionRight("保存")
    }

    override fun initData() {
        binding.text.setOnClickListener {
            Log.d("TAGTAGTAG", "initData: 11111111111")
            goNewInspection()
        }

    }

    override fun initListeners() {
        actionRight.setOnClickListener {
            Log.d("MainActivity_TAG", "initListeners: 111111111111111")
        }
    }


    private fun goNewInspection(){
        AddInspectionDialog.getInstance(this).setClickInterListener(object :AddInspectionDialog.ClickInterListener{
            override fun projectClick() {
            }

            override fun programClick() {
            }

            override fun typeClick() {
            }

            override fun submit() {
            }

            override fun cancel() {
            }

        }).createDialog()
    }

}