package com.ssf.framework.main.mvvm.adapter

import android.content.Context
import android.databinding.ViewDataBinding

/**
 * 多布局适配器
 * Created by Hzz on 2018/9/28
 */
abstract class BaseMultiBindingAdapter<T>(context: Context, list: ArrayList<T> = ArrayList(), itemClickListener: OnItemClickListener<T>? = null) : BaseBindingAdapter<T, ViewDataBinding>(context, 0, list, itemClickListener) {

    constructor(context: Context) : this(context, ArrayList())

    /**
     * 以layoutId作为viewType
     */
    override fun getDefItemViewType(position: Int): Int {
        return getMultiLayoutId(list[position])
    }

    /**
     * 以viewType作为布局进行加载
     */
    override fun getLayoutId(viewType: Int): Int {
        return viewType
    }

    abstract fun getMultiLayoutId(data: T): Int

    override fun onBindBindingViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        val bean = list[position] ?: return
        convertBefore(holder, bean, position)
        convert(holder, bean, position)
        convertAfter(holder, bean, position)
    }
}