package com.mobeiwsq.projectengine.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mobeiwsq.engine_project.adapter.listview.BaseListAdapter
import com.mobeiwsq.engine_project.utils.ResUtils
import com.mobeiwsq.projectengine.R

/**
 * 城市列表adapter
 *
 * @author : mobeiwsq
 * @since :  2025/5/8 08:45
 */
class CityListAdapter(
    private val context: Context,  // 添加 Context 参数
    private val cityList: ArrayList<String?> = ArrayList()
) : BaseListAdapter<String, CityListAdapter.ViewHolder>(context, cityList) {


    class ViewHolder() {
        var mText: TextView? = null
    }

    override fun newViewHolder(convertView: View?, parent: ViewGroup): ViewHolder {
        val holder = ViewHolder()
        holder.mText = convertView?.findViewById(R.id.title)
        return holder
    }

    override fun getLayoutId(): Int {
        return R.layout.adapter_drop_down_list_item
    }

    override fun convert(holder: ViewHolder, item: String?, position: Int) {
        holder.mText?.setText(item)
        if (mSelectPosition !== -1) {
            if (mSelectPosition === position) {
                holder.mText?.setSelected(true)
                holder.mText?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    ResUtils.getVectorDrawable(context, R.drawable.ic_checked_right),
                    null
                )
            } else {
                holder.mText?.setSelected(false)
                holder.mText?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            }
        }
    }

}