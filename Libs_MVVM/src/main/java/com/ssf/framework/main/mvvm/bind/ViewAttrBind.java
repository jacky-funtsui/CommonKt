package com.ssf.framework.main.mvvm.bind;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ssf.framework.autolayout.utils.AutoUtils;

public class ViewAttrBind {

    @BindingAdapter("android:layout_marginLeft")
    public static void marginLeft(View view, int margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).leftMargin = AutoUtils.getPercentHeightSize(margin);
            view.setLayoutParams(lp);
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void marginTop(View view, int margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).topMargin = AutoUtils.getPercentHeightSize(margin);
            view.setLayoutParams(lp);
        }
    }

    @BindingAdapter("android:layout_marginRight")
    public static void marginRight(View view, int margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).rightMargin = AutoUtils.getPercentHeightSize(margin);
            view.setLayoutParams(lp);
        }
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void marginBottom(View view, int margin) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin = AutoUtils.getPercentHeightSize(margin);
            view.setLayoutParams(lp);
        }
    }

    @BindingAdapter("android:padding")
    public static void padding(View view, int padding) {
        int paddingSize = AutoUtils.getPercentWidthSize(padding);
        view.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
    }

    @BindingAdapter("android:paddingLeft")
    public static void paddingLeft(View view, int padding) {
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        view.setPadding(AutoUtils.getPercentWidthSize(padding), t, r, b);
    }

    @BindingAdapter("android:paddingTop")
    public static void paddingTop(View view, int padding) {
        int l = view.getPaddingLeft();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        view.setPadding(l, AutoUtils.getPercentHeightSize(padding), r, b);
    }

    @BindingAdapter("android:paddingRight")
    public static void paddingRight(View view, int padding) {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int b = view.getPaddingBottom();
        view.setPadding(l, t, AutoUtils.getPercentWidthSize(padding), b);
    }

    @BindingAdapter("android:paddingBottom")
    public static void paddingBottom(View view, int padding) {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        view.setPadding(l, t, r, AutoUtils.getPercentHeightSize(padding));
    }
}
