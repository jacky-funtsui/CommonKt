package com.ssf.framework.glide.ex

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.ssf.framework.glide.interfac.IGlideType
import com.xm.imageloader.GlideLoader
import java.io.File

/**
 * @author robert
 * @date 2018/1/17
 */
fun ImageView.load(
        context: Context,
        path: String?,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, path)
}


fun ImageView.load(
        fragment: Fragment,
        path: String?,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, path)
}

fun ImageView.load(
        context: Context,
        path: String?,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, path, requestOptions)
}


fun ImageView.load(
        fragment: Fragment,
        path: String?,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, path, requestOptions)
}


fun ImageView.load(
        context: Context,
        @DrawableRes drawableRes: Int,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, drawableRes)
}

fun ImageView.load(
        fragment: Fragment,
        @DrawableRes drawableRes: Int,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, drawableRes)
}

fun ImageView.load(
        context: Context,
        @DrawableRes drawableRes: Int,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, drawableRes, requestOptions)
}

fun ImageView.load(
        fragment: Fragment,
        @DrawableRes drawableRes: Int,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, drawableRes, requestOptions)
}


fun ImageView.load(
        context: Context,
        file: File?,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, file)
}

fun ImageView.load(
        fragment: Fragment,
        file: File?,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, file)
}

fun ImageView.load(
        context: Context,
        file: File?,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(context, type, this, file, requestOptions)
}

fun ImageView.load(
        fragment: Fragment,
        file: File,
        requestOptions: RequestOptions,
        type: IGlideType = IGlideType.NORMAL
) {
    GlideLoader.loadImage(fragment, type, this, file, requestOptions)
}
