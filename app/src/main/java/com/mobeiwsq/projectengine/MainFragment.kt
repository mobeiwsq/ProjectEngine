package com.mobeiwsq.projectengine

import com.mobeiwsq.annotation.Page
import com.mobeiwsq.engine_project.base.EngineFragment
import com.mobeiwsq.engine_project.core.openPage
import com.mobeiwsq.projectengine.databinding.FragmentMainBinding

@Page(name = "測試1")
class MainFragment :EngineFragment<FragmentMainBinding>(R.layout.fragment_main){
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
        binding.button.setOnClickListener {
            openPage(TwoFragment::class.java)
        }
    }

//    override fun initTitleBar(): TitleBar? {
//        return null
//    }

}