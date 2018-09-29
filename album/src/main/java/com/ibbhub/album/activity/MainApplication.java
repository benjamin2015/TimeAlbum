package com.ibbhub.album.activity;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.ibbhub.album.PicassoUtils;


/**
 * Created by gackor on 2015/7/29.
 */
public class MainApplication extends Application {
    private static Context mContext;
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        mContext = this.getApplicationContext();
        PicassoUtils.initPicasso(this);
        super.onCreate();
    }

}
