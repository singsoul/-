package com.shenmeiguan.myapplication;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class DemoHelper {
    private static final String TAG = "UUID_TAG";

    private static final String TEMP_DIR = "system_uuid";
    private static final String TEMP_FILE_NAME = ".config";


    private static String key = "boniu--uuid--key";
    private static Handler handler = new Handler();
    private static boolean isNext = true;
    public static void getDeviceIds(Context context, final OaidInterfaces oaidInterfaces){
        int nres = CallFromReflect(context,oaidInterfaces);

        if(nres == ErrorCode.INIT_ERROR_RESULT_DELAY){//延迟获取
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isNext){
                        saveCode(oaidInterfaces,"");
                    }
                }
            },1000);
        }else if (nres == 0){

        }else{//获取失败
            saveCode(oaidInterfaces,"");
        }
    }

    /*
     * 方法调用
     *
     *
     * */
    private static int CallFromReflect(Context cxt, final OaidInterfaces oaidInterfaces){
        return MdidSdkHelper.InitSdk(cxt, true, new IIdentifierListener() {
            @Override
            public void OnSupport(boolean b, IdSupplier idSupplier) {
                isNext = false;
                if (idSupplier != null){
                    String oaid = idSupplier.getOAID();
                    saveCode(oaidInterfaces,oaid);
                }else{
                    saveCode(oaidInterfaces,"");
                }
            }
        });
    }

    private static void saveCode(OaidInterfaces oaidInterfaces, String uuid){
        String deviceId;
        if (TextUtils.isEmpty(uuid) || "00000000000000000000000000000000".equals(uuid) || "00000000-0000-0000-0000-000000000000".equals(uuid)){
            uuid = UUID.randomUUID().toString();
            deviceId = SPUtils.getInstance(MyApp.getInstance()).getString(UUidUtils.UUID_STR);
//            if ("00000000000000000000000000000000".equals(deviceId) || "00000000-0000-0000-0000-000000000000".equals(deviceId)){
//                deviceId = uuid;
//            }
        }
        else{
            deviceId = uuid;
        }

        String oaidKey = AESUtil.encrypt(uuid,key);
        if (TextUtils.isEmpty(deviceId) || "00000000000000000000000000000000".equals(deviceId) || "00000000-0000-0000-0000-000000000000".equals(deviceId)) {
            String fileuuid = get9UUid();
            if (TextUtils.isEmpty(fileuuid)){
                createFile(oaidKey);
                deviceId = uuid;
            }else{
                deviceId = AESUtil.decrypt(fileuuid,key);
                if (TextUtils.isEmpty(deviceId) || "00000000000000000000000000000000".equals(deviceId) || "00000000-0000-0000-0000-000000000000".equals(deviceId)){
                    createFile(oaidKey);
                    deviceId = uuid;
                }
            }
        }else{
            oaidKey = AESUtil.encrypt(deviceId,key);
            createFile(oaidKey);
        }

        SPUtils.getInstance(MyApp.getInstance()).put(UUidUtils.UUID_STR,deviceId);
        oaidInterfaces.OnIdsAvalid(deviceId);

    }
    public interface OaidInterfaces{
        void OnIdsAvalid(String oaid);

    }

    /**
     * android 10以下获取本地uuid
     * @return
     */
    private static String get9UUid(){
        String my9uuid = "";
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File applicationFileDir = new File(externalDownloadsDir, TEMP_DIR);
        if (!applicationFileDir.exists()) {
            if (!applicationFileDir.mkdirs()) {
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
                Log.e(TAG, "get9UUid: " + e.getLocalizedMessage() );
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
    private static String createFile(String myoaidkey){
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File applicationFileDir = new File(externalDownloadsDir, TEMP_DIR);
        if (!applicationFileDir.exists()) {
            if (!applicationFileDir.mkdirs()) {
            }
        }
        File file = new File(applicationFileDir, TEMP_FILE_NAME);
        if (file.exists()){
            file.delete();
        }
        FileWriter fileWriter = null;
        try {
            if (file.createNewFile()) {
                fileWriter = new FileWriter(file, false);
                fileWriter.write(myoaidkey);
            } else {
            }
        } catch (Exception e) {
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
