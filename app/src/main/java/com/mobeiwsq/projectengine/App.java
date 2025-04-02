package com.mobeiwsq.projectengine;

import android.app.Application;
import com.mobeiwsq.engine_project.AutoPageConfiguration;
import com.mobeiwsq.engine_project.PageConfig;
import com.mobeiwsq.engine_project.base.XPageActivity;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PageConfig.getInstance()
                .debug(true)
                .setContainActivityClazz(XPageActivity.class)
                .init(this);
    }
}
