package com.shenmeiguan.myapplication;


import android.content.Context;
import android.text.TextUtils;


import java.util.UUID;

public class UUidUtils {
    public static final String UUID_STR = "UUID_STR";
    public static String getOaid(){
        String myoaid = SPUtils.getInstance(MyApp.getInstance()).getString(UUID_STR,"");
        if (TextUtils.isEmpty(myoaid)){
            myoaid = "bn_"+ UUID.randomUUID().toString();
        }
        return myoaid;

    }
}
