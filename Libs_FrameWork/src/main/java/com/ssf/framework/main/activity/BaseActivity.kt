package com.ssf.framework.main.activity

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.ssf.framework.autolayout.AutoConstraintLayout
import com.ssf.framework.autolayout.AutoFrameLayout
import com.ssf.framework.autolayout.AutoLinearLayout
import com.ssf.framework.autolayout.AutoRelativeLayout
import com.ssf.framework.main.ex.IConfig
import com.ssf.framework.main.swipebacklayout.app.SwipeBackActivity
import com.ssf.framework.main.util.StatusBarUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import com.umeng.analytics.MobclickAgent
import com.xm.xlog.KLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit
import android.content.res.TypedArray
import android.annotation.SuppressLint


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
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            val result = fixOrientation();
            KLog.d("BaseActivity", "onCreate fixOrientation when Oreo, result = $result");
        }
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
                        findViewById<View>(id)?.setOnClickListener(this)
                    }
                }
            }
        }).compose(bindUntilEvent(ActivityEvent.DESTROY))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    try {
                        onClick(it)
                    } catch (e: Exception) {
                        KLog.e("BaseActivity", "点击事件有异常，异常信息为: ${e.message}")
                    }
                }
    }

    /** 设置状态栏  https://github.com/laobie/StatusBarUtil */
    open fun initStatusBar() {
        if (statusBarColor != 0) {
            StatusBarUtil.setColor(this, statusBarColor)
        } else {
            StatusBarUtil.setTranslucentForImageView(this, 0, findViewById(com.ssf.framework.main.R.id.toolbar))
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


    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            KLog.i("BaseActivity", "avoid calling setRequestedOrientation when Oreo.");
            return
        }
        super.setRequestedOrientation(requestedOrientation)
    }

    @SuppressLint("PrivateApi")
    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window").get(null) as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m = ActivityInfo::class.java.getMethod("isTranslucentOrFloating", TypedArray::class.java)
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    private fun fixOrientation(): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val activityInfo: ActivityInfo = field.get(this) as ActivityInfo
            activityInfo.screenOrientation = -1
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}