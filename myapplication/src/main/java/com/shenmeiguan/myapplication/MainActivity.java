package com.shenmeiguan.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "asd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nums();

    }

    private void nums(){
        int a = 401;
        int b = 271;
        int c = 0;
        for (int i = 0; i < 50; i++) {
            a = (int)((a + 75) * 0.85);
            b = (int)((b + 75) * 0.85);
            c = (int)((c + 75) * 0.85);
            Log.e(TAG, "nums: " + i + ":" + a + ":" + b + ":" + c );
        }
    }
}
