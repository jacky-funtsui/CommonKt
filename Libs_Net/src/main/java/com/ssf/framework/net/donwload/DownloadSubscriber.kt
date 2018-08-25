package com.ssf.framework.net.donwload

import android.content.Context
import com.ssf.framework.net.donwload.cache.DownInfoDbUtil
import com.ssf.framework.net.donwload.interfac.DownState
import com.ssf.framework.net.donwload.interfac.IDownloadFinished
import com.xm.xlog.KLog
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * @author 小民
 * @time 2017/7/21
 * @说明 ：下载的一些问题
 */

class DownloadSubscriber<T>(
        private val context: Context,
        private val downloadUrl: String,
        private val finished: IDownloadFinished
) : Observer<T> {
    //用于关闭上下流
    private var disposable: Disposable? = null

    override fun onSubscribe(@NonNull d: Disposable) {
        disposable = d
        update(DownState.DOWN)
    }

    override fun onNext(@NonNull t: T) {
        KLog.e( "DownloadSubscriber -> onNext")
    }

    override fun onError(@NonNull e: Throwable) {
        KLog.e("DownloadSubscriber -> onError：" + e.message)
        update(DownState.ERROR)
    }

    override fun onComplete() {
        KLog.e("DownloadSubscriber -> onComplete")
        // 回调
        finished.finished(downloadUrl)
        // 刷新数据库
        update(DownState.FINISH)
    }

    /** 更新状态  */
    private fun update(state: DownState) {
        //查询
        val query = DownInfoDbUtil.getInstance(context).query(downloadUrl)
        //更新到数据库
        query.downState = state.state
        DownInfoDbUtil.getInstance(context).update(query)
    }

    /** 关闭  */
    fun dispose() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable?.dispose()
        }
    }
}
