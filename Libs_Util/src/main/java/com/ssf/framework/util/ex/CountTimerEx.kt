package com.ssf.framework.util.ex

import android.view.View
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxDialogFragment
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * @author admin
 * @data 2018/4/26
 * @describe  倒计时扩展
 */

/**
 * 倒计时功能
 * @param activity 用于绑定生命周期
 * @param count    倒计时最大
 * @param isEnable 是否禁止按钮
 * @param next     每一秒回调
 * @param complete 完成回调
 */
inline fun View.bindCountTimer(activity: RxAppCompatActivity, count: Long, isEnable: Boolean = false, crossinline next: (Long) -> Unit, crossinline complete: () -> Unit): Disposable {
    if (isEnable) {
        this.isEnabled = false
    } else {
        // 移除 onclick事件，变为点击 调用完成
        this.setOnClickListener {
            complete()
        }
    }
    return Observable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({
                next(count - it - 1)
                // 已完成
                if (count == it + 1) {
                    if (isEnable) {
                        this.isEnabled = true
                    }
                    complete()
                }
            }, { print(it.message) })
}

inline fun View.bindCountTimer(activity: RxFragment, count: Long, isEnable: Boolean = false, crossinline next: (Long) -> Unit, crossinline complete: () -> Unit):Disposable {
    if (isEnable) {
        this.isEnabled = false
    }
    return Observable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .compose(activity.bindUntilEvent<Long>(FragmentEvent.DESTROY))
            .doOnNext {
                next(count - it - 1)
                // 已完成
                if (count == it + 1) {
                    if (isEnable) {
                        this.isEnabled = true
                    }
                    complete()
                }
            }.subscribe()
}

inline fun View.bindCountTimer(activity: RxDialogFragment, count: Long, isEnable: Boolean = false, crossinline next: (Long) -> Unit, crossinline complete: () -> Unit):Disposable {
    if (isEnable) {
        this.isEnabled = false
    }
    return Observable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .compose(activity.bindUntilEvent<Long>(FragmentEvent.DESTROY))
            .doOnNext {
                next(count - it - 1)
                // 已完成
                if (count == it + 1) {
                    if (isEnable) {
                        this.isEnabled = true
                    }
                    complete()
                }
            }.subscribe()
}