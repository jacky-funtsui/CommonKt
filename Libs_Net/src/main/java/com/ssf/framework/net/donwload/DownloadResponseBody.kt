package com.ssf.framework.net.donwload

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author: ydm
 * @time: 2018/5/9
 * @说明: 下载相应 body
 */
class DownloadResponseBody(
        // 下载文件网站
        private val donwloadUrl:String,
        // 响应body
        private val responseBody: ResponseBody,
        // 回调
        private val update: (String, Long, Long, Boolean) -> Unit
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long  = responseBody.contentLength()

    override fun contentType(): MediaType?  = responseBody.contentType()

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    /**
     * 进行处理，并回调
     */
    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            /* 当前已经读取的字节数量 */
            private var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                // 回调
                update(donwloadUrl, totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }
}