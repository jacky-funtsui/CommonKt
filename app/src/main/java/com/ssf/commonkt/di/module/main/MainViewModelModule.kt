package com.ssf.commonkt.di.module.main

import android.arch.lifecycle.ViewModel
import com.ssf.commonkt.di.ViewModelKey
import com.ssf.commonkt.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @atuthor ydm
 * @data on 2018/8/23
 * @describe
 */
@Module
internal abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}
