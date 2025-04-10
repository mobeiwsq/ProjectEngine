package com.mobeiwsq.projectengine

import android.view.KeyEvent
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.engine_project.base.EngineFragment
import com.mobeiwsq.engine_project.core.openPage
import com.mobeiwsq.engine_project.utils.KeyClickUtils.back2Click
import com.mobeiwsq.projectengine.databinding.FragmentMainBinding

@Page(name = "測試1", anim = CoreAnim.none)
class MainFragment : EngineFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListeners() {
        binding.button.setOnClickListener {
            openPage(TwoFragment::class.java, newActivity = true)
        }
    }

//    override fun initTitleBar(): TitleBar? {
//        return null
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back2Click()
        }
        return true
    }

}