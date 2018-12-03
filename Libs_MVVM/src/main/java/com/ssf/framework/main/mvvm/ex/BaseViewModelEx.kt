package com.ssf.framework.main.mvvm.ex

import com.ssf.framework.main.mvvm.lifecycle.ViewModelEvent
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.net.transformer.ApplySchedulers
import com.ssf.framework.net.transformer.ConvertSchedulers
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response

/**
 * @atuthor dm
 * @data on 2018/9/20
 * @describe
 */


/**
 * 网络请求
 */
public inline fun <T> BaseViewModel.apply(
        // 必传对象，用于控制声明周期
        observable: Observable<T>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    observable.compose(ApplySchedulers(retry))
            .compose(bindUntilEvent(ViewModelEvent.CLEAR))
            .subscribe(object : Observer<T> {

                override fun onSubscribe(d: Disposable) {
                    if (iDialog != IDialog.UN_LOADING) {
                        progress.show(message)
                    }
                }

                override fun onNext(t: T) {
                    progress.hide()
                    try {
                        success(t)
                    } catch (e: Exception) {
                        //业务代码异常
                        onError(e)
                    }
                }

                override fun onError(e: Throwable) {
                    progress.hide()
                    try {
                        error(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    complete()
                }

                override fun onComplete() {
                    complete()
                }

            })
}

public inline fun <T> BaseViewModel.convert(
        // 必传对象，用于控制声明周期
        observable: Observable<Response<T>>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    observable.compose(ConvertSchedulers(retry))
            .compose(bindUntilEvent(ViewModelEvent.CLEAR))
            .subscribe(object : Observer<T> {

                override fun onSubscribe(d: Disposable) {
                    if (iDialog != IDialog.UN_LOADING) {
                        progress.show(message)
                    }
                }

                override fun onNext(t: T) {
                    progress.hide()
                    try {
                        success(t)
                    } catch (e: Exception) {
                        //业务代码异常
                        onError(e)
                    }
                }

                override fun onError(e: Throwable) {
                    progress.hide()
                    try {
                        error(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    complete()
                }

                override fun onComplete() {
                    complete()
                }

            })
}


