package com.ssf.commonkt.ui.welcome

import android.arch.lifecycle.ViewModel
import com.ssf.commonkt.ui.welcome.WelcomeViewModel
import com.ssf.commonkt.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @atuthor ydm
 * @data on 2018/8/13
 * @describe
 */
@Module
internal abstract class WelcomeViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindMainViewModel(viewModel: WelcomeViewModel): ViewModel

}
