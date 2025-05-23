package com.mobeiwsq.engine_project.view.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.mobeiwsq.engine_project.R;
import com.mobeiwsq.engine_project.utils.DensityUtils;
import com.mobeiwsq.engine_project.utils.ThemeUtils;
import com.mobeiwsq.engine_project.utils.Utils;
import com.mobeiwsq.engine_project.view.widget.WrapContentListView;

/**
 * 继承自 {@link EnginePopup}，在 {@link EnginePopup} 的基础上，支持显示一个列表。
 */
public class ListPopup<T extends ListPopup> extends EnginePopup {
    protected ListView mListView;
    protected ListAdapter mAdapter;
    private boolean mHasDivider;

    /**
     * 构造方法.
     *
     * @param context   Context
     * @param direction
     */
    public ListPopup(Context context, int direction, ListAdapter adapter) {
        super(context, direction);
        mAdapter = adapter;
    }

    public ListPopup(Context context, ListAdapter adapter) {
        super(context);
        mAdapter = adapter;
    }

    /**
     * 创建弹窗
     *
     * @param width               弹窗的宽度
     * @param maxHeight           弹窗最大的高度
     * @param onItemClickListener 列表点击的监听
     * @return
     */
    public T create(int width, int maxHeight, AdapterView.OnItemClickListener onItemClickListener) {
        create(width, maxHeight);
        mListView.setOnItemClickListener(onItemClickListener);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width     弹窗的宽度
     * @param maxHeight 弹窗最大的高度
     * @return
     */
    protected T create(int width, int maxHeight) {
        int margin = Utils.dp2px(getContext(), 5);
        if (maxHeight != 0) {
            mListView = new WrapContentListView(getContext(), maxHeight);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, maxHeight);
            lp.setMargins(0, margin, 0, margin);
            mListView.setLayoutParams(lp);
        } else {
            mListView = new WrapContentListView(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, margin, 0, margin);
            mListView.setLayoutParams(lp);
        }
        mListView.setPadding(margin, 0, margin, 0);
        mListView.setAdapter(mAdapter);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        updateListViewDivider(mListView);
        setContentView(mListView);
        return (T) this;
    }

    /**
     * 创建弹窗
     *
     * @param width 弹窗的宽度
     * @return
     */
    protected T create(int width) {
        return create(width, 0);
    }

    /**
     * 设置是否有分割线
     *
     * @param hasDivider
     * @return
     */
    public T setHasDivider(boolean hasDivider) {
        mHasDivider = hasDivider;
        if (mListView != null) {
            updateListViewDivider(mListView);
        }
        return (T) this;
    }

    private void updateListViewDivider(ListView listView) {
        if (mHasDivider) {
            listView.setDivider(new ColorDrawable(ThemeUtils.resolveColor(getContext(), R.attr.config_color_separator_light)));

            listView.setDividerHeight(DensityUtils.INSTANCE.dp2px(getContext(), 0.5F));
        } else {
            listView.setDivider(null);
        }
    }

    /**
     * 设置分割线的资源
     *
     * @param divider
     * @return
     */
    public T setDivider(Drawable divider) {
        if (mListView != null) {
            mListView.setDivider(divider);
        }
        return (T) this;
    }

    /**
     * 设置分割线的高度
     *
     * @param dividerHeight
     * @return
     */
    public T setDividerHeight(int dividerHeight) {
        if (mListView != null) {
            mListView.setDividerHeight(dividerHeight);
        }
        return (T) this;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public ListView getListView() {
        return mListView;
    }
}
