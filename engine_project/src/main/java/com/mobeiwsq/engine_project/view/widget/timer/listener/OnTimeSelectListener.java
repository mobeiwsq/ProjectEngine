package com.mobeiwsq.engine_project.view.widget.timer.listener;

import android.view.View;

import java.util.Date;

/**
 * 时间选择监听器
 *
 */
public interface OnTimeSelectListener {
    /**
     * 时间选择
     *
     * @param date
     * @param v
     */
    void onTimeSelected(Date date, View v);
}
