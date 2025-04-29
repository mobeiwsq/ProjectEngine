package com.mobeiwsq.projectengine.view.fragment

import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.databinding.FragmentMineBinding

class MineFragment:EngineFragment<FragmentMineBinding>(R.layout.fragment_mine) {

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
        binding.button.setOnClickListener {
            if (binding.expandableLayout.isExpanded){
                binding.expandableLayout.collapse()
            } else {
                binding.expandableLayout.expand()
            }
        }
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }


}