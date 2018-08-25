package com.ssf.framework.main.mvvm.vm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.ssf.framework.main.mvvm.livedata.ErrorLiveData
import com.ssf.framework.main.mvvm.livedata.ProgressLiveData
import com.ssf.framework.main.mvvm.livedata.ToastLiveData

/**
 * Created by hzz on 2018/8/18.
 */
open class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val progress = ProgressLiveData()
    val error = ErrorLiveData()
    val toast = ToastLiveData()
}