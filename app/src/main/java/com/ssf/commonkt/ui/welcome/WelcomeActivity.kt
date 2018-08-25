package com.ssf.commonkt.ui.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route

import com.ssf.commonkt.R
import com.ssf.commonkt.data.config.IRouterConfig
import com.ssf.commonkt.databinding.ActivityMainBinding
import com.ssf.commonkt.databinding.ActivityWelcomeBinding
import com.ssf.framework.main.mvvm.activity.MVVMActivity

@Route(path = IRouterConfig.AR_PATH_WELCOME)
class WelcomeActivity : MVVMActivity<ActivityWelcomeBinding>(
        R.layout.activity_welcome
) {

    override fun init() {
        val welcomeViewModel = viewModelProvider.get(WelcomeViewModel::class.java)
        binding.viewModel = welcomeViewModel
        welcomeViewModel.start(this)
    }

    /**
     * Back键拦截
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> return false
        }
        return super.onKeyDown(keyCode, event)
    }
}
