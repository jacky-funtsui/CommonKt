package com.ssf.framework.main.mvvm.livedata

import com.ssf.framework.main.mvvm.livedata.event.EventLiveData
import com.ssf.framework.widget.ex.IToast

/**
 * Created by hzz on 2018/8/18.
 */
class ToastLiveData : EventLiveData<ToastLiveData.Toast>() {

    fun show(message: String, type: IToast = IToast.NORMAL) {
        postEvent(Toast(type, message))
    }

    class Toast(val type: IToast, val message: String)
}