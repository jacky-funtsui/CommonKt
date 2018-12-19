package com.ssf.commonkt.ui.rv.simple

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.ssf.commonkt.R
import com.ssf.framework.main.activity.BaseActivity
import com.ssf.framework.main.ex.bindView
import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter
import com.ssf.framework.main.mvvm.adapter.BaseBindingViewHolder
import com.ssf.framework.widget.ex.toast

/**
 * Created by Hzz on 2018/9/29
 */
class SimpleHeaderFooterActivity : BaseActivity(R.layout.simple_recycler) {
    private  val mRv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleListAdapter(this) }
    override fun init() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        mRv.layoutManager = gridLayoutManager

        mAdapter.itemClickListener = object :BaseBindingAdapter.OnItemClickListener<Int>{
            override fun click(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                toast("点击了Item $position")
            }
        }

        mAdapter.itemChildClickListener = object :BaseBindingAdapter.OnItemChildClickListener<Int>{
            override fun onItemChildClick(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                when (view.id) {
                    R.id.text -> toast("点击了位置:$position 的文本")
                    R.id.icon -> toast("点击了位置:$position 的Logo")
                }
            }
        }

        mAdapter.isHeaderViewAsFlow=false //是否和默认Item占用权重一样
        mAdapter.isFooterViewAsFlow=false
        mAdapter.addHeader(LayoutInflater.from(this).inflate(R.layout.activity_welcome,null))
        mAdapter.addFooter(LayoutInflater.from(this).inflate(R.layout.activity_welcome,null))

        mRv.adapter = mAdapter



        mAdapter.list.addAll(IntRange(0,5))
        mAdapter.notifyDataSetChanged()
    }

    override fun initStatusBar() {
    }

    class SimpleListAdapter(context:Context) :BaseBindingAdapter<Int,ViewDataBinding>(context,R.layout.item_4){
        override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: Int, position: Int) {
            holder.addOnClickListener(R.id.text, R.id.icon)
        }
    }
}