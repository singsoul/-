package com.boniu.uuidmodel;

import android.app.Application;

import com.boniu.uuidsdk.UuidSdk;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UuidSdk.init(this);
    }
}
