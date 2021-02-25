package com.shenmeiguan.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApp extends Application {

    private static MyApp context;

    public synchronized static MyApp getInstance() {
        if (null == context) {
            context = new MyApp();
        }
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        DemoHelper.getDeviceIds(this, new DemoHelper.OaidInterfaces() {
            @Override
            public void OnIdsAvalid(String oaid) {
                Log.e("asd", "OnIdsAvalid: " + oaid );
            }
        });
    }
}
