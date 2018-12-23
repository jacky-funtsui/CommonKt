package com.ssf.commonkt.di.module

import com.ssf.commonkt.di.scope.FragmentScope
import com.ssf.commonkt.ui.lazy.SampleLazyLogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectorModule {
    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun lazySampleFragmentInjector():SampleLazyLogFragment
}