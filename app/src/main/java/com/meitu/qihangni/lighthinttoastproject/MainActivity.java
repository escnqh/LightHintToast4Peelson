package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.meitu.qihangni.lighthinttoastproject.Window.LightHintService;
import com.meitu.qihangni.lighthinttoastproject.Window.LightHintWindow;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCount == 0) {
                    Intent intent = new Intent(MainActivity.this, LightHintService.class);
                    intent.putExtra(LightHintService.ACTION, LightHintService.SHOW);
                    intent.putExtra(LightHintService.MSG,"这是一个超级长的消息1234567890哈哈哈啦啦啦啦啦啦啦123");
                    intent.putExtra(LightHintService.DURATION,3);
                    intent.putExtra(LightHintService.TEXTSIZE,20);
                    startService(intent);
                    mCount++;
                } else {
                    mCount--;
                    Intent intent = new Intent(MainActivity.this, LightHintService.class);
                    intent.putExtra(LightHintService.ACTION, LightHintService.CLOSE);
                    startService(intent);
                }

            }
        });


    }

}
