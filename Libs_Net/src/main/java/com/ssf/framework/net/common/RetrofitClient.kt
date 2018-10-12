package com.ssf.framework.net.common

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ssf.framework.net.ex.IConfig
import com.ssf.framework.net.interceptor.HeaderInterceptor
import com.xm.xlog.KLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @author admin
 * @data 2018/4/25
 * @describe
 */
class RetrofitClient {
    companion object {
        /**
         * 初始化创建Retrofit 对象
         */
        fun <T> createRetrofit(builder: Builder<T>, cls: Class<T>): T {
            KLog.i("创建 createRetrofit")
            // 构建 log
            val loggingInterceptor = if (builder.debug) {
                HttpLoggingInterceptor {
                    // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                    if ((it.startsWith("{") && it.endsWith("}")
                                    || (it.startsWith("[") && it.endsWith("]")))) {
                        KLog.json(IConfig.tag, it)
                    } else {
                        KLog.i(IConfig.tag, it)
                    }

                }
            } else {
                null
            }

            //初始化OkHttp
            val okHttpBuilder = OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                    .connectTimeout(builder.connectionTimeout, TimeUnit.SECONDS)
            // header
            okHttpBuilder.addInterceptor(HeaderInterceptor(builder.headers))
                    .addNetworkInterceptor(StethoInterceptor()) //添加fackebook Stetho
            // log
            loggingInterceptor?.apply {
                level = HttpLoggingInterceptor.Level.BODY
                okHttpBuilder.addInterceptor(this)
            }
            // 构建
            return Retrofit.Builder()
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpBuilder.build())
                    // 多域名
                    .baseUrl(builder.baseUrl)
                    .build()
                    .create(cls)
        }
    }


    /**
     * Builder 模式构建配置
     */
    class Builder<out T>(
            private val cls: Class<T>,
            // 是否开启调试输出
            val debug: Boolean = false,
            // 当前http网络请求
            val baseUrl: String,
            // 读取超时 - 默认6秒
            val readTimeout: Long = 6,
            // 超时链接 - 默认6秒
            val connectionTimeout: Long = 6,
            // 头部
            val headers: () -> HashMap<String, String>
    ) {

        fun create() = RetrofitClient.createRetrofit(this, cls)
    }
}