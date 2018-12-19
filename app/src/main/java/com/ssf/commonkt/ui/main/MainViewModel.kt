package com.ssf.commonkt.ui.main

import android.app.Application
import android.databinding.ObservableArrayList
import com.ssf.commonkt.ui.rv.RvDemoActivity
import com.ssf.commonkt.ui.rv.simple.*
import com.ssf.commonkt.ui.state.StateLayoutActivity
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/25 0025
 * @describe
 */
class MainViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    val list = ObservableArrayList<MainActivity.Bean>()

    init {
        list.add(MainActivity.Bean("StateLayoutDemo", StateLayoutActivity::class.java))
        list.add(MainActivity.Bean("RecycleViewDemo", RvDemoActivity::class.java))
    }
}