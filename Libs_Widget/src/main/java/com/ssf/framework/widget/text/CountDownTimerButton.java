package com.ssf.framework.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ssf.framework.widget.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: xiong
 * @time: 2017/09/05
 * @说明:
 */

public class CountDownTimerButton extends AppCompatButton implements View.OnClickListener {

    private OnClickListener mOnClickListener;
    private long duration = 10;//倒计时时长 设置默认10秒
    private String text;//点击前

    private int textColor;
    private Drawable bg;
    private Disposable mDisposable;

    public CountDownTimerButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public CountDownTimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CountDownTimerButton);
        text = mTypedArray.getString(R.styleable.CountDownTimerButton_text);
        duration = mTypedArray.getInt(R.styleable.CountDownTimerButton_countdown, 1000);
        textColor = mTypedArray.getColor(R.styleable.CountDownTimerButton_textColor, Color.GRAY);
        bg = mTypedArray.getDrawable(R.styleable.CountDownTimerButton_bg);
        setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {//提供外部访问方法
        if (onClickListener instanceof CountDownTimerButton) {
            super.setOnClickListener(onClickListener);
        } else {
            this.mOnClickListener = onClickListener;
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }
    }

    //计时开始
    public void startTimer() {
        CountDownTimerButton.this.setEnabled(false);
        Observable.interval(1, TimeUnit.SECONDS)
                .take(duration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Log.e("tag", aLong.toString());
                        CountDownTimerButton.this.setText((duration - aLong - 1) + "s");
                        CountDownTimerButton.this.setEnabled(false);
                        CountDownTimerButton.this.setTextColor(getResources().getColor(R.color.default_text_color));
                        CountDownTimerButton.this.setBackgroundResource(R.drawable.other_shape_timebtn_false);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e("tag", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("tag", "onComplete");
                        CountDownTimerButton.this.setEnabled(true);
                        CountDownTimerButton.this.setText(text);
                        CountDownTimerButton.this.setTextColor(textColor);
                        CountDownTimerButton.this.setBackgroundDrawable(bg);
                    }
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
