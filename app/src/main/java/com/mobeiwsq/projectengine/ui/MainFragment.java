package com.mobeiwsq.projectengine.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.mobeiwsq.annotation.Page;
import com.mobeiwsq.annotation.enums.CoreAnim;
import com.mobeiwsq.engine_project.utils.TitleBar;
import com.mobeiwsq.engine_project.utils.TitleUtils;
import com.mobeiwsq.projectengine.databinding.FragmentMainBinding;

/**
 * @author xuexiang
 * @date 2018/1/7 下午6:47
 */
@Page
public class MainFragment extends BaseFragment<FragmentMainBinding>{
    @NonNull
    @Override
    protected FragmentMainBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentMainBinding.inflate(inflater, container, false);
    }

    @Override
    protected TitleBar initTitleBar() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack());
    }
}
