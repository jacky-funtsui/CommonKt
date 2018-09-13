package com.ssf.commonkt.ui.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.ssf.commonkt.R
import com.ssf.commonkt.data.config.IRouterConfig
import com.ssf.commonkt.databinding.ActivityMainBinding
import com.ssf.framework.main.mvvm.activity.MVVMActivity

@Route(path = IRouterConfig.AR_PATH_MAIN)
class MainActivity :   MVVMActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initStatusBar() {

    }

    override fun init() {

    }
}
