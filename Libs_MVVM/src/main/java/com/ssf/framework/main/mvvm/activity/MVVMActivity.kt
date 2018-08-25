package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ssf.framework.main.activity.BaseActivity
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/5 0005
 * @describe
 */
abstract class MVVMActivity<T : ViewDataBinding>(
        // 自定义布局
        private val layoutResID: Int,
        //需要设置点击事件的ViewId
        vararg ids: Int,
        // 是否可以滑动退出，默认true
        swipeBackLayoutEnable: Boolean = true,
        // StatusBar颜色
        statusBarColor: Int = 0,
        // StatusBar 透明度 (0 - 255)
        statusBarAlpha: Int = 0
) : BaseActivity(layoutResID, *ids, swipeBackLayoutEnable = swipeBackLayoutEnable, statusBarColor = statusBarColor, statusBarAlpha = statusBarAlpha),
        HasSupportFragmentInjector {

    // mvvm
    protected lateinit var binding: T
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appViewModelProvider: ViewModelProvider

    val viewModelProvider: ViewModelProvider by lazy {
        createViewModelProvider()
    }

    override fun supportFragmentInjector() = fragmentInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        // 注入
        AndroidInjection.inject(this)
        // create
        super.onCreate(savedInstanceState)
    }

    override fun setContentView() {
        // 初始化 Binding
        binding = DataBindingUtil.setContentView(this, layoutResID)
        binding.setLifecycleOwner(this)
    }

    /** 回收资源 */
    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    protected fun createViewModelProvider(): ViewModelProvider {
        return SuperViewModelProvider(this, viewModelFactory, appViewModelProvider)
    }

}