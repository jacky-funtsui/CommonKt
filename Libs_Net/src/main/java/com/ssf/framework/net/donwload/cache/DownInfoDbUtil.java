package com.ssf.framework.net.donwload.cache;

import android.content.Context;

import com.xm.xlog.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author: ydm
 * @time: 2018/5/10
 * @说明:
 */
public class DownInfoDbUtil {
    /* tag */
    private static final String TAG = DownInfoDbUtil.class.getSimpleName();
    /* 单例 */
    private static DownInfoDbUtil db;
    //GreenDao操作
    private final DaoMaster.DevOpenHelper mOpenHelper;

    private DownInfoDbUtil(Context context) {
        mOpenHelper = new DaoMaster.DevOpenHelper(context, "gr_download_db");
    }

    /**
     * 单例
     */
    public static DownInfoDbUtil getInstance(Context context) {
        if (db == null){
            db = new DownInfoDbUtil(context);
        }
        return db;
    }

    /**
     * 增
     */
    public void insert(DownInfo info) {
        KLog.e(TAG,"新增数据库..."  + info.getUrl());
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.insert(info);
    }

    /**
     * 删
     */
    public void delete(DownInfo info) {
        KLog.e(TAG,"删除数据库..."  + info.getUrl());
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.delete(info);
    }

    /**
     * 改
     */
    public void update(DownInfo info,boolean isPostMessage) {
        DownInfo query = query(info.getUrl());
        if( query != null){
            DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            DownInfoDao downInfoDao = daoSession.getDownInfoDao();
            info.setId(query.getId());
            downInfoDao.update(info);
            // EventBus
            if (isPostMessage){
                KLog.e("发送消息 -> " + info.getUrl() + " 进度 " + info.getProgress() + " %");
                EventBus.getDefault().post(info);
            }
        }
    }

    /**
     * 改
     */
    public void update(DownInfo info) {
        update(info,true);
    }

    /**
     * 查
     */
    public DownInfo query(String url) {
//        KLog.e(TAG,"查找数据库..."  + url);
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.Url.eq(url));
        List<DownInfo> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 查全部
     */
    public List<DownInfo> queryAll() {
        KLog.e(TAG,"查询全部数据库");
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
