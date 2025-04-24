package com.mobeiwsq.projectengine.view.fragment

import androidx.viewpager2.widget.ViewPager2
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.adapter.MainFragmentAdapter
import com.mobeiwsq.projectengine.databinding.FragmentMainBinding

@Page(anim = CoreAnim.none)
class MainFragment : EngineFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val fragmentList = listOf(
        HomeFragment(),
        MineFragment()
    )

    // 监听页面切换事件
    private var callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when (position) {
                0 -> binding.mainMenuHome.isChecked = true
                1 -> binding.mainMenuMine.isChecked = true
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }
    }


    override fun initView() {
        val fragmentAdapter = MainFragmentAdapter(childFragmentManager, lifecycle, fragmentList)
        binding.vpHome.adapter = fragmentAdapter
        binding.vpHome.currentItem = 0

        binding.vpHome.registerOnPageChangeCallback(callback)

    }

    override fun initData() {
    }

    override fun initListeners() {
        binding.mainMenuHome.setOnClickListener {
            binding.vpHome.currentItem = 0
        }

        binding.mainMenuMine.setOnClickListener {
            binding.vpHome.currentItem = 1
        }
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }
}