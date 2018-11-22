package com.ssf.framework.main.mvvm.bind.list;

import android.databinding.ObservableList;

import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter;

import java.util.List;

/**
 * 根据可观察的数据进行自动刷新列表
 */
public class ObservableListUpdateCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private BaseBindingAdapter mAdapter;

    public ObservableListUpdateCallback(BaseBindingAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        replace(sender);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        replace(sender);
        if (positionStart == 0) {//有头部的适配器特殊处理
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        replace(sender);
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
        replace(sender);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        replace(sender);
        if (positionStart == 0) {//有头部的适配器特殊处理
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    private void replace(List list) {
        mAdapter.getList().clear();
        mAdapter.getList().addAll(list);
    }
}
