package com.ssf.framework.main.mvvm.vm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.ssf.framework.main.mvvm.lifecycle.RxLifecycleViewModel
import com.ssf.framework.main.mvvm.lifecycle.ViewModelEvent
import com.ssf.framework.main.mvvm.livedata.*
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by hzz on 2018/8/18.
 */
open class BaseViewModel constructor(application: Application) : AndroidViewModel(application), LifecycleProvider<ViewModelEvent> {
    private val lifecycleSubject = BehaviorSubject.create<ViewModelEvent>()

    val progress = ProgressLiveData()
    val error = ErrorLiveData()
    val toast = ToastLiveData()
    val activity = ActivityLiveData()
    val handler = HandlerLiveData()

    override fun onCleared() {
        super.onCleared()
        lifecycleSubject.onNext(ViewModelEvent.CLEAR)
    }

    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindUntilEvent(event: ViewModelEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleViewModel.bindViewModel(lifecycleSubject)
    }
}