package com.mobeiwsq.engine_project.view.widget.timer.configure;

/**
 * 时间选择器的类型<br>
 * <p>
 * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
 *
 */
public enum TimePickerType {

    /**
     * 显示“年”“月”“日”
     */
    DEFAULT(new boolean[]{true, true, true, false, false, false}),
    /**
     * 显示“年”“月”“日”“时”“分”“秒”
     */
    ALL(new boolean[]{true, true, true, true, true, true}),
    /**
     * 显示“时”“分”“秒”
     */
    TIME(new boolean[]{false, false, false, true, true, true}),
    /**
     * 显示“年”“月”“日”“时”“分”
     */
    DATE(new boolean[]{true, true, true, true, true, false});


    private final boolean[] mType;

    TimePickerType(boolean[] type) {
        mType = type;
    }

    public boolean[] getType() {
        return mType;
    }
}

