package com.mobeiwsq.projectengine.view.fragment

import android.view.View
import com.mobeiwsq.engine_project.engine.base.EngineFragment
import com.mobeiwsq.engine_project.view.imageview.preview.PreviewBuilder
import com.mobeiwsq.engine_project.view.imageview.preview.enitity.ImageViewInfo
import com.mobeiwsq.engine_project.view.progress.HorizontalProgressView.HorizontalProgressUpdateListener
import com.mobeiwsq.engine_project.view.title.TitleBar
import com.mobeiwsq.projectengine.R
import com.mobeiwsq.projectengine.databinding.FragmentMineBinding

class MineFragment : EngineFragment<FragmentMineBinding>(R.layout.fragment_mine) ,HorizontalProgressUpdateListener{

    override fun initView() {
        binding.hpvLanguage.setProgressViewUpdateListener(this)
    }

    override fun initData() {
        binding.hpvLanguage.startProgressAnimation()
    }

    override fun initListeners() {
        binding.button.setOnClickListener {
            if (binding.expandableLayout.isExpanded) {
                binding.expandableLayout.collapse()
            } else {
                binding.expandableLayout.expand()
            }
        }

        binding.imgButton.setOnClickListener {
            val imageViewInfo =
                ImageViewInfo("http://112.132.1.147:9000/bozhou-jindi/2025/4/17/1912696821959553026.jpg")
            PreviewBuilder.from(requireActivity()).setImg(imageViewInfo).start()
        }
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