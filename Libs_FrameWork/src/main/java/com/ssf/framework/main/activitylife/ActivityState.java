package com.ssf.framework.main.activitylife;

import android.app.Activity;

public interface ActivityState {
    /**
     * 得到当前Activity
     * @return
     */
    Activity current();

    /**
     * 任务栈中Activity的总数
     * @return
     */
    int count();
    /**
     * 判断应用是否处于前台，即是否可见
     * @return
     */
    boolean isFront();
}