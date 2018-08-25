package com.ssf.framework.net.ex


/**
 * @author admin
 * @data 2018/4/25
 * @describe
 */
interface IConfig{
    companion object {
        val tag = "XNet"

        /**
         * 多域名，如果单域名不切换
         */
        var DOMAIN_URL = ""
        /**
         * 网络请求加载框 TAG
         */
        val NET_PROGRESS_TAG = "NET_PROGRESS_TAG"
    }
}