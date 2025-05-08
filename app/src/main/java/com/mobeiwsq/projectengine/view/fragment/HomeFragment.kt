package com.mobeiwsq.projectengine.view.fragment

import android.util.Log
import com.mobeiwsq.engine_project.adapter.SimpleAdapter
import com.mobeiwsq.engine_project.adapter.simple.AdapterItem
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.engine_project.view.calendarview.Calendar
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.engine_project.view.widget.popupwindow.SimplePopup
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.databinding.FragmentHomeBinding


class HomeFragment : EngineFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private var mMenuPopup: SimplePopup<*>? = null

    override fun initView() {
    }

    override fun initData() {
        val map: MutableMap<String, Calendar> = HashMap()
        map[getSchemeCalendar(2025, 1, 13, -0xec5310, "假").toString()] =
            getSchemeCalendar(2025, 1, 13, -0xec5310, "卡")
        map[getSchemeCalendar(2025, 1, 12, -0xec5310, "卡").toString()] =
            getSchemeCalendar(2025, 1, 12, -0xec5310, "卡")
        map[getSchemeCalendar(2025, 1, 11, -0xec5310, "卡").toString()] =
            getSchemeCalendar(2025, 1, 11, -0xec5310, "卡")
        map[getSchemeCalendar(2025, 1, 1, -0xec5310, "卡").toString()] =
            getSchemeCalendar(2025, 1, 1, -0xec5310, "卡")
        binding.calendarView.setSchemeDate(map)
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

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        calendar.addScheme(Calendar.Scheme())
        calendar.addScheme(-0xff7800, "假")
        calendar.addScheme(-0xff7800, "节")
        return calendar
    }


}