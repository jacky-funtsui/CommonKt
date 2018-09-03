package com.ssf.framework.main.mvvm.livedata

import android.app.Activity
import android.content.Intent
import com.ssf.framework.main.mvvm.livedata.event.EventLiveData

/**
 * Created by hzz on 2018/8/18.
 */
class ActivityLiveData : EventLiveData<ActivityLiveData.Result>() {
    companion object {
        const val CMD_FINISH=1
        const val CMD_FINISH_RESULT=2
    }

    fun finish(){
        postEvent(Result(CMD_FINISH))
    }

    fun finishResult(resultCode:Int,data:Intent?=null){
        postEvent(Result(CMD_FINISH_RESULT,resultCode,data))
    }

    class Result(val cmd:Int, val resutCode:Int=Activity.RESULT_CANCELED, val data:Intent?=null)
}