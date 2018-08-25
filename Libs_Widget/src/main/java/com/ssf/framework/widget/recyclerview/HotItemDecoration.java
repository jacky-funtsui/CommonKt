/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ssf.framework.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.ssf.framework.widget.R;


/**
 * <pre>
 *     mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
 *             mLayoutManager.getOrientation());
 *     recyclerView.addItemDecoration(mDividerItemDecoration);
 * </pre>
 */
public class HotItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DividerItem";

    private Drawable mDivider;

    private final Rect mBounds = new Rect();

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     *
     * @param context Current context, it will be used to access resources.
     */
    public HotItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.gray_line);
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            spanCount = 1;
        }
        return spanCount;
    }

    /**
     * 是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @return
     */
    private boolean isLastCol(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        int childCount = adapter.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.HORIZONTAL) {
                childCount -= childCount % spanCount;
                if (pos >= childCount) {
                    return true;
                }
            } else if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            return true;
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        int childCount = adapter.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            childCount -= childCount % spanCount;
            if (pos >= childCount) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == 1) {
                childCount -= childCount % spanCount;
                if (pos >= childCount) {
                    return true;
                }
            } else if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (pos == (childCount - 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (isLastRaw(parent, pos, getSpanCount(parent))) {
            outRect.set(0, 0, mDivider.getIntrinsicHeight(), 0);
        } else if (isLastCol(parent, pos, this.getSpanCount(parent))) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }
    }
}
