package com.ssf.framework.net.donwload

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: ydm
 * @time: 2018/5/9
 * @说明: 下载拦截器
 */
class DownloadInterceptor(
        private val update: (String, Long, Long, Boolean) -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        return originalResponse.newBuilder()
                .body(DownloadResponseBody(request.url().toString(), originalResponse.body()!!, update))
                .build()
    }

}