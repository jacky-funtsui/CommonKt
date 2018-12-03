package com.ssf.framework.main.mvvm.ex

import com.ssf.framework.main.mvvm.lifecycle.ViewModelEvent
import com.ssf.framework.main.mvvm.vm.BaseViewModel
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.net.transformer.ConvertSchedulers
import com.trello.rxlifecycle2.LifecycleTransformer
import com.xm.xlog.KLog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import retrofit2.Response
import java.util.*

/**
 * @atuthor dm
 * @data on 2018/9/20
 * @describe
 */

/**
 * Fragment扩展，加入生命周期控制，有重试操作
 * @param transformer  生命周期绑定
 * @param retry        是否重试，默认 yes
 */
public inline fun <T> Observable<Response<T>>.convertRequest(transformer: LifecycleTransformer<T>, retry: Boolean = true): Observable<T> {
    return this.compose(ConvertSchedulers(retry))
            .compose(transformer)
}

/**
 * 组合 zip操作符号
 */
public inline fun <T1, T2> BaseViewModel.convertZip(
        t1: Observable<Response<T1>>,
        t2: Observable<Response<T2>>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        crossinline success: (T1, T2) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 网络失败，是否重试请求
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    Observable.zip(
            t1.convertRequest(bindUntilEvent(ViewModelEvent.CLEAR), retry),
            t2.convertRequest(bindUntilEvent(ViewModelEvent.CLEAR), retry),
            BiFunction<T1, T2, ArrayList<Any>> { t1, t2 ->
                val arrayList = ArrayList<Any>()
                arrayList.add(t1 as Any)
                arrayList.add(t2 as Any)
                arrayList
            }).subscribe(object : Observer<List<Any>> {


        override fun onSubscribe(d: Disposable) {
            if (iDialog != IDialog.UN_LOADING) {
                progress.show(message)
            }
        }

        override fun onNext(data: List<Any>) {
            progress.hide()
            try {
                success(data[0] as T1, data[1] as T2)
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
                KLog.e("onError函数调用奔溃")
            }
            complete()
        }

        override fun onComplete() {
            complete()
        }

    })
}


/**
 * 组合 zip操作符号
 */
public inline fun <T1, T2, T3> BaseViewModel.convertZip(
        t1: Observable<Response<T1>>,
        t2: Observable<Response<T2>>,
        t3: Observable<Response<T3>>,
        // dialog呈现方式，两种：UN_LOADING(不显示),FORBID_LOADING(显示不关闭)
        iDialog: IDialog = IDialog.FORBID_LOADING,
        // 成功回调
        crossinline success: (T1, T2, T3) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 无论成功失败，之后都会调用
        noinline complete: () -> Unit = {},
        // 网络失败，是否重试请求
        retry: Boolean = true,
        //loading显示内容
        message: String = "loading"
) {
    Observable.zip(
            t1.convertRequest(bindUntilEvent(ViewModelEvent.CLEAR), retry),
            t2.convertRequest(bindUntilEvent(ViewModelEvent.CLEAR), retry),
            t3.convertRequest(bindUntilEvent(ViewModelEvent.CLEAR), retry), Function3<T1, T2, T3, ArrayList<Any>> { t1, t2, t3 ->
        val arrayList = ArrayList<Any>()
        arrayList.add(t1 as Any)
        arrayList.add(t2 as Any)
        arrayList.add(t3 as Any)
        arrayList
    }).subscribe(object : Observer<List<Any>> {


        override fun onSubscribe(d: Disposable) {
            if (iDialog != IDialog.UN_LOADING) {
                progress.show(message)
            }
        }

        override fun onNext(data: List<Any>) {
            progress.hide()
            try {
                success(data[0] as T1, data[1] as T2, data[2] as T3)
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
                KLog.e("onError函数调用奔溃")
            }
            complete()
        }

        override fun onComplete() {
            complete()
        }

    })
}
