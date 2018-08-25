package com.ssf.framework.net.transformer

import com.ssf.framework.net.common.RetryWhenNetwork
import com.xm.xnet.exception.ApiException
import com.xm.xnet.exception.CodeException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * @author yedanmin
 * @data 2018/1/11 15:28
 * @describe
 */
class ConvertSchedulers<T> : ObservableTransformer<Response<T>,T>{
    override fun apply(upstream: Observable<Response<T>>): ObservableSource<T> =
            upstream
//                    .delay(5, TimeUnit.SECONDS)     //请求延迟五秒，再开始
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .flatMap { response ->
                        if (response.isSuccessful) {
                            Observable.just(response.body()!!)
                        } else {
                            val code = response.code()
                            val string = response.errorBody()!!.string()
                            Observable.error(ApiException(CodeException.NotSuccessfulException, code, string))
                        }
                    }.observeOn(AndroidSchedulers.mainThread(),true)
                    .retryWhen(RetryWhenNetwork())
}