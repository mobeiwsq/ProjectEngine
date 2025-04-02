package com.mobeiwsq.projectengine;

import android.os.Bundle;
import com.mobeiwsq.projectengine.databinding.ActivityMainBinding;
import com.mobeiwsq.projectengine.ui.BaseActivity;
import com.mobeiwsq.projectengine.ui.MainFragment;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(MainFragment.class);
    }

}
