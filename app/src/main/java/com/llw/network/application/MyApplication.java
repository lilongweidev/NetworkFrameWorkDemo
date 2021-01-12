package com.llw.network.application;

import android.app.Application;

import com.llw.network.NetworkApi;

/**
 * 自定义Application
 * @author llw
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
    }
}
