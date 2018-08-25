package com.xm.social;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by yedanmin on 2017/12/19.
 */

public class SocialSharedUtils {
    private String TAG = SocialSharedUtils.class.getSimpleName();
    public Activity mActivity;

    public SocialSharedUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * 系统分享功能
     * @param media
     * @param options
     */
    public void sysShare(String media,ShareOptionsBean options){
        switch (media) {
            case "微博":
                if(!SocialUtils.isSinaClientAvailable(mActivity)){
                    toast("请先安装微博客户端");
                    return;
                }
                sysShareSina(options);
                break;
            case "QQ":
                if(!SocialUtils.isQQClientAvailable(mActivity)){
                    toast("请先安装QQ客户端");
                    return;
                }
                sysShareQQ(options);
                break;
            case "QQ空间":
                if(!SocialUtils.isQzoneClientAvailable(mActivity)){
                    toast("请先安装QQ空间客户端");
                    return;
                }
                sysShareQzone(options);
                break;
            case "微信":
                if(!SocialUtils.isWeChatAvailable(mActivity)){
                    toast("请先安装微信客户端");
                    return;
                }
                sysShareWeixin(options);
                break;
            case "朋友圈":
                if(!SocialUtils.isWeChatAvailable(mActivity)){
                    toast("请先安装微信客户端");
                    return;
                }
                sysShareWeixinCircle(options);
                break;
            case "更多分享":
                sysShare(options);
                break;
            case "复制链接":
                String url;
                if(TextUtils.isEmpty(options.shareShortUrl)){
                    url = options.shareUrl;
                }else{
                    url = options.shareShortUrl;
                }
                String text = options.title + "   " + url;
                ClipboardManager myClipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                toast("成功复制链接,请黏贴给好友");
                break;
            default:
                break;
        }
    }


    /** 系统分享 */
    private void sysShare(ShareOptionsBean options) {
        if(!TextUtils.isEmpty(options.shareShortUrl) && !TextUtils.isEmpty(options.shareUrl)){
            String url;
            if(TextUtils.isEmpty(options.shareShortUrl)){
                url = options.shareUrl;
            }else{
                url = options.shareShortUrl;
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, options.title + "   " + url);
            //设置分享列表的标题，并且每次都显示分享列表
            mActivity.startActivity(Intent.createChooser(intent, "分享到"));
        }else{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), options.image.asBitmap(), null,null));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra("Kdescription", options.content);
            //设置分享列表的标题，并且每次都显示分享列表
            mActivity.startActivity(Intent.createChooser(intent, "分享到"));
        }

    }

    /**
     * 分享到指定app，
     * 支持文本 或 单纯一张图片
     * @param options
     */
    private void sysShareSocial(ShareOptionsBean options,String pkg,String cls) {
        //地址
        String url;
        if(TextUtils.isEmpty(options.shareShortUrl)){
            url = options.shareUrl;
        }else{
            url = options.shareShortUrl;
        }
        //文本
        String shareText;
        if(TextUtils.isEmpty(url)){
            shareText = options.content;
        }else{
            shareText = options.title + "\n" + options.content + "\n" + url;
        }
        //判断文本 还是图片
        if(!TextUtils.isEmpty(options.shareShortUrl) && !TextUtils.isEmpty(options.shareUrl)){
            Intent intent = new Intent();
            if (cls != null){
                ComponentName comp = new ComponentName(pkg, cls);
                intent.setComponent(comp);
            }else{
                intent.setPackage(pkg);
            }
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_TEXT,  options.title + "\n" + url);
            mActivity.startActivity(intent);
        }else{
            Intent intent = new Intent();
            if (cls != null){
                ComponentName comp = new ComponentName(pkg, cls);
                intent.setComponent(comp);
            }else{
                intent.setPackage(pkg);
            }
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra("Kdescription", shareText);
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), options.image.asBitmap(), null,null));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 分享到微信，
     * 支持文本 或 单纯一张图片
     * @param options
     */
    private void sysShareSina(ShareOptionsBean options) {
        sysShareSocial(options,"com.sina.weibo", null);
    }

    /**
     * 分享到微信，
     * 支持文本 或 单纯一张图片
     * @param options
     */
    private void sysShareQQ(ShareOptionsBean options) {
        sysShareSocial(options,"com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
    }

    /**
     * 分享到微信，
     * 支持文本 或 单纯一张图片
     * @param options
     */
    private void sysShareQzone(ShareOptionsBean options) {
        sysShareSocial(options,"com.qzone", null);
    }

    /**
     * 分享到微信，
     * 支持文本 或 单纯一张图片
     * @param options
     */
    private void sysShareWeixin(ShareOptionsBean options) {
        sysShareSocial(options,"com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
    }

    /**
     * 分享到微信圈  只支持 图片 + 标题
     * @param options
     */
    private void sysShareWeixinCircle(ShareOptionsBean options) {
        String url;
        if(TextUtils.isEmpty(options.shareShortUrl)){
            url = options.shareUrl;
        }else{
            url = options.shareShortUrl;
        }

        String shareText;
        if(TextUtils.isEmpty(url)){
            shareText = options.content;
        }else{
            shareText = options.title + "\n" + options.content + "\n" + url;
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra("Kdescription", shareText);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), options.image.asBitmap(), null,null));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        mActivity.startActivity(intent);
    }

    /**
     * 复制链接
     * @param text  内容
     */
    public void copy(String text){
        ClipboardManager myClipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        toast("复制链接成功,快去分享吧!");
    }

    /** 控件Bitmap对象 */
    public Bitmap getViewBitmap(int viewId){
        View view = mActivity.findViewById(viewId);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /** Toast */
    public void toast(String message){
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }
}
