package com.mobeiwsq.projectengine

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import com.mobeiwsq.annotation.Page
import com.mobeiwsq.annotation.enums.CoreAnim
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.engine.core.openPage
import com.mobeiwsq.engine_project.logger.PageLog
import com.mobeiwsq.engine_project.utils.KeyClickUtils.back2Click
import com.mobeiwsq.projectengine.databinding.FragmentMainBinding

@Page(name = "測試1", anim = CoreAnim.none)
class MainFragment : EngineFragment<FragmentMainBinding>(R.layout.fragment_main) {

    // 模拟数据
    val dataList = listOf("切换动画", "传递数据", "打开新的activity容器")
    val fragmentList = listOf(
        TwoFragment::class.java,
        TwoFragment::class.java,
        TwoFragment::class.java,
    )


    override fun initView() {
    }

    override fun initData() {
        // 创建适配器
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dataList)

        // 为 ListView 设置适配器
        binding.listView.adapter = adapter

        // 设置 ListView 的点击事件监听器
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            openPage(fragmentList[position])
//            openPageForResult(fragmentList[position], 2222)
        }
    }

    override fun initListeners() {
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back2Click()
        }
        return true
    }


    override fun onFragmentResult(resultCode: Int, intent: Intent) {
        super.onFragmentResult(resultCode, intent)
        if (intent != null) {
            val extras: Bundle? = intent.extras
            PageLog.d(" resultCode:$resultCode data:" + extras?.getString("aaaaa"))
        }
    }

}