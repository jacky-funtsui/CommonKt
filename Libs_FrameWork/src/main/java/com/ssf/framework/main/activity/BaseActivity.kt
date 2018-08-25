package com.ssf.framework.main.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.ssf.framework.autolayout.AutoConstraintLayout
import com.ssf.framework.autolayout.AutoFrameLayout
import com.ssf.framework.autolayout.AutoLinearLayout
import com.ssf.framework.autolayout.AutoRelativeLayout
import com.ssf.framework.main.R
import com.ssf.framework.main.ex.IConfig
import com.ssf.framework.main.swipebacklayout.app.SwipeBackActivity
import com.ssf.framework.main.util.StatusBarUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import com.umeng.analytics.MobclickAgent
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

/**
 * BaseActivity基础类
 */
abstract class BaseActivity(
        // 自定义布局
        private val layoutResID: Int,
        //需要设置点击事件的ViewId
        private vararg val ids: Int = intArrayOf(0),
        // 是否可以滑动退出，默认true
        private val swipeBackLayoutEnable: Boolean = true,
        // StatusBar颜色
        private val statusBarColor: Int = 0,
        // StatusBar 透明度 (0 - 255)
        private val statusBarAlpha: Int = 0
) : SwipeBackActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        // 初始化默认配置
        initDefaultConfig()
    }

    open fun setContentView() {
        //设置布局
        setContentView(layoutResID)
    }

    /**
     * 初始化默认配置
     */
    open fun initDefaultConfig() {
        //初始化状态栏
        initStatusBar()
        //滑动退出,是否打开
        setSwipeBackEnable(swipeBackLayoutEnable)
        //初始化
        init()
        //初始化监听
        setClickViewId()
    }

    private fun setClickViewId() {
        val disposable = Observable.create(object : ObservableOnSubscribe<View>, View.OnClickListener {
            lateinit var emitter: ObservableEmitter<View>
            override fun onClick(v: View) {
                emitter.onNext(v)
            }

            override fun subscribe(emitter: ObservableEmitter<View>) {
                this.emitter = emitter
                ids.forEach { id ->
                    if (id != 0) {
                        findViewById<View>(id).setOnClickListener(this)
                    }
                }
            }
        }).compose(bindUntilEvent(ActivityEvent.DESTROY))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe { onClick(it) }
    }

    /** 设置状态栏  https://github.com/laobie/StatusBarUtil */
    open fun initStatusBar() {
        if (statusBarColor != 0) {
            StatusBarUtil.setColor(this, statusBarColor)
        } else {
            StatusBarUtil.setTranslucentForImageView(this, 0, findViewById(R.id.toolbar))
        }
    }


    /**
     * 屏幕适配
     */
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return when (name) {
            IConfig.LAYOUT_FRAMELAYOUT -> AutoFrameLayout(context, attrs)
            IConfig.LAYOUT_LINEARLAYOUT -> AutoLinearLayout(context, attrs)
            IConfig.LAYOUT_RELATIVELAYOUT -> AutoRelativeLayout(context, attrs)
            IConfig.LAYOUT_CONSTRAINTLAYOUT -> AutoConstraintLayout(context, attrs)
            else -> super.onCreateView(name, context, attrs)
        }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    /** 初始化 */
    abstract fun init()

    override fun onClick(v: View) {
        val disposable = Observable.just(v)
    }

}