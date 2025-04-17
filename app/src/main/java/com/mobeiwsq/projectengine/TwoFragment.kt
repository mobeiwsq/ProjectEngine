package com.mobeiwsq.projectengine

import android.content.Intent
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.engine.core.popToBack
import com.mobeiwsq.projectengine.databinding.FragmentTwoBinding

@Page(anim = CoreAnim.slide)
class TwoFragment : EngineFragment<FragmentTwoBinding>(R.layout.fragment_two){
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
//        binding.button.setOnClickListener {
//            val intent = Intent()
//            intent.putExtra("aaaaa", "==【返回的数据】==")
//            setFragmentResult(500, intent)
//            popToBack()
//        }
    }
}