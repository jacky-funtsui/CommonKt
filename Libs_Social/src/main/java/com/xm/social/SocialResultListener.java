package com.xm.social;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 第三方登录回调
 */
public interface SocialResultListener {
    /* 开始调用的时候 */
    void onStart(SHARE_MEDIA share_media);
    /* 完成的时候回调 */
    void onComplete(SHARE_MEDIA platform, Map<String, String> data);
}
