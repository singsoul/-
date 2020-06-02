package com.boniu.uuidmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.boniu.uuidsdk.UuidSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UuidSdk.init(this);
    }
}
