package com.xm.updates.tinker.util;

/**
 * Created by admin on 2018/3/15.
 */

public interface ILocalBroadcast {
    /**
     * 安装 tinker 成功 or 失败 广播
     */
    String TINKER_RESULT_ACTION = "TINKER_RESULT_ACTION";
    /**
     * 安装状态
     */
    String TINKER_RESULT_SUCCESS = "TINKER_RESULT_SUCCESS";
}
