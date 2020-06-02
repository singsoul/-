package com.boniu.uuidsdk;

import android.content.Context;
import android.text.TextUtils;

import com.bun.miitmdid.core.ErrorCode;

public class UuidUtils {
    public static boolean checkCode(UuidCreator uuidCreator, Context context, UUidDialog.DialogClick dialogClick){
        UUidDialog dialog = null;
        boolean isSuccess = false;

        if(uuidCreator.code == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT){//不支持的设备
            dialog = new UUidDialog(context,"不支持的设备,获取匿名设备标识符为空",dialogClick);
        }else if(uuidCreator.code == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT){//不支持的设备厂商
            dialog = new UUidDialog(context,"不支持的设备厂商,获取匿名设备标识符为空",dialogClick);
        }else if(uuidCreator.code == ErrorCode.INIT_ERROR_RESULT_DELAY || uuidCreator.code == 0){//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
            String deviceId = uuidCreator.getDeviceId();
            if (TextUtils.isEmpty(deviceId)){
                dialog = new UUidDialog(context,"匿名设备标识符为空，请重试或开启设备标示",dialogClick);
            }else{
                isSuccess = true;
            }
        }else if(uuidCreator.code == ErrorCode.INIT_HELPER_CALL_ERROR || uuidCreator.code == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE){//反射调用出错
            dialog = new UUidDialog(context,"配置异常，无法获取匿名设备标识符",dialogClick);
        }else{
            dialog = new UUidDialog(context,"配置异常，无法获取匿名设备标识符",dialogClick);
        }
        if (dialog != null){
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        return isSuccess;

    }
}
