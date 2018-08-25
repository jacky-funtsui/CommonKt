package com.ssf.commonkt.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import com.ssf.commonkt.R
import com.ssf.commonkt.data.network.IConstantPool
import com.ssf.commonkt.data.network.IOfficialApi
import com.ssf.framework.net.common.RetrofitClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @atuthor ydm
 * @data on 2018/8/13
 * @describe
 */
@Module
internal object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun getHttpService(): IOfficialApi {
        return RetrofitClient.Builder(
                IOfficialApi::class.java,
                true,
                IConstantPool.sCommonUrl,
                headers = { HashMap() }
        ).create()
    }

    @Singleton
    @Provides
    @JvmStatic
            /**
             * 共享的ViewModelProvider，提供全局的ViewModelStore
             * @param factory factory的创建需要有Map<Class,ViewModel>的支持，所以需要IntoMap来提供具体的ViewModel
             * @see AppViewModelModule
             */
    fun getShareViewModelProvider(factory: ViewModelProvider.Factory): ViewModelProvider {
        val viewModelStore = ViewModelStore()
        return ViewModelProvider(viewModelStore, factory)
    }


}