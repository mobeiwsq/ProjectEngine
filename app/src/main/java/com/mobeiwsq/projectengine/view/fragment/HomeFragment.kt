package com.mobeiwsq.projectengine.view.fragment

import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.databinding.FragmentHomeBinding


class HomeFragment:EngineFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
        binding.richang.setOnClickListener {
            openPage(DailyInspectionFragment::class.java)
        }

        binding.zhaunxiag.setOnClickListener {
            openPage(SpecialInspectionFragment::class.java)
        }
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }


}