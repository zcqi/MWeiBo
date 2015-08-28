package com.example.zhangzhao.mweibo;

/**
 * Created by zhangzhao on 2015/8/11.
 */


import android.app.Application;


public  class MWeiBoApplication extends Application {

    private static MWeiBoApplication instance;

    /**
     * Create main application
     */


    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;


    }


    public static MWeiBoApplication getInstance() {
        return instance;
    }
}

