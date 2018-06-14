package com.meitu.qihangni.lighthinttoastproject.old;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author nqh 2018/6/11
 */
public class LightHintService extends Service {
    private LightHintWindow mLightHintWindow;
    public static final String ACTION = "lighthint";
    public static final String SHOW = "show";
    public static final String CLOSE = "close";
    public static final String MSG = "MSG";
    public static final String DURATION = "DURATION";
    public static final String TEXTSIZE = "TEXTSIZE";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                if (intent.getStringExtra("MSG") != null) {
                    mLightHintWindow = new LightHintWindow(this, intent.getStringExtra("MSG"), intent.getIntExtra("DURATION", 3), intent.getIntExtra("TEXTSIZE", 20));
                    mLightHintWindow.show();
                }
            } else if (CLOSE.equals(action)) {
                if (mLightHintWindow != null) {
                    mLightHintWindow.close();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
