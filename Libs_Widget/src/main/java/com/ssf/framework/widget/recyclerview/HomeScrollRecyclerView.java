package com.ssf.framework.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author: robert
 * @date: 2017-12-02
 * @time: 19:49
 * @说明:
 */
public class HomeScrollRecyclerView extends RecyclerView {
    public HomeScrollRecyclerView(Context context) {
        super(context);
    }

    public HomeScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        e.setAction(MotionEvent.ACTION_CANCEL);
        return super.onTouchEvent(e);
    }
}
