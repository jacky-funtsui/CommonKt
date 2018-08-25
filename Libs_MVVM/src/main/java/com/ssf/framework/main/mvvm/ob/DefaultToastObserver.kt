package com.ssf.framework.main.mvvm.ob

import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import com.ssf.framework.main.mvvm.livedata.ToastLiveData
import com.ssf.framework.widget.ex.IToast
import com.ssf.framework.widget.ex.toast

/**
 * Created by hzz on 2018/8/18.
 */
class DefaultToastObserver(val owner: FragmentActivity) : Observer<ToastLiveData.Toast> {

    override fun onChanged(it: ToastLiveData.Toast?) {
        owner.toast(it?.message ?: "", it?.type ?: IToast.NORMAL)
    }

}