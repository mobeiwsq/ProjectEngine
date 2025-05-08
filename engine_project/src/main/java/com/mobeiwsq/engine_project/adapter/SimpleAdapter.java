package com.mobeiwsq.engine_project.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.mobeiwsq.engine_project.R;
import com.mobeiwsq.engine_project.adapter.listview.BaseListAdapter;
import com.mobeiwsq.engine_project.adapter.simple.AdapterItem;
import com.mobeiwsq.engine_project.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的适配器
 *
 */
public class SimpleAdapter extends BaseListAdapter<AdapterItem, ViewHolder> {

    private int mPaddingStartPx;

    public SimpleAdapter(Context context) {
        super(context);
    }

    public SimpleAdapter(Context context, List<AdapterItem> data) {
        super(context, data);
    }

    public SimpleAdapter(Context context, AdapterItem[] data) {
        super(context, data);
    }

    @Override
    protected ViewHolder newViewHolder(View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        holder.mLLContentView = convertView.findViewById(R.id.ll_content);
        holder.mTvTitle = convertView.findViewById(R.id.tv_title);
        holder.mIvIcon = convertView.findViewById(R.id.iv_icon);

        if (mPaddingStartPx != 0) {
            holder.mLLContentView.setPaddingRelative(mPaddingStartPx, 0, 0, 0);
            holder.mLLContentView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            holder.mLLContentView.setGravity(Gravity.CENTER);
        }
        return holder;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_listview_simple_item;
    }

    @Override
    protected void convert(ViewHolder holder, AdapterItem item, int position) {
        holder.mTvTitle.setText(item.getTitle());
        if (item.getIcon() != null) {
            holder.mIvIcon.setVisibility(View.VISIBLE);
            holder.mIvIcon.setImageDrawable(item.getIcon());
        } else {
            holder.mIvIcon.setVisibility(View.GONE);
        }

    }

    @Deprecated
    public SimpleAdapter setPaddingLeftPx(int paddingLeftPx) {
        mPaddingStartPx = paddingLeftPx;
        return this;
    }

    @Deprecated
    public SimpleAdapter setPaddingLeftDp(@NonNull Context context, int paddingLeftDp) {
        mPaddingStartPx = Utils.dp2px(context, paddingLeftDp);
        return this;
    }

    public SimpleAdapter setPaddingStartPx(int paddingStartPx) {
        mPaddingStartPx = paddingStartPx;
        return this;
    }

    public SimpleAdapter setPaddingStartDp(@NonNull Context context, int paddingStartDp) {
        mPaddingStartPx = Utils.dp2px(context, paddingStartDp);
        return this;
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static SimpleAdapter create(Context context, String[] data) {
        if (data != null && data.length > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (String datum : data) {
                lists.add(new AdapterItem(datum));
            }
            return new SimpleAdapter(context, lists);
        } else {
            return new SimpleAdapter(context);
        }
    }

    /**
     * 创建简单的适配器【不含图标】
     *
     * @param context
     * @param data
     * @return
     */
    public static SimpleAdapter create(Context context, List<String> data) {
        if (data != null && data.size() > 0) {
            List<AdapterItem> lists = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                lists.add(new AdapterItem(data.get(i)));
            }
            return new SimpleAdapter(context, lists);
        } else {
            return new SimpleAdapter(context);
        }
    }

}
