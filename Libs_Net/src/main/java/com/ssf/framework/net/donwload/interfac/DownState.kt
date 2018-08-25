package com.ssf.framework.net.donwload.interfac

/**
 * 下载状态
 * by：小民修订，原版：WZG
 */

enum class DownState(var state: Int) {
    NORMAL(0), //正常
    WAIT(1), //等待
    DOWN(2),
    PAUSE(3),
    ERROR(5),
    FINISH(6);

    /** 对应下载状态，反向描述  */
    val stateEnum: DownState
        get() {
            when (state) {
                0 -> return NORMAL
                1 -> return WAIT
                2 -> return DOWN
                3 -> return PAUSE
                5 -> return ERROR
                6 -> return FINISH
                else -> return FINISH
            }
        }
    /** 对应下载状态，真实表述  */
    val text: String
        get() {
            return when (state) {
                0 -> "正常"
                1 -> "等待"
                2 -> "下载中"
                3 -> "暂停"
                5 -> "错误"
                6 -> "下载完成"
                else -> "下载完成"
            }
        }
    /** 对应下载状态，反向描述  */
    val stateText: String
        get() {
            return when (state) {
                0 -> "下载"
                1 -> "等待"
                2 -> "暂停"
                3 -> "继续"
                5 -> "重试"
                6 -> "安装"
                else -> "安装"
            }
        }
}
