package com.ssf.framework.net.ex

import com.ssf.framework.net.common.ResponseListener
import com.ssf.framework.net.common.ResponseSubscriber
import com.ssf.framework.net.transformer.ApplySchedulers
import com.ssf.framework.net.transformer.ConvertSchedulers
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xm.xlog.KLog
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Response
import java.util.*

/**
 * @atuthor ydm
 * @data on 2018/8/7
 * @describe
 */

public inline fun <T> Observable<T>.apply(
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 成功后，并执行完 success 方法后回调
        noinline complete: () -> Unit = {},
        retry: Boolean = true
) {
    this.compose(ApplySchedulers(retry))
            .subscribe(ResponseSubscriber(responseListener = object : ResponseListener<T> {

                override fun onSucceed(data: T) {
                    try {
                        success(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(exception: Throwable) {
                    try {
                        error(exception)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        KLog.e("onError函数调用奔溃")
                    }
                }

                override fun onComplete() {
                    complete()
                }
            }))
}

/**
 * activity网络请求扩展
 */
public inline fun <T> Observable<Response<T>>.convert(
        // 成功回调
        noinline success: (T) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 成功后，并执行完 success 方法后回调
        noinline complete: () -> Unit = {},
        // 网络失败，是否重试请求
        retry: Boolean = true
) {
    this.compose(ConvertSchedulers(retry))
            .subscribe(ResponseSubscriber(responseListener = object :
                    ResponseListener<T> {

                override fun onSucceed(data: T) {
                    try {
                        success(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onError(e)
                    }
                }

                override fun onError(exception: Throwable) {
                    try {
                        error(exception)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        KLog.e("onError函数调用奔溃")
                    }
                }

                override fun onComplete() {
                    complete()
                }
            }))
}


/**
 * 组合 zip操作符号
 */
public inline fun <T1, T2> RxFragment.convertZip(
        t1: Observable<Response<T1>>,
        t2: Observable<Response<T2>>,
        // 成功回调
        crossinline success: (T1, T2) -> Unit,
        // 失败回调
        noinline error: (Throwable) -> Unit = {},
        // 成功后，并执行完 success 方法后回调
        noinline complete: () -> Unit = {},
        retry: Boolean = true
) {
    Observable.zip(t1.convertRequest(this, retry), t2.convertRequest(this, retry), BiFunction<T1, T2, ArrayList<Any>> { t1, t2 ->
        val arrayList = ArrayList<Any>()
        arrayList.add(t1 as Any)
        arrayList.add(t2 as Any)
        arrayList
    }).subscribe(ResponseSubscriber(responseListener = object : ResponseListener<ArrayList<Any>> {
        override fun onSucceed(data: ArrayList<Any>) {
            try {
                success(data[0] as T1, data[1] as T2)
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e)
            }
        }

        override fun onError(exception: Throwable) {
            try {
                error(exception)
            } catch (e: Exception) {
                e.printStackTrace()
                KLog.e("onError函数调用奔溃")
            }
        }

        override fun onComplete() {
            complete()
        }
    }))
}
