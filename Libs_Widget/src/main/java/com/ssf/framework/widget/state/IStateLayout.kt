package com.ssf.framework.widget.state

/**
 * @author 小民
 * @time 2017/7/16
 * @说明 布局状态，布局，加载中布局
 */

enum class IStateLayout {
    NORMAL,      //隐藏不显示
    EMPTY,       //空布局
    LOADING,     //加载中布局
    REFRESH      //网络异常，重新刷新布局
}
