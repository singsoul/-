package com.boniu.uuidsdk;


import android.content.Context;

import com.bun.miitmdid.core.JLibrary;

public class UuidSdk {
    public static final String TAG = "UUID_TAG";
    public static void init(Context context){
        try {
            JLibrary.InitEntry(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}