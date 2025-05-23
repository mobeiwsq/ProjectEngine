package com.mobeiwsq.engine_project.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;
import com.mobeiwsq.engine_project.R;
import com.mobeiwsq.engine_project.utils.DensityUtils;
import com.mobeiwsq.engine_project.utils.KeyboardUtils;

/**
 * 监听键盘弹出，自动滚动
 */
public class KeyboardScrollView extends ScrollView implements KeyboardUtils.SoftKeyboardToggleListener {

    /**
     * 默认键盘弹出时滚动的高度
     */
    private final static int DEFAULT_SCROLL_HEIGHT = 40;
    /**
     * 是否自动滚动，默认false
     */
    private boolean mAutoScroll;
    /**
     * 键盘弹出时滚动的高度
     */
    private int mScrollHeight;

    /**
     * 滚动延迟
     */
    private int mScrollDelay;

    /**
     * 滚动是否隐藏键盘，默认true
     */
    private boolean mScrollHide;

    public KeyboardScrollView(Context context) {
        super(context);
        mScrollHide = true;
    }

    public KeyboardScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public KeyboardScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KeyboardScrollView);
        if (ta != null) {
            mAutoScroll = ta.getBoolean(R.styleable.KeyboardScrollView_ksv_auto_scroll, false);
            mScrollHeight = ta.getDimensionPixelSize(R.styleable.KeyboardScrollView_ksv_scroll_height, DensityUtils.INSTANCE.dp2px(getContext(), DEFAULT_SCROLL_HEIGHT));
            mScrollDelay = ta.getInt(R.styleable.KeyboardScrollView_ksv_scroll_delay, 100);
            mScrollHide = ta.getBoolean(R.styleable.KeyboardScrollView_ksv_scroll_hide, true);
            ta.recycle();
        }
        if (mAutoScroll) {
            KeyboardUtils.addKeyboardToggleListener(this, this);
        }
    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {
        if (isVisible) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, getScrollY() + mScrollHeight);
                }
            }, mScrollDelay);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, 0);
                }
            }, mScrollDelay);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAutoScroll) {
            KeyboardUtils.removeKeyboardToggleListener(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollHide) {
            KeyboardUtils.hideSoftInput(this);
        }
    }

    /**
     * 设置滚动是否隐藏键盘
     *
     * @param isScrollHideKeyboard
     * @return
     */
    public KeyboardScrollView setIsScrollHideKeyboard(boolean isScrollHideKeyboard) {
        mScrollHide = isScrollHideKeyboard;
        return this;
    }
}
