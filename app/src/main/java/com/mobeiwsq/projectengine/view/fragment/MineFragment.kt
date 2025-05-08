package com.mobeiwsq.projectengine.view.fragment

import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.view.progress.HorizontalProgressView.HorizontalProgressUpdateListener
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.adapter.CityListAdapter
import com.mobeiwsq.projectengine.databinding.FragmentMineBinding

class MineFragment : EngineFragment<FragmentMineBinding>(R.layout.fragment_mine), HorizontalProgressUpdateListener {

    private val mHeaders = arrayOf("城市")

    private val mCities:ArrayList<String?> = arrayListOf("上海", "北京", "广东", "重庆")
    var textView:TextView?  = null



    private val mPopupViews: ArrayList<View> = ArrayList()

    override fun initView() {
//        binding.hpvLanguage.setProgressViewUpdateListener(this)
    }

    override fun initData() {
        val cityView = ListView(requireContext())
        val cityListAdapter = CityListAdapter(requireContext(),mCities)
//        binding.hpvLanguage.startProgressAnimation()

        cityView.dividerHeight = 0
        cityView.adapter = cityListAdapter
//        cityListAdapter.refresh(mCities)
        mPopupViews.add(cityView)

        //add item click event
        cityView.setOnItemClickListener(OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            Log.d("swn_f", "position11111-----: $position")
            cityListAdapter.setSelectPosition(position)
            binding.dropDownMenu.setTabMenuText(if (position == 0) mHeaders[0] else mCities[position])
            binding.dropDownMenu.closeMenu()
            if (textView==null){
                textView = binding.dropDownMenu.contentView.findViewById<TextView>(R.id.text)
            }
            textView?.text = "56748"
        })


        //init dropdownview
        binding.dropDownMenu.setDropDownMenu(mHeaders, mPopupViews)
    }

    override fun initListeners() {
//        binding.button.setOnClickListener {
//            if (binding.expandableLayout.isExpanded) {
//                binding.expandableLayout.collapse()
//            } else {
//                binding.expandableLayout.expand()
//            }
//        }
//
//        binding.imgButton.setOnClickListener {
//            val imageViewInfo =
//                ImageViewInfo("http://112.132.1.147:9000/bozhou-jindi/2025/4/17/1912696821959553026.jpg")
//            PreviewBuilder.from(requireActivity()).setImg(imageViewInfo).start()
//        }
    }

    override fun initTitleBar(): TitleBar? {
        return null
    }

    override fun onHorizontalProgressStart(view: View?) {
    }

    override fun onHorizontalProgressUpdate(view: View?, progress: Float) {
    }

    override fun onHorizontalProgressFinished(view: View?) {
    }


}