package cn.reactnative.modules.update;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 2018/2/2.
 */

public class DiffUtil {
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * 生成差异包
     * @param oldPath   旧的安装包路径
     * @param newPath   新的安装包路径
     * @param patchPath 差分包路径
     * @return 生成的结果
     */
    public static native int generateDiffApk(String oldPath, String newPath, String patchPath);

    /**
     * 合并差异包
     * @param oldPath   旧的安装包路径
     * @param newPath   新的安装包路径
     * @param patchPath 差分包路径
     * @return 生成的结果
     */
    public static native int mergeDiffApk(String oldPath, String newPath, String patchPath);


    /**
     * 将RN 更新 基包 复制 到其他位置 index.android.bundle
     * @param context       上下文
     * @param outPath       输出Path
     * @return
     * @throws IOException
     */
    private static String copyOriginBundle(Context context,String outPath)  throws IOException {
        InputStream in = null;
        try {
            in = context.getAssets().open("index.android.bundle");
            return outFile(in,outPath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if (in != null){
                in.close();
            }
        }
    }


    /**
     * 将RN 更新 基包 复制 到其他位置 index.android.bundle
     * @param context       上下文
     * @param outPath       输出Path
     * @return
     * @throws IOException
     */
    private static String copyOriginBundleMeta(Context context, String outPath)  throws IOException {
        InputStream in = null;
        try {
            in = context.getAssets().open("index.android.bundle.meta");
            return outFile(in,outPath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if (in != null){
                in.close();
            }
        }
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context,String oldPath,String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File oldDirectory = new File(newPath + File.separator + oldPath);
                oldDirectory.mkdirs();
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + File.separator + fileName,newPath + File.separator + oldPath + File.separator +fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }

    /**
     * 释放 ReactNative 只释放图片
     */
    public static void copyReactNativeFilesResource(Context context,String outPath){
        copyFilesFassets(context,"drawable-hdpi",outPath);
        copyFilesFassets(context,"drawable-mdpi",outPath);
        copyFilesFassets(context,"drawable-xhdpi",outPath);
        copyFilesFassets(context,"drawable-xxhdpi",outPath);
        copyFilesFassets(context,"drawable-xxxhdpi",outPath);
    }

    /**
     * 释放 ReactNative 相关资源到其他目录
     */
    public static void copyReactNativeFiles(Context context,String outPath){
        copyReactNativeFilesResource(context,outPath);
        try {
            copyOriginBundle(context,new File(outPath,"index.android.bundle").getAbsolutePath());
            copyOriginBundleMeta(context,new File(outPath,"index.android.bundle.meta").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出文件
     * @param in      输入流
     * @param outPath 输出文件
     * @throws IOException
     */
    private static String outFile(InputStream in, String outPath) throws IOException {
        byte[] buffer = new byte[1024];
        int count;
        FileOutputStream fout = new FileOutputStream(new File(outPath));
        while ((count = in.read(buffer)) != -1) {
            fout.write(buffer, 0, count);
        }
        fout.close();
        in.close();
        return outPath;
    }

    /**
     * 合成apk
     * @param context
     * @param outPath     输出新包的路径
     * @param patchPath   差异包
     * @return
     */
    public static int bspatchApk(Context context,String outPath, String patchPath){
        //基础包
        String baseApkPath = context.getApplicationInfo().sourceDir;
        //输出包
        FileUtil.copyFile(baseApkPath,outPath);
        //合并
        return mergeDiffApk(outPath,outPath,patchPath);
    }

    /**
     * 合成apk
     * @param context
     * @param srcPath
     * @param outPath     输出新包的路径
     * @param patchPath   差异包
     * @return
     */
    public static int bspatchApk(Context context,String srcPath,String outPath, String patchPath){
        //基础包
        String baseApkPath = srcPath;
        //输出包
        FileUtil.copyFile(baseApkPath,outPath);
        //合并
        return mergeDiffApk(outPath,outPath,patchPath);
    }


    /**
     * 合成apk
     * @param context
     * @param outPath     输出新包的路径
     * @param patchPath   差异包
     * @return
     */
    public static int bspatchReactNative(Context context,String outPath, String patchPath){
        try {
            //将 RN 文件 复制到 outPath 目录
            copyOriginBundle(context, outPath);
            // 合并文件
            return mergeDiffApk(outPath,outPath,patchPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
