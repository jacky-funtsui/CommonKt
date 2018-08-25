package com.ssf.framework.refreshlayout.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * 作者：小民
 * 功能：多布局专用适配器
 * 时间：2017/10/17
 */

abstract class MulCommonAdapter<T>(
    /* Data */
    var data: ArrayList<T>) : RecyclerView.Adapter<BaseViewHolder>() {
    /* 监听列表 id */
    private val mClickIDs = ArrayList<Int>()
    /* 多功能监听 */
    private var mItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //多布局
        val typeView = createTypeView(viewType)
        //创建View
        val inflate = LayoutInflater.from(parent.context).inflate(typeView, parent, false)
        //复杂监听
        setItemClick(inflate)
        //返回
        return BaseViewHolder(inflate)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        //设置监听的Position
        holder.itemView.tag = position
        //回调
        val t = data[position]
        convert(holder, t, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 为Item设置简单监听事件
     */
    private fun setItemClick(inflate: View) {
        if (mClickIDs.size > 0 && mItemClickListener != null) {
            for (id in mClickIDs) {
                val viewById = inflate.findViewById<View>(id)
                if (viewById == null) {
                    Log.e("tag", "error not findViewById")
                    continue
                }
                itemClick(inflate, viewById)
            }
        } else {
            if (mItemClickListener != null){
                itemClick(inflate, inflate)
            }
        }
    }

    /**
     * Item Click 会调
     */
    private fun itemClick(inflate: View, child: View) {
        child.setOnClickListener { v ->
            val tag = inflate.tag
            if (tag is Int) {
                //简单回调存在时
                val t = data[tag]
                mItemClickListener!!.onItemChildClick(v, this@MulCommonAdapter, t, tag)
            } else {
                Log.e("tag", "inflate 请勿使用setTag操作,影响逻辑")
            }
        }
    }

    /**
     * 多功能监听
     **/
    interface OnItemClickListener<T> {
        fun onItemChildClick(v: View, adapter: MulCommonAdapter<T>, bean: T, position: Int)
    }

    /* 设置监听事件 */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>, vararg viewIds: Int) {
        mClickIDs.addAll(viewIds.toList())
        mItemClickListener = onItemClickListener
    }

    /* 抽象数据方法，创建对布局 */
    protected abstract fun createTypeView(viewType: Int): Int

    /* 抽象数据方法，简化Adapter */
    protected abstract fun convert(holder: BaseViewHolder, bean: T, position: Int)
}

