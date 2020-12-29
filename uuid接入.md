# 统一联盟oaid接入


1.接入 oaid_sdk_1.0.23.aar

```java
    //oaid
  implementation(name: 'oaid_sdk_1.0.23', ext: 'aar')
```



2.在assets中 加入 supplierconfig.json





3.初始化oaid

```java
DemoHelper.getDeviceIds(this, new DemoHelper.OaidInterfaces() {
            @Override
            public void OnIdsAvalid(String oaid) {

            }
        });
```



4.获取oaid

```java
//获取uuid
        String oaid = UUidUtils.getOaid();
```



5.混淆配置

```java
#oaid相关
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
```

