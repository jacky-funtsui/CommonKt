package com.ssf.commonkt.di.component

import android.app.Application
import com.ssf.commonkt.App
import com.ssf.commonkt.di.module.AllActivitiesModule
import com.ssf.commonkt.di.module.AppModule
import com.ssf.commonkt.di.module.AppViewModelModule

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @atuthor ydm
 * @data on 2018/8/13
 * @describe
 */
@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AndroidSupportInjectionModule::class,
            // 全局提供的module
            AppModule::class,
            AppViewModelModule::class,
            // 每个activity 对应的设计
            AllActivitiesModule::class
        ]
)
interface AppComponent {


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}