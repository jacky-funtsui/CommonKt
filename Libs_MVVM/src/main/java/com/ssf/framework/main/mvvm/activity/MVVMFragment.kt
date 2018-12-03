package com.ssf.framework.main.mvvm.activity

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssf.framework.main.activity.BaseFragment
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @atuthor ydm
 * @data on 2018/8/14
 * @describe
 */
abstract class MVVMFragment<T : ViewDataBinding>(
        private val layoutResID: Int,
        // click 列表
        vararg ids: Int = intArrayOf(0)
) : BaseFragment(layoutResID, *ids) {
    // mvvm
    protected lateinit var binding: T

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appViewModelProvider: ViewModelProvider

    val viewModelProvider: ViewModelProvider by lazy {
        createViewModelProvider()
    }

    /**
     * 标志位
     */
    protected var isPrepared: Boolean = false //是否已经就绪，但未显示
    protected var isLazyLoad: Boolean = false //是否已执行过懒加载

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 注入
        AndroidSupportInjection.inject(this)
        // dataBinding
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false)
        binding.setLifecycleOwner(this)
        // 记录布局，避免多次创建
        mInflate = binding.root
        // 初始化默认配置
        initDefaultConfig(savedInstanceState)
        // DataBinding
        // 注册
        initClickEvent()

        isPrepared = true
        return mInflate
    }


    override fun initDefaultConfig(savedInstanceState: Bundle?) {
        init(mInflate, savedInstanceState)
        //初始化监听
        setClickViewId(mInflate)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {
            isLazyLoad = true
            onLazyLoad()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isPrepared && !isLazyLoad) {
            isLazyLoad = true
            onLazyLoad()
        }
    }


    /**
     * 懒加载数据
     */
    open fun onLazyLoad() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }


    private fun initClickEvent() {
        //初始化监听
        setClickViewId(mInflate)
    }

    protected open fun createViewModelProvider(): ViewModelProvider {
        return SuperViewModelProvider(this, viewModelFactory, appViewModelProvider)
    }
}