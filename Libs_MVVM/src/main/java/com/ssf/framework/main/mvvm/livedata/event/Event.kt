package com.ssf.framework.main.mvvm.livedata.event

/**
 * Created by hzz on 2018/8/20.
 */
class Event<T> (
        val data:T,
        var hasBeenHandled:Boolean=false
) {

    /**
     * 返回数据内容
     */
    fun getDataIfNotHandled():T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            data
        }
    }
}