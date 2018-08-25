package com.ssf.commonkt.di.module

import android.arch.lifecycle.ViewModel
import com.ssf.framework.main.mvvm.vm.AppViewModel
import com.ssf.commonkt.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * 全局的ViewModel提供模块
 * Created by Hzz on 2018/8/22
 */
@Module
internal abstract class AppViewModelModule {
    //避免因没有Provider<ViewModel>造成ViewModelFactory无法生成，编译无法通过,需要有最少一个ViewModel提供到Map中
    //AppViewModel这里做占坑用,全局的ViewModel最好按业务划分，继承于AppViewModel
    @Binds
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    abstract fun bindAppViewModel(vm: AppViewModel): ViewModel
}