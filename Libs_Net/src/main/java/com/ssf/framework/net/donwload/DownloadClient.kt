package com.ssf.framework.net.donwload

import android.content.Context
import android.os.AsyncTask.execute
import android.os.Environment
import com.ssf.framework.net.donwload.cache.DownInfo
import com.ssf.framework.net.donwload.cache.DownInfoDbUtil
import com.ssf.framework.net.donwload.interfac.DownState
import com.ssf.framework.net.donwload.interfac.IDownloadFinished
import com.ssf.framework.net.interceptor.HeaderInterceptor
import com.xm.xlog.KLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


/**
 * @author admin
 * @data 2018/4/25
 * @describe  下载项目构建
 */
class DownloadClient(
        private val context: Context,
        private val service: IDownloadService
) : IDownloadFinished {
    /**
     * 最大下载队列
     */
    private val maxRequests = 5
    /**
     * 正在运行的队列
     */
    private val runningDownInfos = ArrayDeque<DownInfo>()

    /**
     * 准备运行的队列
     */
    private val readyDownInfos = ArrayDeque<DownInfo>()

    companion object {

        /**
         * 初始化创建Retrofit 对象
         * @param builder  建造者模式构造
         * @param cls      接口类
         */
        fun createRetrofit(builder: Builder, cls: Class<IDownloadService>): DownloadClient {
            KLog.i("创建 download -> createRetrofit")
            //初始化OkHttp
            val okHttpBuilder = OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                    .connectTimeout(builder.connectionTimeout, TimeUnit.SECONDS)
            // header
            okHttpBuilder.addInterceptor(HeaderInterceptor(builder.headers))
            // 拦截器
            okHttpBuilder.addInterceptor(DownloadInterceptor({ url: String, readCount: Long, totalCount: Long, done: Boolean ->
                // 刷新数据库
                DownloadService.update(builder.context, url, readCount, totalCount, done)
            }))
            // 构建
            val iDownloadService = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpBuilder.build())
                    .baseUrl(builder.baseUrl)
                    .build()
                    .create(cls)
            // 返回下载对象
            return DownloadClient(builder.context, iDownloadService)
        }

    }

    //------------------------------------ 队列的操作 ------------------------------------------

    /**
     * 通过下载地址，获取 队列中的 DownInfo
     */
    private fun findDownInfos(downloadUrl: String):DownInfo?{
        runningDownInfos.forEach {
            if (downloadUrl == it.url){
                return it
            }
        }
        readyDownInfos.forEach {
            if (downloadUrl == it.url){
                return it
            }
        }
        return null
    }

    /**
     * 如果队列执行完，判断是否执行 准备队列
     */
    private fun promoteCalls(){
        if (runningDownInfos.size >= maxRequests) return  // Already running max capacity.
        if (readyDownInfos.isEmpty()) return  //如果为空返回

        val i = readyDownInfos.iterator()
        while (i.hasNext()) {
            val call = i.next()
            i.remove()
            runningDownInfos.add(call)
            // 执行
            execute(call)
            // 是否到达最大容量
            if (runningDownInfos.size >= maxRequests) return
        }
    }

    /**
     * 入队，并开始下载
     * @param downInfo 操作的对象类
     */
    private fun enqueue(downInfo: DownInfo) {
        // 加入队列
        if (runningDownInfos.size < maxRequests) {
            runningDownInfos.add(downInfo)
            // 执行下载
            execute(downInfo)
        } else {
            // 等待
            downInfo.downState = DownState.WAIT.state
            readyDownInfos.add(downInfo)
            // 发送消息
            EventBus.getDefault().post(downInfo)
        }
    }

    /**
     * 下载完成
     */
    override fun finished(downloadUrl: String){
        synchronized(this) {
            val downInfos = findDownInfos(downloadUrl)
            if (!runningDownInfos.remove(downInfos)) throw AssertionError("Call wasn't in-flight!")
            // 下一条
            promoteCalls()
        }
    }

    /**
     * 取消时出列
     */
    private fun cancel(downInfo: DownInfo){
        val downInfos = findDownInfos(downInfo.url)
        if (downInfos != null){
            if (!runningDownInfos.remove(downInfos)) throw AssertionError("Call wasn't in-flight!")
        }
        promoteCalls()
    }


    //------------------------------------ 队列的操作 ------------------------------------------

    /**
     * 开始下载
     * @param downInfo 操作的对象类
     */
    private fun execute(downInfo: DownInfo){
        // 设置观察者
        val subscriber = DownloadSubscriber<DownInfo>(context, downInfo.url,this)
        downInfo.downloadSubscriber = subscriber
        // 开始下载
        service.download("bytes=" + downInfo.readLength + "-", downInfo.url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map({
                    // 写入缓存
                    DownloadFileUtil.writeCache(it, downInfo)
                    downInfo
                })
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(subscriber)
    }

    /**
     * 暂停下载
     * @param downInfo 操作的对象类
     */
    fun pause(downInfo: DownInfo) {
        downInfo.downState = DownState.PAUSE.state
        //关闭流
        val subscriber = downInfo.downloadSubscriber
        subscriber?.dispose()
        //保存到数据库
        DownInfoDbUtil.getInstance(context).update(downInfo)
        // 出列,并执行预备队列
        cancel(downInfo)
    }

    /**
     * 创建对象，如果存在返回，不存在创建
     * @param downloadUrl 下载地址
     * @param fileName    保存的文件名，不传为 下载程序自动判断
     */
    fun createDownInfo(downloadUrl: String, fileName: String? = null):DownInfo{
        val downInfo = queryDownInfo(downloadUrl, fileName)
        if (downInfo.state == DownState.DOWN){
            downInfo.downState = DownState.PAUSE.state
            // 更新
            DownInfoDbUtil.getInstance(context).update(downInfo,false)
        }
        return downInfo
    }

    /**
     * 查询对象，如果存在返回，不存在创建
     * @param downloadUrl 下载地址
     * @param fileName    保存的文件名，不传为 下载程序自动判断
     */
    fun queryDownInfo(downloadUrl: String, fileName: String? = null):DownInfo{
        val query = DownloadFileUtil.queryDownInfo(context, downloadUrl, fileName)
        val infos = findDownInfos(downloadUrl)
        if (query != null){
            infos?.let { query.downloadSubscriber = infos.downloadSubscriber }
        }else{
            infos?.let { runningDownInfos.remove(it) }
        }
        return query ?: DownloadFileUtil.createDownInfo(context, downloadUrl, fileName)
    }

    /**
     * 重新下载
     * @param downloadUrl 下载地址
     * @param fileName    保存的文件名，不传为 下载程序自动判断
     */
    fun againDownload(downloadUrl: String, fileName: String? = null) {
        // 查询最新
        val downInfo = queryDownInfo(downloadUrl, fileName)
        // 删除掉队列
        val downInfos = findDownInfos(downInfo.url)
        if (downInfos != null){
            if (runningDownInfos.contains(downInfo)){
                runningDownInfos.remove(downInfos)
            }
            if (readyDownInfos.contains(downInfo)){
                readyDownInfos.remove(downInfos)
            }
        }
        // 重新下载
        downInfo.reset()
        enqueue(downInfo)
    }

    /**
     * 下载
     * @param downloadUrl 下载地址
     * @param fileName    保存的文件名，不传为 下载程序自动判断
     */
    fun download(downloadUrl: String, fileName: String? = null) {
        // 查询最新
        val downInfo = queryDownInfo(downloadUrl, fileName)
        // 下载状态
        val state = downInfo.state
        when (state) {
            DownState.NORMAL, DownState.WAIT, DownState.PAUSE -> enqueue(downInfo)
            DownState.ERROR -> {
                // 出现异常重新下载
                downInfo.reset()
                enqueue(downInfo)
            }
            DownState.DOWN -> pause(downInfo)
            DownState.FINISH -> EventBus.getDefault().post(downInfo)
            else -> KLog.e("other")
        }
    }

    /**
     * 移除队列，不再下载
     */
    fun remove(downloadUrl: String, fileName: String? = null) {
        // 查询最新
        val downInfo = queryDownInfo(downloadUrl, fileName)
        // 暂停
        pause(downInfo)
        // 移除
        DownInfoDbUtil.getInstance(context).delete(downInfo)
    }


    /**
     * Builder 模式构建配置
     */
    class Builder(
            // 上下文
            val context: Context,
            // 是否开启调试输出
            val debug: Boolean = false,
            // 当前http网络请求
            val baseUrl: String,
            // 读取超时 - 默认6秒
            val readTimeout: Long = 30,
            // 超时链接 - 默认6秒
            val connectionTimeout: Long = 30,
            // 针对一些下载需要使用 自定义头部
            val headers: () -> HashMap<String, String> = { HashMap() }
    ) {

        fun create() = DownloadClient.createRetrofit(this, IDownloadService::class.java)
    }
}