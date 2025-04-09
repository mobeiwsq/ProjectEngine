package com.mobeiwsq.projectengine

import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.engine_project.base.EngineFragment
import com.mobeiwsq.projectengine.databinding.FragmentTwoBinding

@Page(anim = CoreAnim.zoom)
class TwoFragment :EngineFragment<FragmentTwoBinding>(R.layout.fragment_two){
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
    }
}