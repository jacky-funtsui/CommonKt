package com.ssf.framework.main.mvvm.bind.state;

import android.databinding.BindingAdapter;

import com.ssf.framework.refreshlayout.XRefreshLayout;
import com.ssf.framework.widget.state.IStateLayout;
import com.ssf.framework.widget.state.StateFrameLayout;

/**
 * 状态布局的绑定适配
 * Created by Hzz on 2018/8/21
 */
public class StateLayoutBinding {
    /**
     * 实际上XRefresh控件自己本身已经实现了状态的管理，可以不用处理
     * @param refreshLayout
     * @param state
     */
    @BindingAdapter(value = "state")
    public static void bindStateLayout(XRefreshLayout refreshLayout, IStateLayout state) {
        refreshLayout.setStateLayout(state);
    }

    @BindingAdapter(value = "state")
    public static void bindStateLayout(StateFrameLayout stateLayout, IStateLayout state) {
        stateLayout.setStateLayout(state);
    }
}
