package com.ssf.framework.refreshlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssf.framework.widget.state.IStateLayout;

import java.util.List;

/**
 * @author yedanmin
 * @data 2018/1/18 17:53
 * @describe
 */

public class LoadMoreRecyclerView extends RecyclerView {
    // 加载更多布局类型
    private int TYPE_FOOTER = 80001;
    // 是否开启加载更多
    private boolean mLoadingMoreEnabled = false;
    // 包裹的adapter
    private WrapAdapter mWrapAdapter;
    // 加载更多的事件
    private LoadMoreListener mLoadMoreListener;
    // 是否允许加载更多事件触发
    private boolean mIsLoadMoreListener = false;
    // 主控件
    private XRefreshLayout mXRefreshLayout;
    // 是否正在处于加载更多
    private boolean mIsLoadingMoreIng = false;
    // 页数刚好 80 条 每页20的时候的 bug (假定条件)
    private boolean mIsLoadingError = false;
    // 数据改变监听
    private RecyclerView.AdapterDataObserver mDataObserver;


    public LoadMoreRecyclerView(Context context, XRefreshLayout refreshLayout) {
        super(context);
        mXRefreshLayout = refreshLayout;
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        // 数据改变监听
        if (mDataObserver == null){
            mDataObserver = new WrapDataObserver();
            // 注册
            adapter.registerAdapterDataObserver(mDataObserver);
            mDataObserver.onChanged();
        }
    }

    //避免用户自己调用getAdapter() 引起的ClassCastException
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null)
            return mWrapAdapter.getOriginalAdapter();
        else
            return null;
    }

    /**
     * 强制设置为加载已完成
     * @param isLoadingError
     */
    public void setLoadingError(boolean isLoadingError) {
        mIsLoadingError = isLoadingError;
    }

    /**
     * 是否开启加载更多
     * @param loadingMoreEnabled
     */
    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.mLoadingMoreEnabled = loadingMoreEnabled;
    }

    /**
     * 加载更多的事件
     * @param mLoadMoreListener
     */
    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        mLoadingMoreEnabled = true;
        this.mLoadMoreListener = mLoadMoreListener;
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return mWrapAdapter.getItemViewType(position) == TYPE_FOOTER ? 1 : gridManager.getSpanCount();
                    }
                });

            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingMoreEnabled && mLoadMoreListener != null) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && mWrapAdapter.getItemCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount() && mIsLoadMoreListener && !mIsLoadingMoreIng) {
                mIsLoadingMoreIng = true;
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 是否满足显示底部的要求，有些时候，不满足一页
     */
    private boolean isDisplayLoadMoreLayout() {
        // 不需要直接返回
        if (!mLoadingMoreEnabled) return false;
        // 如果是第一页
        if (mXRefreshLayout.mPagerNo == 1) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null || layoutManager.getItemCount() < mXRefreshLayout.mPagerSize) {
                // 第一页都不满足，直接不显示
                mIsLoadMoreListener = false;
                return false;
            }
            mIsLoadMoreListener = true;
        }
        return true;
    }

    /**
     * 是否显示数据加载到底
     */
    private boolean isLoadMoreFinish() {
        // 如果是第一页
        if (mXRefreshLayout.mPagerNo > 1) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null ||
                    layoutManager.getItemCount() < mXRefreshLayout.mPagerNo * mXRefreshLayout.mPagerSize ||
                    (mIsLoadingError && layoutManager.getItemCount() == mXRefreshLayout.mPagerNo * mXRefreshLayout.mPagerSize )) {
                // 不够一页
                mIsLoadMoreListener = false;
                return true;
            }
            mIsLoadMoreListener = true;
            return false;
        } else {

            return false;
        }
    }

    /**
     * 设置加载更多的状态
     */
    public void setLoadingMoreIng(boolean loadingMoreIng) {
        mIsLoadingMoreIng = loadingMoreIng;
    }

    /**
     * 刷新布局，主要针对空布局
     */
    public void updateDataObserver() {
        mDataObserver.onChanged();
    }

    /**
     * 状态改变
     */
    private class WrapDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                int itemCount = mWrapAdapter.getItemCount();
                IStateLayout layout = mXRefreshLayout.getStateLayout();
                if (layout == IStateLayout.NORMAL && itemCount == 0) {
                    mXRefreshLayout.setStateLayout(IStateLayout.EMPTY);
                } else {
                    if (layout == IStateLayout.EMPTY && itemCount != 0) {
                        mXRefreshLayout.setStateLayout(IStateLayout.NORMAL);
                    }
                }
            }
        }
    }

    /**
     * 包裹adapter
     */
    private class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

        public WrapAdapter(Adapter<ViewHolder> adapter) {
            this.adapter = adapter;
        }

        /* 包裹的adapter */
        public RecyclerView.Adapter getOriginalAdapter() {
            return adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_refresh_footer, parent, false);
                return new SimpleViewHolder(footerView);
            } else {
                return adapter.createViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) != TYPE_FOOTER) {
                adapter.onBindViewHolder(holder, position);
            } else {
                onLoadMoreLayoutBind(holder, position, null);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (getItemViewType(position) != TYPE_FOOTER) {
                if (payloads == null || payloads.isEmpty()) {
                    adapter.onBindViewHolder(holder, position);
                } else {
                    adapter.onBindViewHolder(holder, position, payloads);
                }
            } else {
                onLoadMoreLayoutBind(holder, position, payloads);
            }
        }

        /**
         * 加载更多布局的 bindViewHolder 事件
         */
        private void onLoadMoreLayoutBind(ViewHolder holder, int position, List<Object> payloads) {
            TextView tvFooter = holder.itemView.findViewById(R.id.listview_foot_more);
            if (isLoadMoreFinish()) {
                holder.itemView.findViewById(R.id.listview_foot_progress).setVisibility(View.GONE);
                tvFooter.setText(R.string.footer_load_end);
            } else {
                holder.itemView.findViewById(R.id.listview_foot_progress).setVisibility(View.VISIBLE);
                tvFooter.setText(R.string.footer_load_ing);
            }
        }

        @Override
        public int getItemCount() {
            if (isDisplayLoadMoreLayout()) {
                int count = adapter.getItemCount();
                return count == 0 ? 0 : count + 1;
            } else {
                return adapter.getItemCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position >= adapter.getItemCount()) {
                return TYPE_FOOTER;
            }
            return adapter.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return adapter.getItemId(position);
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (getItemViewType(position) == TYPE_FOOTER) ? 1 : gridManager.getSpanCount();
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && getItemViewType(holder.getLayoutPosition()) == TYPE_FOOTER) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }


        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

    /**
     * 回调处理
     */
    private interface LoadMoreListener {
        void onLoadMore();
    }
}
