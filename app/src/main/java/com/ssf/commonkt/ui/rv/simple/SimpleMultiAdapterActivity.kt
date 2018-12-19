package com.ssf.commonkt.ui.rv.simple

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
import com.ssf.framework.main.mvvm.adapter.BaseMultiBindingAdapter
import com.ssf.framework.widget.ex.toast

/**
 * Created by Hzz on 2018/9/29
 */
class SimpleMultiAdapterActivity : BaseActivity(R.layout.simple_recycler) {
    private val mRv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleMultiAdapter(this) }
    override fun init() {
        mRv.layoutManager = LinearLayoutManager(this)
        mRv.adapter = mAdapter

        mAdapter.itemClickListener = object : BaseBindingAdapter.OnItemClickListener<Int> {
            override fun click(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                toast("点击了Item $position")
            }
        }

        mAdapter.itemChildClickListener = object : BaseBindingAdapter.OnItemChildClickListener<Int> {
            override fun onItemChildClick(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                when (view.id) {
                    R.id.text -> toast("点击了位置:$position 的文本")
                    R.id.icon -> toast("点击了位置:$position 的Logo")
                }
            }
        }

        mAdapter.list.addAll(IntRange(0,15))
        mAdapter.notifyDataSetChanged()
    }

    override fun initStatusBar() {
    }

    class SimpleMultiAdapter(context: Context) : BaseMultiBindingAdapter<Int>(context) {
        override fun getMultiLayoutId(data: Int): Int {
            return when (data % 4) {
                0 -> R.layout.item_1
                1 -> R.layout.item_2
                2 -> R.layout.item_3
                3 -> R.layout.item_4
                else -> {
                    R.layout.item_1
                }
            }
        }

        override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: Int, position: Int) {
            holder.addOnClickListener(R.id.text, R.id.icon)
        }
    }
}