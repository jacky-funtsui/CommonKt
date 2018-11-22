package com.ssf.commonkt.ui.main

import android.app.Application
import android.databinding.ObservableArrayList
import com.ssf.commonkt.ui.simple.*
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
        list.add(MainActivity.Bean("常规Adapter", SimpleListAdapterActivity::class.java))
        list.add(MainActivity.Bean("兼容ClickIds Adapter", SimpleSupportAdapterActivity::class.java))
        list.add(MainActivity.Bean("头部尾部Adapter", SimpleHeaderFooterActivity::class.java))
        list.add(MainActivity.Bean("多布局MultiAdapter", SimpleMultiAdapterActivity::class.java))
        list.add(MainActivity.Bean("多布局DelegateAdapter", SimpleDelegateAdapterActivity::class.java))
    }
}