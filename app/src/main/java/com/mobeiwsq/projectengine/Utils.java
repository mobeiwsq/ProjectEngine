package com.mobeiwsq.projectengine;

import android.content.Context;
import android.util.Log;
import androidx.annotation.StringRes;
import com.mobeiwsq.engine_project.utils.DateUtils;
import com.mobeiwsq.engine_project.view.dialog.bottomsheet.BottomSheet;
import com.mobeiwsq.engine_project.view.widget.timer.TimePickerBuilder;
import com.mobeiwsq.engine_project.view.widget.timer.configure.TimePickerType;
import com.mobeiwsq.engine_project.view.widget.timer.listener.OnTimeSelectListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {
    // 时间选择器，具体到日
    public static void showTimePickerDialog(Context context, OnTimeSelectListener listener) {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(DateUtils.getStartOfDay(new Date(), -30));
        Calendar nowDate = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.getStartOfDay(new Date(), 30));
        new TimePickerBuilder(context, listener)
                .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                .setType(TimePickerType.DEFAULT)
                .setTitleText("时间选择")
                .isDialog(true)
                .setOutSideCancelable(false)
                .setDate(nowDate)
                .setRangDate(startDate, calendar)
                .build().show();
    }

    /**
     * 底部弹窗列表Dialog
     */
    public static void showSimpleBottomSheetList(Context context, @StringRes int titleRes, List<String> numberList,
                                                 BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener onSheetItemClickListener) {
        BottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new BottomSheet.BottomListSheetBuilder(context);
        bottomListSheetBuilder
                .setTitle(titleRes);
        for (String number : numberList) {
            bottomListSheetBuilder.addItem(number);
        }
        bottomListSheetBuilder.setIsCenter(true)
                .setOnSheetItemClickListener(onSheetItemClickListener)
                .build()
                .show();
    }
}
