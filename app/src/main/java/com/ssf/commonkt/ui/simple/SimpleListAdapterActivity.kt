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
import com.ssf.framework.main.mvvm.adapter.BaseMultiBindingAdapter
import com.ssf.framework.widget.ex.toast

/**
 * 兼容以前的Adapter使用方式
 * Created by Hzz on 2018/9/29
 */
class SimpleListAdapterActivity : BaseActivity(R.layout.simple_recycler) {
    private  val mRv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleListAdapter(this) }
    override fun init() {
        mRv.layoutManager = LinearLayoutManager(this)
        mRv.adapter = mAdapter

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

        mAdapter.itemLongClickListener = object :BaseBindingAdapter.OnItemLongClickListener<Int>{
            override fun onLongClick(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int): Boolean {
                toast("长按了Item $position")
                return false
            }

        }

        mAdapter.itemChildLongClickListener = object :BaseBindingAdapter.OnItemChildLongClickListener<Int>{
            override fun onItemChildLongClick(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int): Boolean {
                when (view.id) {
                    R.id.text -> toast("长按了位置:$position 的文本")
                    R.id.icon -> toast("长按了位置:$position 的Logo")
                }
                return false
            }
        }

        mAdapter.list.addAll(IntRange(0,15))
        mAdapter.notifyDataSetChanged()
    }

    override fun initStatusBar() {
    }

    class SimpleListAdapter(context:Context) :BaseBindingAdapter<Int,ViewDataBinding>(context,R.layout.item_4){
        override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: Int, position: Int) {
            //给子View注册监听
            holder.addOnClickListener(R.id.text,R.id.icon)
            //给子View注册长按监听
            holder.addLongClickListener(R.id.text,R.id.icon)
        }
    }
}