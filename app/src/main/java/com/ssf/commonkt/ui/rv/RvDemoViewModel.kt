package com.ssf.commonkt.ui.rv

import android.app.Application
import android.databinding.ObservableArrayList
import com.ssf.commonkt.ui.rv.simple.*
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/25 0025
 * @describe
 */
class RvDemoViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    val list = ObservableArrayList<RvDemoActivity.Bean>()
    init {
        list.add(RvDemoActivity.Bean("常规Adapter", SimpleListAdapterActivity::class.java))
        list.add(RvDemoActivity.Bean("兼容ClickIds Adapter", SimpleSupportAdapterActivity::class.java))
        list.add(RvDemoActivity.Bean("头部尾部Adapter", SimpleHeaderFooterActivity::class.java))
        list.add(RvDemoActivity.Bean("多布局MultiAdapter", SimpleMultiAdapterActivity::class.java))
        list.add(RvDemoActivity.Bean("多布局DelegateAdapter", SimpleDelegateAdapterActivity::class.java))
    }
}