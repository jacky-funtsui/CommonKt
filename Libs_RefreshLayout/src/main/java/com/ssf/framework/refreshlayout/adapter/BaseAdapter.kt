package com.ssf.framework.refreshlayout.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.Exception

/**
 * @author yedanmin
 * @data 2018/1/17 15:38
 * @describe
 */
abstract class BaseAdapter<T>(
        // 布局
        private val mLayoutID: Int,
        // data
        var mData: ArrayList<T>,
        // Item点击监听回调
        var mItemClickListener: OnItemClickListener<T>? = null,
        // layout 上 绑定的监听id
        vararg var mClickIDs: Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(mLayoutID, parent, false)
        // 绑定监听回调
        initializationItemClickListener(inflate)
        // 返回 baseViewHolder
        return BaseViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        //设置监听的Position
        holder.itemView.tag = position
        //简化处理后，回调
        val t = mData[position]
        convert(holder, t, position)
    }

    override fun getItemCount(): Int = mData.size

    /**
     * 获取绑定的数据源
     */
    fun getBindDataList() = mData

    /**
     *  初始化Item 回调
     *  @param inflate   绑定的布局
     */
    private fun initializationItemClickListener(inflate: View) {
        if (mClickIDs.isEmpty()) {
            // 给 root item 设置监听
            inflate.setOnClickListener {
                clickCallback(inflate,it)
            }
        } else {
            // 给 item 上面的 子 view 设置监听
            mClickIDs.forEach {
                inflate.findViewById<View>(it)?.setOnClickListener {
                    clickCallback(inflate,it)
                }
            }
        }
    }

    /** 监听到点击事件后，回调 */
    private fun clickCallback(inflate: View, it: View) {
        val pos = inflate.tag
        if (pos is Int) {
            //回调
            mItemClickListener?.click(it, this, mData[pos], pos)
        } else {
            Exception("root item 请勿使用setTag操作,影响逻辑")
        }
    }


    /**
     * 抽象方法，简化adapter过程
     * @param holder     item对应的 ViewHolder
     * @param bean       item对应的数据
     * @param position   item 在列表中的 下标
     */
    protected abstract fun convert(holder: BaseViewHolder, bean: T, position: Int)

    /**
     * item点击监听
     */
    interface OnItemClickListener<T>{
        /**
         *  点击的时候回调
         *  @param view      点击的View
         *  @param adapter   当前的adapter
         *  @param bean      获取到的数据结构
         *  @param position  点击的item
         */
        fun click(view:View, adapter: BaseAdapter<T>, bean:T, position: Int)
    }
}