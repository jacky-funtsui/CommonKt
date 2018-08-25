package com.ssf.framework.main.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

abstract class BaseFragment(
        private val layoutResID: Int,
        private vararg val ids: Int = intArrayOf(0)
) : RxFragment(), View.OnClickListener {

    /* 主视图 */
    var mInflate: View? = null
    // 是否初始化过
    private var mInit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mInflate == null) {
            mInflate = inflater.inflate(layoutResID, container, false)
        }
        // 初始化默认配置
        initDefaultConfig(savedInstanceState)
        // 返回
        return mInflate
    }

    /**
     * 初始化默认配置
     */
    open fun initDefaultConfig(savedInstanceState: Bundle?){
        if (!mInit) {
            mInit = true
            init(mInflate, savedInstanceState)
            //初始化监听
            setClickViewId(mInflate)
        }
    }

    /** 初始化 */
    abstract fun init(view: View?, savedInstanceState: Bundle?)

    /**
     * 简化 findViewById 过程
     */
    fun <T : View> findViewById(@IdRes id: Int): T {
        return mInflate!!.findViewById(id)
    }

    /**
     * 跳转页面
     *
     * @param clazz
     */
    fun startActivity(clazz: Class<out BaseActivity>) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
    }

    protected fun setClickViewId(view: View?) {
        view?.let {
            Observable.create(object : ObservableOnSubscribe<View>, View.OnClickListener {
                lateinit var emitter: ObservableEmitter<View>
                override fun onClick(v: View) {
                    emitter.onNext(v)
                }

                override fun subscribe(emitter: ObservableEmitter<View>) {
                    this.emitter = emitter
                    ids.forEach { id ->
                        if (id != 0) {
                            it.findViewById<View>(id).setOnClickListener(this)
                        }
                    }
                }
            }).compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe({ onClick(it) })
        }
    }


    override fun onClick(v: View) {
    }

    open fun refresh() {

    }
}