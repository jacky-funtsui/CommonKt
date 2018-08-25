package com.ssf.framework.main.activity

import android.os.Bundle

abstract class BackHandledFragment(
        layoutResID: Int,
        vararg ids: Int = intArrayOf(0)
) : BaseFragment(layoutResID, *ids) {

    protected var mBackHandledInterface: BackHandledInterface? = null

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    abstract fun onBackPressed(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity !is BackHandledInterface) {
            throw ClassCastException("Hosting Activity must implement BackHandledInterface")
        } else {
            this.mBackHandledInterface = activity as BackHandledInterface?
        }
    }

    override fun onStart() {
        super.onStart()
        //告诉FragmentActivity，当前Fragment在栈顶
        mBackHandledInterface?.setSelectedFragment(this)
    }

}