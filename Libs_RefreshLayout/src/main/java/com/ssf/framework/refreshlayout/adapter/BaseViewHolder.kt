package com.ssf.framework.refreshlayout.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import com.ssf.framework.autolayout.utils.AutoUtils

/**
 * @author yedanmin
 * @data 2018/1/17 15:51
 * @describe
 */
class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views = SparseArray<View>()

    init {
        AutoUtils.autoSize(itemView)
    }

    operator fun <T : View> get(viewId: Int): T {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

}