package com.ssf.framework.main.mvvm.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import com.ssf.framework.main.mvvm.adapter.delegate.ProviderDelegate

/**
 * 多布局代理适配器，适用大量多布局解耦使用
 * Created by Hzz on 2018/9/28
 */
abstract class BaseDelegateBindingAdapter<T>(context: Context) : BaseMultiBindingAdapter<T>(context) {
    /**
     * 多布局ItemProvider
     */
    private val providerDelegate: ProviderDelegate by lazy { createProviderDelegate() }

    /**
     * 在复写getMultiLayoutId方法中返回对应的viewType
     * 布局通过viewType去provider中取，必须有注册
     */
    override fun getLayoutId(viewType: Int): Int {
        val itemProvider = providerDelegate.itemProviders[viewType]
        itemProvider?.let {
            return it.layout()
        } ?: throw RuntimeException("未注册viewType=$viewType 的Provider")
    }

    /**
     * 创建委托
     */
    abstract fun createProviderDelegate(): ProviderDelegate

    override fun convert(holder: BaseBindingViewHolder<ViewDataBinding>, bean: T, position: Int) {
        //交给Provider去执行具体的逻辑
        val itemProvider = providerDelegate.itemProviders.get(getDefItemViewType(position))
        itemProvider?.convert(holder, bean, position)
    }


    override fun initializationItemChildClickListener(inflate: View, holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        super.initializationItemChildClickListener(inflate, holder, position)
        //没有设置child监听器时，click父类逻辑不会走注册，所以需要注册事件
        val itemProvider = providerDelegate.itemProviders.get(getDefItemViewType(position))
        itemProvider?.let {
            if (itemChildClickListener == null && holder.childClickViewIds.isNotEmpty()) {
                holder.childClickViewIds.forEach {
                    inflate.findViewById<View>(it)?.setOnClickListener {
                        notifyItemChildClick(it, position)
                    }
                }
            }

            if (itemChildLongClickListener == null && holder.childLongClickViewIds.isNotEmpty()) {
                holder.childLongClickViewIds.forEach {
                    inflate.findViewById<View>(it)?.setOnLongClickListener {
                        notifyItemChildLongClick(it, position)
                    }
                }
            }
        }
    }

    override fun notifyItemChildClick(view: View, position: Int) {
        //回调监听到Provider
        val itemProvider = providerDelegate.itemProviders.get(getDefItemViewType(position))
        itemProvider?.onClick(view, list[position], position)
        super.notifyItemChildClick(view, position)
    }

    override fun notifyItemChildLongClick(view: View, position: Int): Boolean {
        //回调监听到Provider
        val itemProvider = providerDelegate.itemProviders.get(getDefItemViewType(position))
        itemProvider?.onLongClick(view, list[position], position)
        return super.notifyItemLongClick(view, position)
    }

}