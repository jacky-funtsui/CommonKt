package com.ssf.commonkt.ui.lazy

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import com.ssf.commonkt.R
import com.ssf.framework.main.mvvm.activity.MVVMFragment
import com.xm.xlog.KLog

class SampleLazyLogFragment : MVVMFragment<ViewDataBinding>(R.layout.fragment_sample_lazy) {
    companion object {
        fun newInstance(title: String): SampleLazyLogFragment {
            val fragment = SampleLazyLogFragment()
            fragment.title = title
            return fragment
        }
    }

    private val TAG by lazy { "SampleLazy-$title" }
    private var title: String? = null

    override fun init(view: View?, savedInstanceState: Bundle?) {
        KLog.d(TAG, "init")
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        KLog.d(TAG, "onLazyLoad")
    }

    override fun onUserVisible() {
        super.onUserVisible()
        KLog.d(TAG, "onUserVisible")
        mInflate!!.id
    }

    override fun onUserInVisible() {
        super.onUserInVisible()
        KLog.d(TAG, "onUserInVisible")
    }
}