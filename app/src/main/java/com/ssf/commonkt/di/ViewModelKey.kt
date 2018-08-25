package com.ssf.commonkt.di

import android.arch.lifecycle.ViewModel
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)