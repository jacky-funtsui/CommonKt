package com.xm.xnet.exception

/**
 * @author yedanmin
 * @data 2018/1/11 15:30
 * @describe
 */
class ApiException(
        val code:CodeException,
        // 状态吗
        val statusCode:Int,
        // 数据
        val displayMessage:String
):Throwable()