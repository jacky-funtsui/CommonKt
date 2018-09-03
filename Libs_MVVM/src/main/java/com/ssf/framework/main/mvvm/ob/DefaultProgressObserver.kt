package com.ssf.framework.main.mvvm.ob

import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import android.view.View
import com.ssf.framework.main.mvvm.vm.SuperViewModelProvider
import com.ssf.framework.main.mvvm.livedata.ProgressLiveData
import com.ssf.framework.widget.dialog.ProgressDialog

/**
 * Created by hzz on 2018/8/18.
 */
class DefaultProgressObserver(val owner: FragmentActivity) : Observer<ProgressLiveData.Progress> {
    companion object {
        const val PROGRESS_TAG = -100
    }
    private val progressDialog by lazy { getProgress() }


    override fun onChanged(it: ProgressLiveData.Progress?) {
        val show = it?.show ?: false
        if (show) {
//            progressDialog.setMessage(it?.message)
            progressDialog.show()
            progressDialog.setCancelable(it?.cancelable ?:false)
        } else if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    /**
     * 获取Activity内唯一的ProgressDialog,避免多个VM对象操作造成多个Progress同时显示问题
     */
    private fun getProgress(): ProgressDialog {
        val root = owner.findViewById<View>(android.R.id.content)
        val progress = root.getTag(PROGRESS_TAG)
        if (progress != null) {
            return progress as ProgressDialog
        }
        val dialog = ProgressDialog(owner)
        root.setTag(PROGRESS_TAG, dialog)
        return dialog
    }
}