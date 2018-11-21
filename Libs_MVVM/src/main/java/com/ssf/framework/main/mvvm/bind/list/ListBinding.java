package com.ssf.framework.main.mvvm.bind.list;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter;
import com.ssf.framework.refreshlayout.XRefreshLayout;
import com.ssf.framework.refreshlayout.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表布局的相关绑定适配
 * Created by Hzz on 2018/8/20
 */
public class ListBinding {
    private static final String TAG = "ListBinding";

    @BindingAdapter(value = "data")
    public static void bindRefreshLayoutData(XRefreshLayout refreshLayout, List data) {
        RecyclerView.Adapter adapter = refreshLayout.getMLoadMoreRecyclerView().getAdapter();
        bindAdapterData(refreshLayout.getMLoadMoreRecyclerView(), adapter, data);
    }

    @BindingAdapter(value = "data")
    public static void bindRecyclerViewData(RecyclerView recyclerView, List data) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        bindAdapterData(recyclerView, adapter, data);
    }

    /**
     * 数据更新到适配器
     *
     * @param adapter
     * @param data    刷新的数据
     */
    private static void bindAdapterData(RecyclerView view, RecyclerView.Adapter adapter, List data) {
        if (adapter == null) {
            Log.e(TAG, "未给RecyclerView绑定适配器,请检查");
            return;
        }

        if (data instanceof ObservableArrayList && adapter instanceof BaseBindingAdapter) {
            Object tag = view.getTag();
            if (tag == null) {
                ObservableListUpdateCallback callback = new ObservableListUpdateCallback((BaseBindingAdapter) adapter);
                ((ObservableList) data).addOnListChangedCallback(callback);
                if (adapter instanceof BaseBindingAdapter) {
                    BaseBindingAdapter adt = (BaseBindingAdapter) adapter;
                    ArrayList<Object> oldData = adt.getList();
                    if (oldData.isEmpty()) {
                        callback.onChanged((ObservableList) data);//初次绑定手动触发用来添加数据
                    }
                }
                view.setTag(callback);
            } else {
                if (tag instanceof ObservableListUpdateCallback) {
                    //不用处理，内部Callback会自动进行数据处理
                } else {
                    throw new RuntimeException("使用Observable的RecyclerView不能绑定其它类型的TAG");
                }
            }

            return;
        }

        if (adapter instanceof BaseBindingAdapter) {
            BaseBindingAdapter adt = (BaseBindingAdapter) adapter;
            ArrayList<Object> oldData = adt.getList();
            diffBindAdapterData(adapter, oldData, data);
        } else if (adapter instanceof BaseAdapter) {
            BaseAdapter adt = (BaseAdapter) adapter;
            ArrayList oldData = adt.getMData();
            diffBindAdapterData(adapter, oldData, data);
        }
    }

    /**
     * 增量更新数据集并刷新适配器
     *
     * @param adapter
     * @param oldDatas 旧数据
     * @param newDatas 新数据
     */
    private static void diffBindAdapterData(RecyclerView.Adapter adapter, List oldDatas, List newDatas) {
        if (oldDatas.isEmpty()) {
            oldDatas.addAll(newDatas);
            adapter.notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(oldDatas, newDatas));
            oldDatas.clear();
            oldDatas.addAll(newDatas);
            diffResult.dispatchUpdatesTo(new HeaderAdapterListUpdateCallback(adapter));
        }
    }

    private static void closeItemAnimatior(RecyclerView recyclerView, boolean isClose) {
        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
    }
}
