package com.ssf.framework.main.mvvm.bind.image;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageAttrBind {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView image, Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView image, int res) {
        image.setImageResource(res);
    }
}
