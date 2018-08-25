package com.ssf.framework.widget.other;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * @author robert
 * @date 2017/12/18
 * 添加在fragment切换之前的监听
 */

public class MyFragmentTabHost extends FragmentTabHost {
    private OnBeforeTabChangeListener onBeforeTabChangeListener;
    private String lastTabId;

    public void setOnBeforeTabChangeListener(OnBeforeTabChangeListener onBeforeTabChangeListener) {
        this.onBeforeTabChangeListener = onBeforeTabChangeListener;
    }

    public MyFragmentTabHost(Context context) {
        super(context);
    }

    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (onBeforeTabChangeListener != null && onBeforeTabChangeListener.onBeforeTabChanged(tabId)) {
            setCurrentTabByTag(lastTabId);
            return;
        }
        super.onTabChanged(tabId);
        lastTabId = tabId;
    }

    public interface OnBeforeTabChangeListener {
        /**
         * 在fragment跳转之前发生的回调
         * 如果是默认的onTabChangeListener的话，是在fragment切换之后才给出的回调
         *
         * @param tabId tab的Id
         * @return 是否拦截当前fragment的跳转
         */
        boolean onBeforeTabChanged(String tabId);
    }
}