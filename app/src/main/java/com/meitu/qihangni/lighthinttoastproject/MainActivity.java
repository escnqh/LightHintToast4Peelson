package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.meitu.qihangni.lighthinttoastproject.old.LightHintService;

public class MainActivity extends AppCompatActivity {
    private Context mContext;

    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

    }

}
