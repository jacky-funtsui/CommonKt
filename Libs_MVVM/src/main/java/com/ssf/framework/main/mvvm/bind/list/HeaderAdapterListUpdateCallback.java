package com.ssf.framework.main.mvvm.bind.list;

import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter;

/**
 * 带头部适配器列表增量更新
 * Created by Hzz on 2018/8/30
 */
public class HeaderAdapterListUpdateCallback implements ListUpdateCallback {
    private RecyclerView.Adapter mAdapter;

    public HeaderAdapterListUpdateCallback(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        if (isHeadAdapter() && position == 0) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.notifyItemRangeRemoved(position, count);
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        if (isHeadAdapter() && position == 0) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.notifyItemRangeChanged(position, count, payload);
        }
    }

    /**
     * 判断是不是带头部的适配器，如果有头部不能局部操作头部Item，否则会报错
     *
     * @return
     */
    private boolean isHeadAdapter() {
        if (mAdapter instanceof BaseBindingAdapter) {
            return true;
        }
        return false;
    }
}
