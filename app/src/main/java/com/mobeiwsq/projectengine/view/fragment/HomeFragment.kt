package com.mobeiwsq.projectengine.view.fragment

import android.util.Log
import com.mobeiwsq.engine_project.adapter.SimpleAdapter
import com.mobeiwsq.engine_project.adapter.simple.AdapterItem
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.engine_project.view.widget.popupwindow.SimplePopup
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.databinding.FragmentHomeBinding


class HomeFragment : EngineFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private var mMenuPopup: SimplePopup<*>? = null

    override fun initView() {
    }

    override fun initData() {
    }

    var dpiItems: Array<String> = arrayOf(
        "480 × 800",
        "1080 × 1920",
        "720 × 1280",
    )

    var menuItems: Array<AdapterItem> = arrayOf<AdapterItem>(
        AdapterItem("登陆", R.drawable.icon_password_login),
        AdapterItem("筛选", R.drawable.icon_filter),
        AdapterItem("设置", R.drawable.icon_setting),
    )

    override fun initListeners() {

        mMenuPopup = SimplePopup<SimplePopup<*>>(requireContext(), menuItems)
            .create(object : SimplePopup.OnPopupItemClickListener {
                override fun onItemClick(adapter: SimpleAdapter, item: AdapterItem, position: Int) {
                    Log.d("ddddddd", "${item.title}")
                }
            })

        binding.richang.setOnClickListener { view ->
            run {
                mMenuPopup?.showDown(view)
            }
        }

        binding.zhaunxiag.setOnClickListener {
            openPage(DailyInspectionFragment::class.java)
        }
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }


}