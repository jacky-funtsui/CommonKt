package com.ssf.commonkt.ui.main

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ssf.commonkt.R
import com.ssf.commonkt.data.config.IRouterConfig
import com.ssf.commonkt.databinding.ActivityMainBinding
import com.ssf.commonkt.databinding.Item1Binding
import com.ssf.commonkt.ui.simple.*
import com.ssf.framework.main.ex.bindView
import com.ssf.framework.main.mvvm.activity.MVVMActivity
import com.ssf.framework.main.mvvm.adapter.BaseBindingAdapter
import com.ssf.framework.main.mvvm.adapter.BaseBindingViewHolder

@Route(path = IRouterConfig.AR_PATH_MAIN)
class MainActivity : MVVMActivity<ActivityMainBinding>(R.layout.activity_main), BaseBindingAdapter.OnItemClickListener<MainActivity.Bean> {

    override fun click(view: View, adapter: BaseBindingAdapter<Bean, *>, bean: Bean, position: Int) {
        startActivity(Intent(this, bean.target))
    }

    private val rv by bindView<RecyclerView>(R.id.rv)
    private val mAdapter by lazy { SimpleListAdapter(this, this) }
    override fun initStatusBar() {

    }

    override fun init() {
        // 多布局
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mAdapter
        mAdapter.list.add(Bean("常规Adapter", SimpleListAdapterActivity::class.java))
        mAdapter.list.add(Bean("兼容ClickIds Adapter", SimpleSupportAdapterActivity::class.java))
        mAdapter.list.add(Bean("头部尾部Adapter", SimpleHeaderFooterActivity::class.java))
        mAdapter.list.add(Bean("多布局MultiAdapter", SimpleMultiAdapterActivity::class.java))
        mAdapter.list.add(Bean("多布局DelegateAdapter", SimpleDelegateAdapterActivity::class.java))
        mAdapter.notifyDataSetChanged()
    }

    class SimpleListAdapter(context: Context, itemClickListener: OnItemClickListener<Bean>) : BaseBindingAdapter<Bean, Item1Binding>(context, R.layout.item_1, itemClickListener = itemClickListener) {
        override fun convert(holder: BaseBindingViewHolder<Item1Binding>, bean: Bean, position: Int) {
            holder.binding.text.text = bean.name
        }
    }

    class Bean(val name: String, val target: Class<*>)
}
