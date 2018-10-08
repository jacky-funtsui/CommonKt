package com.ssf.framework.main.mvvm.adapter

import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import java.util.*

/**
 * @atuthor ydm
 * @data on 2018/8/8
 * @describe
 */
class BaseBindingViewHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {
    val childClickViewIds by lazy { LinkedHashSet<Int>() }
    val childLongClickViewIds by lazy { LinkedHashSet<Int>() }

    /**
     * 给childView注册点击监听
     */
    fun addOnClickListener(@IdRes vararg viewId: Int) {
        viewId.forEach {
            childClickViewIds.add(it)
        }
    }

    /**
     * 给childView注册长按监听
     */
    fun addLongClickListener(@IdRes vararg viewId: Int) {
        viewId.forEach {
            childLongClickViewIds.add(it)
        }
    }

    fun <BINDING> getBinding(): BINDING {
        return binding as BINDING
    }
}