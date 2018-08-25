package com.ssf.commonkt.ui.welcome

import android.app.Application
import android.content.Context
import android.databinding.ObservableInt
import com.alibaba.android.arouter.launcher.ARouter
import com.ssf.commonkt.data.config.IRouterConfig
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/25 0025
 * @describe
 */
class WelcomeViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    val count = ObservableInt()


    fun start(activity: WelcomeActivity) {
        progress.show("正在载入")
        activity.finish()
        next(activity)
    }


    fun next(context: Context) {
        progress.hide()
        // 启动首页
        ARouter.getInstance()
                .build(IRouterConfig.AR_PATH_MAIN)
                .navigation()

    }
}