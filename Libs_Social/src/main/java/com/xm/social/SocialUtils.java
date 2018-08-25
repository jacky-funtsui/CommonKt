package com.xm.social;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/12.
 */

public class SocialUtils {
    private static SocialUtils mSocialUtils;

    private SocialUtils() {
    }

    public static SocialUtils getInstance(Activity activity) {
        if (mSocialUtils == null) {
            mSocialUtils = new SocialUtils();
        }
        mSocialUtils.mActivity = activity;
        return mSocialUtils;
    }

    /**
     * 发起请求的Activity 记得要加 onActivityResult
     */
    public Activity mActivity;
    /**
     * 登录成功回调
     */
    private SocialResultListener mResultListener;

    /**
     * 第三方登录功能
     *
     * @param platform 平台
     */
    public void onSocialLogin(SHARE_MEDIA platform, SocialResultListener resultListener) {
        mResultListener = resultListener;
//        每次先执行删除授权api，然后再调用授权api
        UMShareAPI.get(mActivity).deleteOauth(mActivity, platform, null);
        UMShareAPI.get(mActivity).doOauthVerify(mActivity, platform, umAuthListener);
    }

    /**
     * 第三方登录信息功能
     */
    private void onSocialLoginInfo(SHARE_MEDIA platform) {
        UMShareAPI.get(mActivity).getPlatformInfo(mActivity, platform, umAuthListener);
    }

    /**
     * 登录回调事件
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        private boolean isLoginInfo;

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            mResultListener.onStart(share_media);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (SHARE_MEDIA.WEIXIN == platform || SHARE_MEDIA.QQ == platform) {
//                if (action == 0) {
//                    //登录成功,进入用户信息
//                    onSocialLoginInfo(platform);
//                } else if (action == 2) {
//                    //登录信息，对接接口
//                    mResultListener.onComplete(platform, data);
//                }
                mResultListener.onComplete(platform, data);
            } else {
                isLoginInfo = !isLoginInfo;
                if (isLoginInfo) {
                    //登录成功,进入用户信息
                    onSocialLoginInfo(platform);
                } else {
                    //登录信息，对接接口
                    mResultListener.onComplete(platform, data);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mResultListener.onComplete(platform, null);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mResultListener.onComplete(platform, null);
        }
    };

    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeChatAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 判断sina是否可用
     *
     * @param context
     * @return
     */
    public static boolean isSinaClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq空间是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQzoneClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.qzone")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断支付宝是否可用
     *
     * @param context
     * @return
     */
    public boolean isAliPayAvailable(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


    /**
     * 打开第三方  地址：http://www.jianshu.com/p/1e92c2b0d773
     *
     * @param context
     * @param pkg     包名
     * @param cls     activity名
     * @return
     */
    public static void openSocial(Context context, String pkg, String cls) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(pkg, cls);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * 跳转到应用
     *
     * @param applicationId 应用id
     */
    public void startMarket(String applicationId) {
        try {
            Uri uri = Uri.parse("market://details?id=" + applicationId);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mActivity, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 使用intent 打开 QQ 支付
     *
     * @param url 跳转的 intent参数
     */
    public static void startPay(Activity activity, String url) {
        try {
            //启动支付宝等 应用
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            activity.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

}
