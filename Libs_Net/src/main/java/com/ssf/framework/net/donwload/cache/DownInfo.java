package com.ssf.framework.net.donwload.cache;



import com.ssf.framework.net.donwload.DownloadSubscriber;
import com.ssf.framework.net.donwload.interfac.DownState;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 14:21
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 下载请求基础类
 */
@Entity
public class DownInfo {
    @Id(autoincrement = true)
    private Long id;
    /*url*/
    private String url;
    /*存储位置*/
    private String savePath;
    /*文件保存名 */
    private String fileName;
    /*下载长度*/
    private long readLength;
    /*文件总长度*/
    private long countLength;
    /* 下载状态 */
    private int downState;
    @Transient
    DownloadSubscriber<DownInfo> downloadSubscriber;

    public DownInfo(String url,String savePath,String fileName){
        this.url = url;
        this.savePath = savePath;
        this.fileName = fileName;
    }

    @Generated(hash = 1290198047)
    public DownInfo(Long id, String url, String savePath, String fileName,
            long readLength, long countLength, int downState) {
        this.id = id;
        this.url = url;
        this.savePath = savePath;
        this.fileName = fileName;
        this.readLength = readLength;
        this.countLength = countLength;
        this.downState = downState;
    }

    @Generated(hash = 928324469)
    public DownInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getReadLength() {
        return this.readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public long getCountLength() {
        return this.countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public int getDownState() {
        return this.downState;
    }

    public void setDownState(int downState) {
        this.downState = downState;
    }

    /** 观察者 */
    public DownloadSubscriber<DownInfo> getDownloadSubscriber() {
        return downloadSubscriber;
    }
    public void setDownloadSubscriber(DownloadSubscriber<DownInfo> downloadSubscriber) {
        this.downloadSubscriber = downloadSubscriber;
    }
    /** 进度条  max:100 */
    public int getProgress(){
        return (int) (getReadLength() * 1.0 / getCountLength() * 100);
    }
    /** 下载状态 */
    public DownState getState() {
        switch (getDownState()){
            case 0:
                return DownState.NORMAL;
            case 1:
                return DownState.WAIT;
            case 2:
                return DownState.DOWN;
            case 3:
                return DownState.PAUSE;
            case 5:
                return DownState.ERROR;
            case 6:
            default:
                return DownState.FINISH;
        }
    }
    /** 对应下载状态，对应因该显示什么文本内容 */
    public String getStateText(){
        switch (getDownState()){
            case 0:
                return "下载";
            case 1:
                return "等待";
            case 2:
                return "暂停";
            case 3:
                return "继续";
            case 4:
                return "停止";
            case 5:
                return "重试";
            case 6:
            default:
                return "安装";
        }
    }
    /** 重置属性，一般用于重新下载 */
    public void reset(){
        setReadLength(0);
        setCountLength(0);
        downState = DownState.NORMAL.getState();
    }

}
