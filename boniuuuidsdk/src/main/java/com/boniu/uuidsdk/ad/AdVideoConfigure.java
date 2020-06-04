package com.boniu.uuidsdk.ad;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

public class AdVideoConfigure {
    public static final String TAG = "AdVideoConfigure";
    private static AdVideoConfigure adVideoConfigure = null;
    private TTAdNative mTTAdNative;
    private Activity context;
    private TTRewardVideoAd newAd;
    public AdVideoConfigure(Activity activity){
        this.context = context;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(context);

    }
    public static AdVideoConfigure getInstance(Activity context) {
        if (adVideoConfigure == null) {
            synchronized (AdVideoConfigure.class) {
                if (adVideoConfigure == null) {
                    adVideoConfigure = new AdVideoConfigure(context);
                }
            }
        }
        return adVideoConfigure;

    }
    public void showAdVideo(String id){
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(id + "")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                newAd = ttRewardVideoAd;
                //mttRewardVideoAd.setShowDownLoadBar(false);
                newAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Log.e(TAG, "onAdShow: " );
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.e(TAG, "onAdVideoBarClick: " );
                    }

                    //视频广告关闭
                    @Override
                    public void onAdClose() {
                        Log.e(TAG, "onAdClose: " );
                    }

                    //视频广告播放完成
                    @Override
                    public void onVideoComplete() {

                        Log.e(TAG, "onVideoComplete: " );

                    }

                    @Override
                    public void onVideoError() {
                        Log.e(TAG, "onVideoError: " );

                    }

                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.e(TAG, "onSkippedVideo: " );

                    }
                });
                newAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {

                        Log.e(TAG, "onIdle: " );
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.e(TAG, "onDownloadActive: " );
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.e(TAG, "onDownloadPaused: " );
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.e(TAG, "onDownloadFailed: " );
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.e(TAG, "onDownloadFinished: " );
                        newAd.showRewardVideoAd(context);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.e(TAG, "onInstalled: " );
                    }
                });
            }
            @Override
            public void onRewardVideoCached() {
                newAd.showRewardVideoAd(context);
            }
        });
    }
}
