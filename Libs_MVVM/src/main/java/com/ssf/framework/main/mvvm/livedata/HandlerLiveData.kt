package com.ssf.framework.main.mvvm.livedata

import android.annotation.SuppressLint
import com.ssf.framework.main.mvvm.livedata.event.SubscribeLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 类似Handler发送的Message，用来VM传递内容给View层
 */
class HandlerLiveData : SubscribeLiveData<HandlerLiveData.Message>() {

    @SuppressLint("CheckResult")
    fun sendMessage(what: Int, obj: Any? = null) {
        Observable.just(Message(what, obj))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    value = it
                }
    }

    data class Message(val what: Int, var obj: Any? = null)
}