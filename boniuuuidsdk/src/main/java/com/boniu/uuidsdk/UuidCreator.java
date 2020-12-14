package com.boniu.uuidsdk;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class UuidCreator {
    private final String TAG = "UUID_TAG";
    private final String SP_NAME = "oaid_info";
    private final String SP_KEY_DEVICE_ID = "oaid_txt";

    private final String TEMP_DIR = "system_uuid";
    private final String TEMP_FILE_NAME = ".system";

    private String oaid;
    private String oaidKey;
    private Context context;
    private String key = "boniu--uuid--key";

    //初始化uuid返回的code  0标示成功
    public int code;

    private volatile static UuidCreator myuuid = null;
    private UuidCreator(Context context) {
        this.context = context;
        if (TextUtils.isEmpty(oaid)){
            code = MdidSdkHelper.InitSdk(context, true, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean b, IdSupplier idSupplier) {
                    String myoaid=idSupplier.getOAID();
                    if (TextUtils.isEmpty(myoaid)){
                        oaid = "error-" + UUID.randomUUID().toString();
                    }else{
                        oaid = myoaid;
                    }
                    Log.d(TAG, "OnSupport: " + oaid );
                    saveDeviceId();

                }
            });
        }
    }
    public static UuidCreator getInstance(Context context) {
        if (myuuid == null) {
            synchronized (UuidCreator.class) {
                if (myuuid == null) {
                    myuuid = new UuidCreator(context);
                }
            }
        }
        return myuuid;

    }


    private void saveDeviceId(){
        oaidKey = AESUtil.encrypt(oaid,key);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString(SP_KEY_DEVICE_ID, "");
        if (TextUtils.isEmpty(deviceId)) {
            String fileuuid = get9UUid();
            if (TextUtils.isEmpty(fileuuid)){
                createFile(oaidKey);
                deviceId = oaid;
            }else{
                deviceId = AESUtil.decrypt(fileuuid,key);
                if (TextUtils.isEmpty(deviceId)){
                    createFile(oaidKey);
                    deviceId = oaid;
                }
            }
            sharedPreferences.edit()
                    .putString(SP_KEY_DEVICE_ID, deviceId)
                    .apply();
        }else{
            oaidKey = AESUtil.encrypt(deviceId,key);
            createFile(oaidKey);
        }
    }

    /**
     * 获取设备唯一码
     * 如果返回为空，获取下 code看看是什么情况
     * oaid 未加密的uuid
     * oaidkey  加密的uuid
     * deviceId 未加密的uuid
     * @return
     */
    public String getDeviceId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString(SP_KEY_DEVICE_ID, "") + "";

        if (TextUtils.isEmpty(deviceId)){
            String uUid = get9UUid();
            if (TextUtils.isEmpty(uUid)){
                return "";
            }else{
                return uUid;
            }
        }else{
            return deviceId.toUpperCase();
        }
    }

    /**
     * android 10以下获取本地uuid
     * @return
     */
    private String get9UUid(){
        String my9uuid = "";
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File applicationFileDir = new File(externalDownloadsDir, TEMP_DIR);
        if (!applicationFileDir.exists()) {
            if (!applicationFileDir.mkdirs()) {
                Log.d(TAG, "文件夹创建失败: " + applicationFileDir.getPath());
            }
        }
        File file = new File(applicationFileDir, TEMP_FILE_NAME);
        if (file.exists()){
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                my9uuid = bufferedReader.readLine();
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return my9uuid;
        }else{
            return "";
        }

    }
    /**
     * android 10以下生成文件
     */
    private String createFile(String myoaidkey){
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File applicationFileDir = new File(externalDownloadsDir, TEMP_DIR);
        if (!applicationFileDir.exists()) {
            if (!applicationFileDir.mkdirs()) {
                Log.d(TAG, "文件夹创建失败: " + applicationFileDir.getPath());
            }
        }
        File file = new File(applicationFileDir, TEMP_FILE_NAME);
        Log.e(TAG, "creatUUIDFile1: " + file.getPath());
        if (file.exists()){
            file.delete();
        }
        FileWriter fileWriter = null;
        try {
            if (file.createNewFile()) {
                fileWriter = new FileWriter(file, false);
                fileWriter.write(myoaidkey);
            } else {
                Log.d(TAG, "文件创建失败：" + file.getPath());
            }
        } catch (Exception e) {
            Log.d(TAG, "文件创建失败：" + file.getPath() + "::" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.getPath()+"";
    }

}
