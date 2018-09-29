package com.ssf.commonkt.ui.simple

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ssf.commonkt.R
import com.ssf.framework.main.activity.BaseActivity
import com.ssf.framework.main.ex.bindView
import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter
import com.ssf.framework.main.mvvm.adapter.BaseBindingViewHolder
import com.ssf.framework.widget.ex.toast

/**
 * 兼容以前的Adapter使用方式
 * Created by Hzz on 2018/9/29
 */
class SimpleSupportAdapterActivity : BaseActivity(R.layout.simple_recycler) {
    private val mRv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleListAdapter(this, R.id.text, R.id.icon) }
    override fun init() {
        mRv.layoutManager = LinearLayoutManager(this)
        mRv.adapter = mAdapter

        mAdapter.itemClickListener = object : BaseBindingAdapter.OnItemClickListener<Int> {
            override fun click(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                when (view.id) {
                    R.id.text -> toast("点击了位置:$position 的文本")
                    R.id.icon -> toast("点击了位置:$position 的Logo")
                    else -> toast("点击了Item $position")
                }
            }
        }

        mAdapter.list.addAll(IntRange(0,15))
        mAdapter.notifyDataSetChanged()
    }

    override fun initStatusBar() {
    }

    /**
     * 注意此构造已标记为过时，建议使用holder注册监听
     * 可参考常规使用
     * @see SimpleListAdapterActivity
     */
    class SimpleListAdapter(context: Context, vararg clickIds: Int) : BaseBindingAdapter<Int, ViewDataBinding>(context, R.layout.item_4, clickIDs = *(clickIds)) {
        override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: Int, position: Int) {

        }
    }
}