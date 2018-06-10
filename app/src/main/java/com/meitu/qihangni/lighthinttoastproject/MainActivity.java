package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.meitu.qihangni.lighthinttoastproject.PopWindow.LightHintPopWindow;
import com.meitu.qihangni.lighthinttoastproject.Toast.LightHintToast;
import com.meitu.qihangni.lighthinttoastproject.Window.LightHintWindow;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private int mCount = 0;
    private LightHintWindow lightHintWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        lightHintWindow = new LightHintWindow(mContext, "这是一个超级长的消息1234567890哈哈哈啦啦啦啦啦啦啦123", 3, 20);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCount == 0) {
                    lightHintWindow.show();
                    mCount++;
                } else {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
