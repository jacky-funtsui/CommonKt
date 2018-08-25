package com.ssf.framework.refreshlayout.ex

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import com.ssf.framework.refreshlayout.XRefreshLayout

/**
 * @author yedanmin
 * @data 2018/1/17 15:22
 * @describe RecyclerView LayoutManager 扩展方法
 */

/**
 * 初始化网格布局
 * @param context 上下文
 * @param orientation 布局方式 {@link #HORIZONTAL} or {@link #VERTICAL}
 * @param pageSize   一页最多显示几条
 */
public inline fun XRefreshLayout.setLinearLayoutManager(context: Context, orientation: Int = LinearLayoutManager.VERTICAL, pagerSize: Int = 20) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = orientation
    this.setLayoutManager(layoutManager,pagerSize)
}

/**
 * 初始化网格布局
 * @param context 上下文
 * @param span    每行的格数
 * @param pageSize   一页最多显示几条
 */
public inline fun XRefreshLayout.setStaggeredGridLayoutManager(context: Context, span:Int, pagerSize: Int = 20){
    val layoutManager = StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    this.setLayoutManager(layoutManager,pagerSize)
}


/**
 * 初始化网格布局
 * @param context 上下文
 * @param span    每行的格数
 * @param pageSize   一页最多显示几条
 */
public inline fun XRefreshLayout.setGridLayoutManager(context: Context, span:Int, pagerSize: Int = 20){
    val layoutManager = GridLayoutManager(context,span)
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    this.setLayoutManager(layoutManager,pagerSize)
}
