package com.mobeiwsq.engine_project

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.mobeiwsq.engine_project.utils.DateUtils
import com.mobeiwsq.engine_project.view.dialog.bottomsheet.BottomSheet
import com.mobeiwsq.engine_project.view.dialog.materialdialog.MaterialDialog
import com.mobeiwsq.engine_project.view.widget.timer.TimePickerBuilder
import com.mobeiwsq.engine_project.view.widget.timer.configure.TimePickerType
import com.mobeiwsq.engine_project.view.widget.timer.listener.OnTimeSelectListener
import java.util.*

/**
 * 常用工具类整合
 *
 * @author : mobeiwsq
 * @since :  2025/4/23 14:50
 */

/**
 * 自定义UI
 */
fun showCustomDialog(
    context: Context, customView: View,
    positiveText: String, negativeText: String,
    callback: MaterialDialog.SingleButtonCallback,
    callbackOut: MaterialDialog.SingleButtonCallback
): MaterialDialog? {
    return MaterialDialog.Builder(context)
        .customView(customView,false)
        .positiveText(positiveText)
        .onPositive(callback)
        .onNegative(callbackOut)
        .negativeText(negativeText)
        .build()
}


/**
 * 简单的提示性对话框
 */
fun showSimpleTipDialog(
    context: Context, title: Int, content: String,
    positiveText: String, negativeText: String,
    callback: MaterialDialog.SingleButtonCallback
) {
    MaterialDialog.Builder(context)
//        .iconRes(R.drawable.icon_check_mark)
        .title(title)
        .content(content)
        .positiveText(positiveText)
        .onPositive(callback)
        .negativeText(negativeText)
        .show()
}

/**
 * 带单选框的dialog
 */
fun showSingleChoiceDialog(
    mContext: Context, titleRes: Int,
    @ArrayRes itemsRes: Int,
    callback: MaterialDialog.ListCallbackSingleChoice
) {
    MaterialDialog.Builder(mContext)
        .title(titleRes)
        .items(itemsRes)
        .itemsCallbackSingleChoice(
            0, callback
        )
        .positiveText("确定")
        .negativeText("取消")
        .show();
}

/**
 * 带单选项的Dialog
 */
fun showSingleChoiceDialog(
    mContext: Context,
    @StringRes titleRes: Int,
    collection: Collection<*>,
    callback: MaterialDialog.ListCallbackSingleChoice
) {
    MaterialDialog.Builder(mContext)
        .title(titleRes)
        .items(collection)
        .itemsCallbackSingleChoice(
            0, callback
        )
        .positiveText("确定")
        .negativeText("取消")
        .show()
}


/**
 * 带单选项的Dialog
 */
fun showSingleChoiceDialog(
    mContext: Context, @StringRes titleRes: Int, collection: Collection<*>, selectedIndex: Int,
    callback: MaterialDialog.ListCallbackSingleChoice
) {
    MaterialDialog.Builder(mContext)
        .title(titleRes)
        .items(collection)
        .itemsCallbackSingleChoice(
            selectedIndex, callback
        )
        .positiveText("确定")
        .negativeText("取消")
        .show()
}

/**
 * 带多选项的Dialog
 */
fun showMultiChoiceDialog(
    mContext: Context, @StringRes titleRes: Int, collection: Collection<*>,
    positions: Array<Int?>, callback: MaterialDialog.ListCallbackMultiChoice
) {
    MaterialDialog.Builder(mContext)
        .title(titleRes)
        .items(collection)
        .itemsCallbackMultiChoice(positions, callback)
        .positiveText("确定")
        .negativeText("取消")
        .show()
}


/**
 * 带多选项的Dialog
 */
fun showMultiChoiceDialog(
    mContext: Context, @StringRes titleRes: Int, collection: Collection<*>,
    positions: Array<Int?>?, callback: MaterialDialog.ListCallbackMultiChoice,
    onNeutralCallback: MaterialDialog.SingleButtonCallback
) {
    MaterialDialog.Builder(mContext)
        .title(titleRes)
        .items(collection)
        .autoDismiss(false)
        .itemsCallbackMultiChoice(positions, callback)
        .positiveText("确定")
        .negativeText("取消")
        .onNegative { dialog, which -> dialog.dismiss() }
        .neutralText("全选")
        .onNeutral(onNeutralCallback)
        .show()
}

/**
 * //     * 带输入框的对话框
 * //
 */
fun showInputDialog(
    context: Context, content: String, hint: String?,
    callback: MaterialDialog.InputCallback, onNegativeCallback: MaterialDialog.SingleButtonCallback
) {
    MaterialDialog.Builder(context)
        .content(content)
        .inputType(
            InputType.TYPE_CLASS_TEXT
        )
        .input(
            hint,
            "",
            true, callback
        )
        .inputRange(0, 100)
        .positiveText("同意")
        .negativeText("拒绝")
        .onNegative(onNegativeCallback)
        .cancelable(true)
        .canceledOnTouchOutside(true)
        .show()
}

/**
 * 底部弹窗列表Dialog
 */
fun showSimpleBottomSheetList(
    context: Context?, @StringRes titleRes: Int, numberList: List<String?>,
    onSheetItemClickListener: BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener?
) {
    val bottomListSheetBuilder: BottomSheet.BottomListSheetBuilder = BottomSheet.BottomListSheetBuilder(context)
    bottomListSheetBuilder
        .setTitle(titleRes)
    for (number in numberList) {
        bottomListSheetBuilder.addItem(number)
    }
    bottomListSheetBuilder.setIsCenter(true)
        .setOnSheetItemClickListener(onSheetItemClickListener)
        .build()
        .show()
}

// 时间选择器，具体到日
fun showTimePickerDialog(context: Context, listener: OnTimeSelectListener, dayStart: Int, dayEnd: Int) {
    val startDate = Calendar.getInstance()
    startDate.time = DateUtils.getStartOfDay(Date(), -dayStart)
    val nowDate = Calendar.getInstance()
    val calendar = Calendar.getInstance()
    calendar.time = DateUtils.getStartOfDay(Date(), dayEnd)
    TimePickerBuilder(context, listener)
        .setTimeSelectChangeListener { date -> Log.i("pvTime", "onTimeSelectChanged") }
        .setType(TimePickerType.DEFAULT)
        .setTitleText("时间选择")
        .isDialog(true)
        .setOutSideCancelable(false)
        .setDate(nowDate)
        .setRangDate(startDate, calendar)
        .build().show()
}

/**
 * 时间选择器，具体到分钟
 */
fun showTimePickerMillionsDialog(context: Context, listener: OnTimeSelectListener, dayStart: Int, dayEnd: Int) {
    val startDate = Calendar.getInstance()
    startDate.time = DateUtils.getStartOfDay(Date(), -dayStart)
    val nowDate = Calendar.getInstance()
    val endData = Calendar.getInstance()
    endData.time = DateUtils.getStartOfDay(Date(), dayEnd)
    TimePickerBuilder(context, listener)
        .setTimeSelectChangeListener { date -> Log.i("pvTime", "onTimeSelectChanged") }
        .setType(TimePickerType.ALL)
        .setTitleText("时间选择")
        .isDialog(true)
        .setOutSideCancelable(false)
        .setDate(nowDate)
        .setRangDate(startDate, endData)
        .build().show()
}