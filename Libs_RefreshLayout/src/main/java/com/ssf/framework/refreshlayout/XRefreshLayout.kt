package com.ssf.framework.refreshlayout

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.Button
import com.ssf.framework.net.ex.convert
import com.ssf.framework.net.interfac.IDialog
import com.ssf.framework.widget.state.IStateLayout
import com.ssf.framework.widget.state.StateFrameLayout
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xm.xlog.KLog
import io.reactivex.Observable
import retrofit2.Response


/**
 * @author yedanmin
 * @data 2018/1/18 15:47
 * @describe  上下拉刷新
 */

class XRefreshLayout : android.support.v4.widget.SwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener {


    companion object {
        const val TAG = "XRefreshLayout"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 状态布局中 加入 recyclerView
        mStateLayout.addView(mLoadMoreRecyclerView)
        //状态布局
        addView(mStateLayout)
    }

    // 状态布局
    val mStateLayout: StateFrameLayout by lazy { StateFrameLayout(context) }
    // 内部包裹的  加载更多 RecyclerView
    val mLoadMoreRecyclerView: LoadMoreRecyclerView by lazy { LoadMoreRecyclerView(context, this) }
    // 当前加载的页
    @JvmField
    var mPagerNo = 1
    // 每页数量，用于判断数据是否到底
    @JvmField
    var mPagerSize = 0

    /**
     * 开关上拉下拉刷新功能
     */
    var isPullRefreshEnabled = true
    var isLoadingMoreEnabled = true
    // 下拉刷新
    var refreshCallback: (Int) -> Unit = {}


    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        mLoadMoreRecyclerView.setLoadingMoreEnabled(isLoadingMoreEnabled)
        mLoadMoreRecyclerView.adapter = adapter
        //下拉刷新
        isEnabled = isPullRefreshEnabled
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager, pageSize: Int) {
        mLoadMoreRecyclerView.layoutManager = layout
        mPagerSize = pageSize
    }

    /**
     * 当前状态
     */
    fun getStateLayout() = mStateLayout.stateLayout

    /**
     * 更新状态
     */
    fun setStateLayout(state: IStateLayout) {
        mStateLayout.stateLayout = state
    }

    /**
     * 手动触发下拉刷新
     */
    fun refresh() {
        isRefreshing = true
        onRefresh()
    }

    /**
     * 下拉刷新回调
     */
    override fun onRefresh() {
        KLog.i(TAG, "下拉刷新 page：$mPagerNo")
        mPagerNo = 1
        refreshCallback(mPagerNo)
    }

    /**
     * 设置下拉刷新
     */
    private fun setRefreshListener(refresh: (Int) -> Unit) {
        refreshCallback = refresh
        setOnRefreshListener(this)
    }

    /**
     * 设置加载更多
     */
    private fun setLoadMoreListener(loadMore: (Int) -> Unit) {
        if (isLoadingMoreEnabled) {
            mLoadMoreRecyclerView.setLoadMoreListener {
                KLog.i(TAG, "加载更多 page：$mPagerNo")
                mPagerNo++
                loadMore(mPagerNo)
            }
        }
    }

    /**
     * 请求网络 RxAppCompatActivity
     */
    private fun <T> request(transformer: LifecycleTransformer<T>, request: (Int) -> Observable<Response<T>>, success: (T, Int) -> Unit, error: (Throwable) -> Unit = {}) {
        request(mPagerNo)
                .convert(transformer,success = {
                    // 隐藏状态布局
                    if (mStateLayout.stateLayout == IStateLayout.LOADING) {
                        mStateLayout.stateLayout = IStateLayout.NORMAL
                    }
                    // 隐藏刷新
                    if (isRefreshing) {
                        isRefreshing = false
                    }
                    // 刚好一页时的bug
                    mLoadMoreRecyclerView.setLoadingError(false)
                    // 可以加载更多了
                    mLoadMoreRecyclerView.setLoadingMoreIng(false)
                    // 回调
                    success(it, mPagerNo)
                    // 是否为空布局
                    mLoadMoreRecyclerView.updateDataObserver()
                }, error = {
                    // 设置错误布局
                    if (mStateLayout.stateLayout == IStateLayout.LOADING) {
                        mStateLayout.stateLayout = IStateLayout.REFRESH
                    }
                    // 隐藏刷新
                    if (isRefreshing) {
                        isRefreshing = false
                    }
                    // 刚好一页时的bug
                    mLoadMoreRecyclerView.setLoadingError(true)
                    // 回调
                    error(it)
                })

    }


    fun <T> setRefreshListener(transformer: LifecycleTransformer<T>, request: (Int) -> Observable<Response<T>>, success: (T, Int) -> Unit, error: (Throwable) -> Unit = {}) {
        // 首次请求
        if (!isRefreshing) {
            // 状态布局
            mStateLayout.stateLayout = IStateLayout.LOADING
            findViewById<Button>(R.id.arr_btn_onload).setOnClickListener {
                mPagerNo = 1
                mStateLayout.stateLayout = IStateLayout.LOADING
                request(transformer, request, success, error)
            }
            // 请求
            request(transformer, request, success, error)
        }
        // 下拉刷新
        setRefreshListener {
            request(transformer, request, success, error)
        }
        // 加载更多
        setLoadMoreListener {
            request(transformer, request, success, error)
        }
    }

    fun <T> setRefreshListener(fragment: RxFragment, request: (Int) -> Observable<Response<T>>, success: (T, Int) -> Unit, error: (Throwable) -> Unit = {}) {
        val rx = fragment.bindUntilEvent<T>(FragmentEvent.DESTROY)
        setRefreshListener(rx, request, success, error)
    }

    fun <T> setRefreshListener(activity: RxAppCompatActivity, request: (Int) -> Observable<Response<T>>, success: (T, Int) -> Unit, error: (Throwable) -> Unit = {}) {
        val rx = activity.bindUntilEvent<T>(ActivityEvent.DESTROY)
        setRefreshListener(rx,request, success, error)
    }


}
