package com.ssf.framework.net.common

import android.app.Activity
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import com.ssf.framework.net.ex.IConfig.Companion.NET_PROGRESS_TAG
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.widget.dialog.ProgressDialog
import com.xm.xlog.KLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * @author admin
 * @data 2018/4/25
 * @describe
 */

/**
 * @author yedanmin
 * @data 2018/1/11 16:04
 * @describe
 */
@Deprecated(message = "This class is no longer supported, do not use it.")
class ProgressSubscriber<T>(
        //上下文对象
        private val activity: Activity,
        /* 显示dialog的方式 */
        private val iDialog: IDialog = IDialog.UN_LOADING,
        /* 回调 */
        private val responseListener: ResponseListener<T>
) : Observer<T> {
    /* Disposable */
    private var disposable: Disposable? = null
    /* 加载进度条 */
    private val progressDialog by lazy { ProgressDialog(activity) }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        //显示进度条，如果有的话
        showProgressDialog()
    }

    override fun onNext(t: T) {
        responseListener.onSucceed(t)
    }

    override fun onComplete() {
        //销毁进度条，如果有的话
        dismissDialog()
        //回调
        responseListener.onComplete()
    }

    override fun onError(e: Throwable) {
        //销毁进度条，如果有的话
        dismissDialog()
        // 回调失败
        responseListener.onError(e)
    }


    /** 显示ProgressDialog */
    private fun showProgressDialog() {
        if (iDialog != IDialog.UN_LOADING) {
            when (iDialog) {
                IDialog.NORMAL_LOADING -> progressDialog.setCancelable(true)
                IDialog.FORBID_LOADING -> progressDialog.setCancelable(false)
                else -> {
                    //什么都不干，暂时
                }
             }
            if (!activity.isFinishing){
                progressDialog.show()
                progressDialog.setDismissCallback {
                    if (disposable != null) {
                        KLog.i("销毁进度条，并关闭异步!!!")
                        disposable?.dispose()
                    }
                }
            }
        }
    }

    /** 销毁dialog */
    private fun dismissDialog() {
        disposable = null
        if (iDialog != IDialog.UN_LOADING && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}