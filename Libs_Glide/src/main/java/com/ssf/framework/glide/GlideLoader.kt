package com.xm.imageloader

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ssf.framework.glide.R
import com.ssf.framework.glide.interfac.IGlideType

import java.io.File

object GlideLoader {

    //TODO 需设置成对应的图片地址
    var imgHost: String? = null

    //正常请求过程使用
    private val mRequestNormalOptions: RequestOptions = RequestOptions
            .placeholderOf(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    // 圆形头像时使用
    private val mRequestCircleOptions: RequestOptions = RequestOptions
            .placeholderOf(R.drawable.ic_launcher_background)
            .circleCrop()
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)


    fun loadImage(context: Context, type: IGlideType, view: ImageView, path: String?) {
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        requestManager
                .setDefaultRequestOptions(requestOptions)
                .load(setUrlHost(path))
                .into(view)
    }

    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, path: String?) {
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(setUrlHost(path))
                .into(view)
    }

    fun loadImage(context: Context, type: IGlideType, view: ImageView, drawable: Int) {
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        requestManager
                .setDefaultRequestOptions(requestOptions)
                .load(drawable)
                .into(view)
    }

    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, drawable: Int) {
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(drawable)
                .into(view)
    }

    fun loadImage(context: Context, type: IGlideType, view: ImageView, file: File?) {
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        requestManager
                .setDefaultRequestOptions(requestOptions)
                .load(file)
                .into(view)
    }

    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, file: File?) {
        val requestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(file)
                .into(view)
    }

    /**
     * 后面传进来的会更新掉默认设置
     */
    fun loadImage(context: Context, type: IGlideType, view: ImageView, path: String?, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        requestManager
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(path)
                .into(view)
    }


    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, path: String?, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(path)
                .into(view)
    }

    fun loadImage(context: Context, type: IGlideType, view: ImageView, drawable: Int, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        requestManager
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(drawable)
                .into(view)
    }

    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, drawable: Int, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(drawable)
                .into(view)
    }

    fun loadImage(context: Context, type: IGlideType, view: ImageView, file: File?, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        val requestManager =
                if (context is Activity) {
                    Glide.with(context)
                } else {
                    Glide.with(context)
                }
        requestManager
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(file)
                .into(view)
    }

    fun loadImage(context: Fragment, type: IGlideType, view: ImageView, file: File?, requestOptions: RequestOptions) {
        val defaultRequestOptions = if (type == IGlideType.CIRCLE) mRequestCircleOptions else mRequestNormalOptions
        Glide.with(context)
                .setDefaultRequestOptions(defaultRequestOptions)
                .applyDefaultRequestOptions(requestOptions)
                .load(file)
                .into(view)
    }

    /**
     * 设置host
     */
    private fun setUrlHost(path: String?): String {
        if (path == null) {
            return ""
        }
        var url = path
        if (!url.startsWith("http") && !url.contains("data:image")) {
            url = imgHost + path
        }
        return url
    }

    fun clearMemoryCache(context: Context) {

    }

    fun clearDiskCache(context: Context) {

    }


}