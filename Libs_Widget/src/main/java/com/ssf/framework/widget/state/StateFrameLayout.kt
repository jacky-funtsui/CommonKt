package com.ssf.framework.widget.state

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import com.ssf.framework.autolayout.AutoFrameLayout


/**
 * @author admin
 * @data 2018/4/26
 * @describe 状态布局
 */
open class StateFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : AutoFrameLayout(context, attrs, defStyleAttr) {
    private val mLayoutUtil by lazy {
        StateLayoutUtil(this)
    }

    /**
     * 刷新状态布局
     */
    var stateLayout: IStateLayout
        get() = mLayoutUtil.getStateLayout()
        set(stateLayout) = mLayoutUtil.setStateLayout(stateLayout)


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mLayoutUtil.recoveryLoadingLayout()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mLayoutUtil.onFinishInflate()
    }


}
