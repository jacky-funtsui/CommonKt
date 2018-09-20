package com.ssf.framework.main.mvvm.ex

import com.ssf.framework.main.mvvm.lifecycle.ViewModelEvent
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import com.ssf.framework.net.common.ProgressSubscriber
import com.ssf.framework.net.common.ResponseListener
import com.ssf.framework.net.common.ResponseSubscriber
import com.ssf.framework.net.ex.convert
import com.ssf.framework.net.ex.convertRequest
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.net.transformer.ApplySchedulers
import com.ssf.framework.net.transformer.ConvertSchedulers
import com.ssf.framework.net.transformer.wrapperSchedulers
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xm.xlog.KLog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.util.ArrayList

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
        // 成功后，并执行完 success 方法后回调
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true
) {
    observable.compose(ApplySchedulers(retry))
            .compose(bindUntilEvent(ViewModelEvent.CLEAR))
            .subscribe(object : Observer<T> {

                override fun onSubscribe(d: Disposable) {
                    if (iDialog != IDialog.UN_LOADING) {
                        progress.show("loading...")
                    }
                }

                override fun onNext(t: T) {
                    progress.hide()
                    try {
                        success(t)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    progress.hide()
                    try {
                        error(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        KLog.e("onError函数调用奔溃")
                    }
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
        // 成功后，并执行完 success 方法后回调
        noinline complete: () -> Unit = {},
        // 是否重试
        retry: Boolean = true
) {
    observable.compose(ConvertSchedulers(retry))
            .compose(bindUntilEvent(ViewModelEvent.CLEAR))
            .subscribe(object : Observer<T> {

                override fun onSubscribe(d: Disposable) {
                    if (iDialog != IDialog.UN_LOADING) {
                        progress.show("loading...")
                    }
                }

                override fun onNext(t: T) {
                    progress.hide()
                    try {
                        success(t)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    progress.hide()
                    try {
                        error(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        KLog.e("onError函数调用奔溃")
                    }
                }

                override fun onComplete() {
                    complete()
                }

            })
}


