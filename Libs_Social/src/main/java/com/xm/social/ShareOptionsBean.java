package com.xm.social;

import com.umeng.socialize.media.UMImage;

/**
 * Created by yedanmin on 2017/12/19.
 */
public class ShareOptionsBean {
    public String title;
    public String content;
    public String shareUrl;
    public String shareShortUrl;
    public UMImage image;


    public ShareOptionsBean(String title, String content, UMImage image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public ShareOptionsBean(String title, String content, String shareUrl, String shareShortUrl, UMImage image) {
        this.title = title;
        this.content = content;
        this.shareUrl = shareUrl;
        this.shareShortUrl = shareShortUrl;
        this.image = image;
    }
}
