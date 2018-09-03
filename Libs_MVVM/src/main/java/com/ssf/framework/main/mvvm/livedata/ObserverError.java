package com.ssf.framework.main.mvvm.livedata;

import com.ssf.framework.main.mvvm.livedata.ErrorLiveData.Error;

import org.jetbrains.annotations.NotNull;
/**
 * Created by hzz on 2018/8/19.
 */
public interface ObserverError {
    Boolean onChangedNeedIntercept(@NotNull Error error);
}
