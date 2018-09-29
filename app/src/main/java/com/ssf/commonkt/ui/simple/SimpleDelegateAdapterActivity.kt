package com.ssf.commonkt.ui.simple

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ssf.commonkt.R
import com.ssf.commonkt.databinding.Item1Binding
import com.ssf.commonkt.databinding.Item2Binding
import com.ssf.commonkt.databinding.Item3Binding
import com.ssf.commonkt.databinding.Item4Binding
import com.ssf.framework.main.activity.BaseActivity
import com.ssf.framework.main.ex.bindView
import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter
import com.ssf.framework.main.mvvm.adapter.BaseBindingViewHolder
import com.ssf.framework.main.mvvm.adapter.BaseDelegateBindingAdapter
import com.ssf.framework.main.mvvm.adapter.delegate.BaseItemProvider
import com.ssf.framework.main.mvvm.adapter.delegate.ProviderDelegate
import com.ssf.framework.widget.ex.toast

/**
 * Created by Hzz on 2018/9/29
 */
class SimpleDelegateAdapterActivity : BaseActivity(R.layout.simple_recycler) {
    private val mRv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleDelegateAdapter(this) }
    override fun init() {
        mRv.layoutManager = LinearLayoutManager(this)
        mRv.adapter = mAdapter

        mAdapter.itemClickListener = object : BaseBindingAdapter.OnItemClickListener<Int> {
            override fun click(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
                toast("点击了Item $position")
            }
        }

//        mAdapter.itemChildClickListener = object : BaseBindingAdapter.OnItemChildClickListener<Int> {
//            override fun onItemChildClick(view: View, adapter: BaseBindingAdapter<Int, *>, bean: Int, position: Int) {
//                when (view.id) {
//                    R.id.text -> toast("点击了位置:$position 的文本")
//                    R.id.icon -> toast("点击了位置:$position 的Logo")
//                }
//            }
//        }

        mAdapter.list.addAll(IntRange(0,15))
        mAdapter.notifyDataSetChanged()
    }

    override fun initStatusBar() {
    }

    class SimpleDelegateAdapter(context: Context) : BaseDelegateBindingAdapter<Int>(context) {
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

        override fun createProviderDelegate(): ProviderDelegate {
            return ProviderDelegate(
                    ItemProvider1(),
                    ItemProvider2(),
                    ItemProvider3(),
                    ItemProvider4()
            )
        }


        override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: Int, position: Int) {
            super.convert(holder, bean, position)
            //注册公共事件，也可以在具体的Item中取注册
            holder.addOnClickListener(R.id.text, R.id.icon)
            holder.addLongClickListener(R.id.icon)
        }
    }

    class ItemProvider1 : BaseItemProvider<Int, BaseBindingViewHolder<Item1Binding>>() {
        override fun layout(): Int {
            return R.layout.item_1
        }

        override fun convert(holder: BaseBindingViewHolder<Item1Binding>, data: Int, position: Int) {
        }

        override fun onClick(view: View, data: Int, position: Int) {
            super.onClick(view, data, position)
            view.context.toast("点击了Provider1 child")
        }
    }

    class ItemProvider2 : BaseItemProvider<Int, BaseBindingViewHolder<Item2Binding>>() {
        override fun layout(): Int {
            return R.layout.item_2
        }

        override fun convert(holder: BaseBindingViewHolder<Item2Binding>, data: Int, position: Int) {
        }

        override fun onClick(view: View, data: Int, position: Int) {
            super.onClick(view, data, position)
            view.context.toast("点击了Provider2 child")
        }

        override fun onLongClick(view: View, data: Int, position: Int) {
            super.onLongClick(view, data, position)
            view.context.toast("长按了Provider2 child")
        }
    }

    class ItemProvider3 : BaseItemProvider<Int, BaseBindingViewHolder<Item3Binding>>() {
        override fun layout(): Int {
            return R.layout.item_3
        }

        override fun convert(holder: BaseBindingViewHolder<Item3Binding>, data: Int, position: Int) {
        }

        override fun onClick(view: View, data: Int, position: Int) {
            super.onClick(view, data, position)
            view.context.toast("点击了Provider3 child")
        }
    }

    class ItemProvider4 : BaseItemProvider<Int, BaseBindingViewHolder<Item4Binding>>() {
        override fun layout(): Int {
            return R.layout.item_4
        }

        override fun convert(holder: BaseBindingViewHolder<Item4Binding>, data: Int, position: Int) {
        }

        override fun onClick(view: View, data: Int, position: Int) {
            super.onClick(view, data, position)
            view.context.toast("点击了Provider4 child")
        }
    }
}