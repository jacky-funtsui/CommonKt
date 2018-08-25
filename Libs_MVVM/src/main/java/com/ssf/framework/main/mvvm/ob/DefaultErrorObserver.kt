package com.ssf.framework.main.mvvm.ob

import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import com.ssf.framework.main.mvvm.livedata.ErrorLiveData.Error
import com.ssf.framework.widget.ex.IToast
import com.ssf.framework.widget.ex.toast

/**
 * Created by hzz on 2018/8/18.
 */
class DefaultErrorObserver(val owner: FragmentActivity) : Observer<Error> {
    override fun onChanged(it: Error?) {
        owner.toast(it?.message ?: "", IToast.NORMAL)
    }
}