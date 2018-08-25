package com.ssf.framework.net.donwload.interfac

/**
 * @author: ydm
 * @time: 2018/5/10
 * @说明: 下载完成事件回调
 */
interface IDownloadFinished{
    fun finished(downloadUrl: String)
}