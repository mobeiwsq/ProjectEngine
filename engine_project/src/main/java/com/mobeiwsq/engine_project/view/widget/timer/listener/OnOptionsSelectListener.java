package com.mobeiwsq.engine_project.view.widget.timer.listener;

import android.view.View;

/**
 * 条件选择的监听器
 *
 */
public interface OnOptionsSelectListener {

    /**
     * @param view
     * @param options1 选项1
     * @param options2 选项2
     * @param options3 选项3
     * @return true：拦截，不消失；false：不拦截，消失
     */
    boolean onOptionsSelect(View view, int options1, int options2, int options3);

}
