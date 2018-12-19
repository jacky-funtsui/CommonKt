package com.ssf.commonkt.di.module

import android.arch.lifecycle.ViewModelProvider
import com.ssf.commonkt.ui.welcome.WelcomeActivity
import com.ssf.commonkt.di.ViewModelFactory
import com.ssf.commonkt.di.module.main.MainViewModelModule
import com.ssf.commonkt.di.scope.ActivityScope
import com.ssf.commonkt.ui.main.MainActivity
import com.ssf.commonkt.ui.rv.RvDemoActivity
import com.ssf.commonkt.ui.welcome.WelcomeViewModelModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @atuthor ydm
 * @data on 2018/8/13
 * @describe
 */
@Module
internal abstract class AllActivitiesModule {


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @ContributesAndroidInjector(modules = [WelcomeViewModelModule::class])
    abstract fun contributeWelcomeActivityInjector(): WelcomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainViewModelModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

}