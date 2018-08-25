package com.ssf.framework.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.ssf.framework.autolayout.utils.AutoUtils;


/**
 * @author 小民
 * @time 2017/7/15
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views  = new SparseArray<>();

    public BaseViewHolder(View itemView) {
        super(itemView);
        AutoUtils.autoSize(itemView);
    }

    public <T extends View> T get(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

}
