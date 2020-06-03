package com.boniu.uuidmodel;


import android.util.Log;

import com.boniu.uuidsdk.UuidCreator;

public class NextFragment extends MyFragment {


    @Override
    public void onclick() {
        String deviceId = UuidCreator.getInstance(getActivity()).getDeviceId();
        Log.e("asd", "onclick: " + deviceId );
    }
}
