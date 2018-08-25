package com.ssf.framework.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.ssf.framework.widget.R

/**
 * 创建自定义ProgressDialog
 * @param context     上下文
 */
class ProgressDialog(context: Context) : Dialog(context, R.style.loading_dialog_style) {
    //dismiss回调
    private var dismissCallback: () -> Unit = {}

    init {
        setContentView(R.layout.layout_dialog_loading)
        //旋转动画
        startAnimation()
        // 设置居中
        window!!.attributes.gravity = Gravity.CENTER
        val lp = window!!.attributes
        // 设置背景层透明度
        lp.dimAmount = 0.6f
        window!!.attributes = lp
        // 取消监听
        setOnCancelListener {
            dismissCallback()
        }
    }

    override fun show() {
        super.show()
        // 启动动画
        startAnimation()
    }

    override fun onStop() {
        super.onStop()
        //  停止动画
        findViewById<View>(R.id.net_iv_loading).clearAnimation()
    }

    /** 销毁回调 */
    fun setDismissCallback(callback: () -> Unit){
        dismissCallback = callback
    }

    /** 旋转动画  */
    private fun startAnimation() {
        val messageView = findViewById<View>(R.id.net_tv_message)
        if (messageView != null){
            val rotate = AnimationUtils.loadAnimation(context, R.anim.rotate)
            findViewById<View>(R.id.net_iv_loading).startAnimation(rotate)
        }else{
            val drawable = findViewById<ImageView>(R.id.net_iv_loading).drawable as? AnimationDrawable
            drawable?.let {
                drawable.start()
            }
        }
    }
}
