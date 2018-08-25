package com.ssf.framework.net.donwload

import android.content.Context
import android.os.Environment
import com.ssf.framework.net.donwload.cache.DownInfo
import com.ssf.framework.net.donwload.cache.DownInfoDbUtil
import com.ssf.framework.net.donwload.interfac.DownState
import com.xm.xlog.KLog
import okhttp3.ResponseBody
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*

/**
 * @author: ydm
 * @time: 2018/5/10
 * @说明: 下载文件工具类
 */
class DownloadFileUtil {
    companion object {

        /**
         * 下载文件保存路径
         */
        fun getDownloadDirectory(): File {
            return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            } else {
                Environment.getDownloadCacheDirectory()
            }
        }

        /**
         * 如果同一url新地址与旧文件命名不同时删除
         */
        fun queryDownInfo(context: Context, downloadUrl: String, fileName: String? = null): DownInfo? {
            val query = DownInfoDbUtil.getInstance(context).query(downloadUrl)
            if (query != null) {
                if (fileName != null) {
                    if (query.fileName != fileName) {
                        // 删除
                        DownInfoDbUtil.getInstance(context).delete(query)
                        // 文件名不同
                        return null
                    }
                }
                if (query.state == DownState.FINISH && !File(query.savePath).exists()){
                    // 删除
                    DownInfoDbUtil.getInstance(context).delete(query)
                    // 文件不存在
                    return null
                }
                return query
            }
            return null
        }

        /**
         * 创建默认的 下载对象
         */
        fun createDownInfo(context: Context,downloadUrl: String, fileName: String? = null): DownInfo {
            val file = if (fileName == null) {
                var name = downloadUrl.substring(downloadUrl.lastIndexOf("/"))
                if (!name.contains(".")) {
                    name = Random().nextInt(Integer.MAX_VALUE).toString()
                }
                name
            } else {
                fileName
            }
            val savePath = File(getDownloadDirectory(), file).absolutePath
            val downInfo = DownInfo(downloadUrl, savePath, file)
            // 保存
            DownInfoDbUtil.getInstance(context).insert(downInfo)
            // 返回
            return downInfo
        }

        /**
         * 写入文件
         */
        fun writeCache(responseBody: ResponseBody,info: DownInfo){
            val file = File(info.savePath)
            if (!file.parentFile.exists()){
                val mkdirs = file.mkdirs()
                KLog.e("创建目录 -> $mkdirs")
            }
            var allLength = info.countLength
            if (allLength == 0L) {
                allLength = responseBody.contentLength()
            }
            RandomAccessFile(file,"rwd").use {
                it.channel.use {
                    val mappedBuffer = it.map(FileChannel.MapMode.READ_WRITE, info.readLength, allLength - info.readLength)
                    responseBody.byteStream().use {
                        val buffer = ByteArray(1024 * 8)
                        while ( true) {
                            val len = it.read(buffer)
                            if (len == -1){
                                break
                            }
                            mappedBuffer.put(buffer, 0, len)
                        }
                    }
                }
            }
        }

    }
}