package me.peng.pengexpress0726.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/7/27.
 */

public class ExpressApplication extends Application{

    private static ExpressApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ExpressApplication getInstance(){
        return sInstance;
    }
}
