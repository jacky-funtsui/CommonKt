package com.ssf.framework.widget.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: robert
 * @date: 2017-10-14
 * @time: 16:55
 * @说明:
 */
public class DotView extends View {
    private Paint mPaint;
    private int mWidth;
    private int minWidth = 12;
    private int gap = 10;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
        paint.setAlpha(80);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        mPaint = paint;
        //关闭硬件加速
        if (!isHardwareAccelerated()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i <= mWidth / (minWidth + gap); i++) {
            canvas.drawLine(i * (minWidth + gap), 0, i * (minWidth + gap) + minWidth, 0, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint.setStrokeWidth(h);
        mWidth = w;
    }
}