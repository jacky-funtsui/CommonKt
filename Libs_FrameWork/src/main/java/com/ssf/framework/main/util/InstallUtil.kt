package com.ssf.framework.main.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.ssf.framework.net.donwload.cache.DownInfo
import com.ssf.framework.net.permissions.RxPermissions
import com.ssf.framework.widget.ex.toast
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.io.File

/**
 * @author: ydm
 * @time: 2018/5/11
 * @说明:
 */
class InstallUtil {
    companion object {

        /**
         * 使用默认浏览器
         *
         * @param activity 源Activity
         * @param url      跳转地址
         */
        fun startDefaultWeb(activity: Activity, url: String) {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            val contentUrl = Uri.parse(url)
            intent.data = contentUrl
            activity.startActivity(intent)
        }

        /**
         * 安装apk
         */
        fun installApk(activity: RxAppCompatActivity, apkFile: File) {
            val intent = Intent(Intent.ACTION_VIEW)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            } else {
                // 声明需要的临时权限
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                // 第二个参数，即第一步中配置的authorities
                val packageName = activity.application.packageName
                val contentUri = FileProvider.getUriForFile(activity, "$packageName.fileProvider", apkFile)
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            }
            activity.startActivity(intent)
        }

        /**
         * 安装apk
         */
        fun installApk(activity: RxAppCompatActivity, downInfo: DownInfo) {
            val apkFile = File(downInfo.savePath)
            if (apkFile.exists()){
                val intent = Intent(Intent.ACTION_VIEW)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                } else {
                    // 声明需要的临时权限
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    // 第二个参数，即第一步中配置的authorities
                    val packageName = activity.application.packageName
                    val contentUri = FileProvider.getUriForFile(activity, "$packageName.fileProvider", apkFile)
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                }
                activity.startActivity(intent)
            }else{
                activity.toast("文件不存在,将使用默认浏览器升级")
                startDefaultWeb(activity,downInfo.url)
            }
        }
    }
}