package com.hiking.climbtogether.tool;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class ClimbTogetherApplication  extends MultiDexApplication {

    private static ClimbTogetherApplication instance = null;

    public static ClimbTogetherApplication getInstance(){
        if (instance == null){
            instance = new ClimbTogetherApplication();
            return instance;
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ImageLoaderProvider.getInstance().initImageLoader();
    }

    public Context getContext(){
        return this;
    }
}
