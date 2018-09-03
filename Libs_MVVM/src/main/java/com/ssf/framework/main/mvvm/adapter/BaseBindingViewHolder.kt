package com.ssf.framework.main.mvvm.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * @atuthor ydm
 * @data on 2018/8/8
 * @describe
 */
class BaseBindingViewHolder<out T:ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)