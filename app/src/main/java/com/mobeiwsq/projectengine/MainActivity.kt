package com.mobeiwsq.projectengine

import android.util.Log
import com.mobeiwsq.engine_project.easy.view.EasyToolbarActivity
import com.mobeiwsq.engine_project.file.FileSelectManager
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding
import com.mobeiwsq.projectengine.ui.AddInspectionDialog

class MainActivity : EasyToolbarActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        setActionRight("保存")
    }

    override fun initData() {
        binding.text.setOnClickListener {
            Log.d("TAGTAGTAG", "initData: 11111111111")
//            goNewInspection()
            FileSelectManager.open(
                this@MainActivity,
                object : FileSelectManager.OnFileSelectListener {
                    override fun onFileSelected(filePath: String) {
                        Log.d("MainActivity_TAG", "filePath: $filePath")
                    }

                    override fun onCanceled() {
                    }

                }
            )
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

    /**
     * 处理权限请求结果，转发给FileSelectManager处理
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 将权限处理结果传递给FileSelectManager
        FileSelectManager.onRequestPermissionsResult(requestCode, grantResults, this)
    }



}