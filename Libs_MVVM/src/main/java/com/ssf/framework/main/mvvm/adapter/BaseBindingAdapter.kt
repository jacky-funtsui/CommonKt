package com.ssf.framework.main.mvvm.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ssf.framework.autolayout.utils.AutoUtils
import com.ssf.framework.main.adapter.BaseViewHolder
import java.util.LinkedHashSet
import kotlin.collections.ArrayList

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
        var itemClickListener: BaseBindingAdapter.OnItemClickListener<T>? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = -100
        const val VIEW_TYPE_FOOTER = -101
    }

    //兼容旧API,保存childItem点击事件
    protected val clickIDs by lazy { LinkedHashSet<Int>() }

    @Deprecated("给子布局设置监听，请使用holder中的addClickListener方法,会回调在OnItemChildClickListener中")
    constructor(context: Context, layoutID: Int, list: ArrayList<T> = ArrayList(), itemClickListener: BaseBindingAdapter.OnItemClickListener<T>? = null, vararg clickIDs: Int) : this(context, layoutID, list, itemClickListener) {
        //兼容旧API
        clickIDs.forEach {
            if (it > 0) {
                this.clickIDs.add(it)
            }
        }
    }

    var itemLongClickListener: OnItemLongClickListener<T>? = null
    var itemChildClickListener: OnItemChildClickListener<T>? = null
    var itemChildLongClickListener: OnItemChildLongClickListener<T>? = null

    private var mHeaderLayout: LinearLayout? = null
    private var mFooterLayout: LinearLayout? = null
    //头部占span是否和其它item相同
    var isHeaderViewAsFlow = false
    //尾部占span是否和其它item相同
    var isFooterViewAsFlow = false

    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    open fun getLayoutId(viewType: Int): Int {
        return layoutID//默认返回传入的layout，可重写实现多布局
    }

    override fun getItemViewType(position: Int): Int {
        mHeaderLayout?.let {
            if (position == 0) return VIEW_TYPE_HEADER
        }

        mFooterLayout?.let {
            if (position == getHeaderLayoutCount() + list.size) return VIEW_TYPE_FOOTER
        }
        return getDefItemViewType(position - getHeaderLayoutCount())//取在数据中的下标
    }

    /**
     * 复写，以实现多布局
     */
    open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                createBaseViewHolder(parent, viewType, mHeaderLayout!!)
            }
            VIEW_TYPE_FOOTER -> {
                createBaseViewHolder(parent, viewType, mFooterLayout!!)
            }
            else -> {
                createBindingViewHolder(parent, viewType)
            }
        }
    }

    private fun createBaseViewHolder(parent: ViewGroup, viewType: Int, itemView: View): BaseViewHolder {
        return BaseViewHolder(itemView)
    }

    private fun createBindingViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> {
        val layoutId = getLayoutId(viewType)
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutId, parent, false)
                ?: throw RuntimeException("layoutID 必须是以根布局为<layout/>的DataBinding方式创建")
        // 创建
        AutoUtils.auto(binding.root)
        //
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            VIEW_TYPE_HEADER -> {
            }//undo
            VIEW_TYPE_FOOTER -> {
            }//undo
            else -> {
                //实际上绑定数据的ViewHolder
                onBindBindingViewHolder(holder as BaseBindingViewHolder<B>, position - getHeaderLayoutCount())
            }
        }
    }

    open fun onBindBindingViewHolder(holder: BaseBindingViewHolder<B>, position: Int) {
        holder.binding.root.tag = position
        val bean = list[position]
        convertBefore(holder, bean, position)
        convert(holder, bean, position)
        convertAfter(holder, bean, position)
    }

    override fun getItemCount(): Int {
        return getDataItemCount() + getHeaderLayoutCount() + getFooterLayoutCount()
    }

    open fun getDataItemCount(): Int {
        return list.size
    }

    /**
     * RecyclerView需要先设置manager再设置adapter才有效
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val sizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    if (viewType == VIEW_TYPE_HEADER && isHeaderViewAsFlow) {
                        return 1
                    }
                    if (viewType == VIEW_TYPE_FOOTER && isFooterViewAsFlow) {
                        return 1
                    }
                    if (isFixedViewType(viewType)) {
                        return layoutManager.spanCount
                    }
                    return sizeLookup.getSpanSize(position - getHeaderLayoutCount())
                }
            }
        }
    }

    private fun isFixedViewType(viewType: Int): Boolean {
        return when (viewType) {
            VIEW_TYPE_HEADER,
            VIEW_TYPE_FOOTER -> true
            else -> false
        }
    }

    /**
     * 数据绑定前的操作
     */
    protected open fun convertBefore(holder: BaseBindingViewHolder<B>, bean: T, position: Int) {

    }

    abstract fun convert(holder: BaseBindingViewHolder<B>, bean: T, position: Int)

    /**
     * 数据绑定后的操作
     */
    protected open fun convertAfter(holder: BaseBindingViewHolder<B>, bean: T, position: Int) {
        // 绑定监听回调
        initializationItemListener(holder.itemView, holder, position)
        holder.binding.executePendingBindings()
    }

    /**
     *  初始化Item监听
     *  @param inflate   绑定的布局
     */
    protected open fun initializationItemListener(inflate: View, holder: BaseBindingViewHolder<B>, position: Int) {
        initializationItemClickListener(inflate, holder, position)//item监听
        initializationItemChildClickListener(inflate, holder, position)//childItem监听
    }

    /**
     *  Item 点击回调
     *  @param inflate   绑定的布局
     */
    protected open fun initializationItemClickListener(inflate: View, holder: BaseBindingViewHolder<B>, position: Int) {
        //点击
        itemClickListener?.let {
            // 给 root item 设置监听
            inflate.setOnClickListener {
                notifyItemClick(inflate, position)
            }

            //兼容以前的逻辑, 给item上面的子view 设置监听
            if (clickIDs.isNotEmpty()) {
                //如果没有设置itemClickListener兼容回调到原来的itemListener
                if (itemChildClickListener == null) {
                    clickIDs.forEach {
                        inflate.findViewById<View>(it)?.setOnClickListener {
                            notifyItemClick(it, position)
                        }
                    }
                } else {
                    //有设置itemChildClickListener后就回调到childItem的监听
                    clickIDs.forEach {
                        if (it > 0) {
                            holder.addOnClickListener(it) //注册事件
                        }
                    }
                }
            }
        }

        //长按
        itemLongClickListener?.let {
            inflate.setOnLongClickListener {
                notifyItemLongClick(inflate, position)
            }
        }
    }

    protected open fun notifyItemClick(view: View, position: Int) {
        itemClickListener?.click(view, this, list[position], position)
    }

    protected open fun notifyItemLongClick(view: View, position: Int): Boolean {
        return itemLongClickListener?.onLongClick(view, this, list[position], position) ?: false
    }

    /**
     * Item 子View点击回调
     */
    protected open fun initializationItemChildClickListener(inflate: View, holder: BaseBindingViewHolder<B>, position: Int) {
        //点击
        itemChildClickListener?.let {
            holder.childClickViewIds.forEach {
                if (it > 0) {
                    inflate.findViewById<View>(it)?.setOnClickListener {
                        notifyItemChildClick(it, position)
                    }
                }
            }
        }

        //长按
        itemChildLongClickListener?.let {
            holder.childLongClickViewIds.forEach {
                if (it > 0) {
                    inflate.findViewById<View>(it)?.setOnLongClickListener {
                        notifyItemChildLongClick(it, position)
                    }
                }
            }
        }
    }

    protected open fun notifyItemChildClick(view: View, position: Int) {
        itemChildClickListener?.onItemChildClick(view, this, list[position], position)
    }

    protected open fun notifyItemChildLongClick(view: View, position: Int): Boolean {
        return itemChildLongClickListener?.onItemChildLongClick(view, this, list[position], position)
                ?: false
    }

    /**
     * 添加头部
     * @param index
     */
    fun addHeader(view: View, index: Int = -1): Int {
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(view.context)
            mHeaderLayout?.orientation = LinearLayout.VERTICAL
            mHeaderLayout?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        var insertIndex = 0
        val childCount = mHeaderLayout!!.childCount
        if (index < 0 || index > childCount) {
            insertIndex = childCount
        }

        mHeaderLayout!!.addView(view, insertIndex)
        if (mHeaderLayout!!.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return insertIndex
    }

    /**
     * 添加尾部
     */
    fun addFooter(view: View, index: Int = -1): Int {
        if (mFooterLayout == null) {
            mFooterLayout = LinearLayout(view.context)
            mFooterLayout?.orientation = LinearLayout.VERTICAL
            mFooterLayout?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        var insertIndex = 0
        val childCount = mFooterLayout!!.childCount
        if (index < 0 || index > childCount) {
            insertIndex = childCount
        }

        mFooterLayout!!.addView(view, insertIndex)
        if (mFooterLayout!!.childCount == 1) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return insertIndex
    }

    fun removeHeader(view: View) {
        mHeaderLayout?.let {
            it.removeView(view)
            if (it.childCount == 0) {
                val position = getHeaderViewPosition()
                if (position != -1) {
                    notifyItemRemoved(position)
                }
            }
        }
    }

    fun removeFooter(view: View) {
        mFooterLayout?.let {
            it.removeView(view)
            if (it.childCount == 0) {
                val position = getFooterViewPosition()
                if (position != -1) {
                    notifyItemRemoved(position)
                }
            }
        }
    }

    fun getHeaderLayoutCount(): Int {
        return mHeaderLayout?.let {
            if (getHeaderCount() > 0) 1 else 0
        } ?: 0
    }

    fun getFooterLayoutCount(): Int {
        return mFooterLayout?.let {
            if (getFooterCount() > 0) 1 else 0
        } ?: 0
    }

    protected fun getHeaderCount(): Int {
        return mHeaderLayout?.childCount ?: 0
    }

    protected fun getFooterCount(): Int {
        return mFooterLayout?.childCount ?: 0
    }

    fun getHeaderViewPosition(): Int {
        mHeaderLayout?.let { return 0 }
        return -1
    }

    fun getFooterViewPosition(): Int {
        mFooterLayout?.let { return getHeaderCount() + list.size }
        return -1
    }

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

    interface OnItemLongClickListener<T> {
        fun onLongClick(view: View, adapter: BaseBindingAdapter<T, *>, bean: T, position: Int): Boolean
    }

    interface OnItemChildClickListener<T> {
        fun onItemChildClick(view: View, adapter: BaseBindingAdapter<T, *>, bean: T, position: Int)
    }

    interface OnItemChildLongClickListener<T> {
        fun onItemChildLongClick(view: View, adapter: BaseBindingAdapter<T, *>, bean: T, position: Int): Boolean
    }
}

