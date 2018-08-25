package com.ssf.framework.main.mvvm.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.Exception

/**
 * @atuthor ydm
 * @data on 2018/8/8
 * @describe
 */
abstract class BaseBindingAdapter<T, B : ViewDataBinding>(
        context: Context,
        private val layoutID: Int,
        val list: ArrayList<T> = ArrayList(),
        // Item点击监听回调
        var itemClickListener: BaseBindingAdapter.OnItemClickListener<T>? = null,
        // layout 上 绑定的监听id
        vararg var clickIDs: Int
) : RecyclerView.Adapter<BaseBindingViewHolder<B>>() {

    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    open fun getLayoutId(viewType: Int): Int {
        return layoutID//默认返回传入的layout，可重写
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> {
        val layoutId = getLayoutId(viewType)
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutId, parent, false)
        // 绑定监听回调
        initializationItemClickListener(binding.root)
        // 创建
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<B>, position: Int) {
        holder.binding.root.tag = position
        val bean = list[position]
        convert(holder, bean, position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     *  初始化Item 回调
     *  @param inflate   绑定的布局
     */
    private fun initializationItemClickListener(inflate: View) {
        if (clickIDs.isEmpty()) {
            // 给 root item 设置监听
            inflate.setOnClickListener {
                clickCallback(inflate, it)
            }
        } else {
            // 给 item 上面的 子 view 设置监听
            clickIDs.forEach {
                inflate.findViewById<View>(it)?.setOnClickListener {
                    clickCallback(inflate, it)
                }
            }
        }
    }

    /** 监听到点击事件后，回调 */
    private fun clickCallback(inflate: View, it: View) {
        val pos = inflate.tag
        if (pos is Int) {
            //回调
            itemClickListener?.click(it, this, list[pos], pos)
        } else {
            throw Exception("root item 请勿使用setTag操作,影响逻辑")
        }
    }

    protected abstract fun convert(holder: BaseBindingViewHolder<B>, bean: T, position: Int)


    /**
     * item点击监听
     */
    interface OnItemClickListener<T> {
        /**
         *  点击的时候回调
         *  @param view      点击的View
         *  @param adapter   当前的adapter
         *  @param bean      获取到的数据结构
         *  @param position  点击的item
         */
        fun click(view: View, adapter: BaseBindingAdapter<T, *>, bean: T, position: Int)
    }
}

