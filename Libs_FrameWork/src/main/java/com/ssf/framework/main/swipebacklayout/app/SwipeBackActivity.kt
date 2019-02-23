package com.ssf.framework.main.swipebacklayout.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.ssf.framework.main.swipebacklayout.SwipeBackLayout
import com.ssf.framework.main.swipebacklayout.Utils
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity


open class SwipeBackActivity : RxAppCompatActivity(), SwipeBackActivityBase {
    private val mHelper: SwipeBackActivityHelper by lazy { SwipeBackActivityHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun <T : View> findViewById(id: Int): T? {
        val v = super.findViewById<T>(id)
        return v ?: mHelper.findViewById(id)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }

    open fun onSwipeBackStateChange(state: Int, scrollPercent: Float) {
        if (state == SwipeBackLayout.STATE_SETTLING) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }
        }
    }
}
