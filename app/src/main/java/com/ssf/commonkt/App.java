package com.ssf.commonkt;

import android.app.Activity;
import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ssf.commonkt.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @atuthor ydm
 * @data on 2018/8/25 0025
 * @describe
 */
public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
        // ARouter
        ARouter.init(this);

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}

