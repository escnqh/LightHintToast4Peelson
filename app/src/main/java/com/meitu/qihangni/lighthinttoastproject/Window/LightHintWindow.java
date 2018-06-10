package com.meitu.qihangni.lighthinttoastproject.Window;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.meitu.qihangni.lighthinttoastproject.AutoScrollTextView;
import com.meitu.qihangni.lighthinttoastproject.R;

/**
 * @author nqh 2018/6/8
 */
public class LightHintWindow extends FrameLayout {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private View mView;
    private AutoScrollTextView mAutoScrollTextView;
    private WindowManager.LayoutParams mParams;
    private LightHintWindowManager mLightHintWindowManager;
    private int mDuration;
    private boolean isScroll = false;
    private String mMsg;

    public LightHintWindow(@NonNull Context context, String msg, int duration, int textSize) {
        super(context);
        if (context instanceof Activity) {
            mContext = context.getApplicationContext();
        } else if (context instanceof Application) {
            mContext = context;
        }
        mMsg = msg;
        mLightHintWindowManager = LightHintWindowManager.getInstance(context);
        if (mLightHintWindowManager != null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = inflater.inflate(R.layout.lighthint, null);
            mAutoScrollTextView = mView.findViewById(R.id.toast_msg);
            mAutoScrollTextView.setTextSize(textSize);
            mAutoScrollTextView.setText(mMsg);
            int windowWidth = mLightHintWindowManager.getScreenWidth();
            int textWidth = sp2px(mContext, textSize) * msg.length();
            isScroll = windowWidth <= textWidth;
            Log.i(TAG, "init nice...");
        }
    }


    public void show() {

        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP;
        mParams.x = 0;
        mParams.y = mLightHintWindowManager.getStatusBarHeight();
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        if (isScroll) {
            mLightHintWindowManager.addView(mView, mParams);
            mAutoScrollTextView.setScrollSpeed(3);
            mAutoScrollTextView.setScrollTime(2);
            mAutoScrollTextView.setOnScrollStopListener(new AutoScrollTextView.OnScrollStopListener() {
                @Override
                public void onScrollStop(@Nullable String param) {
                    hide();
                }
            });
            mAutoScrollTextView.startScroll();
        } else {
            mLightHintWindowManager.addView(mView, mParams);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            }, mDuration * 10000);
        }
    }

    public void hide() {
        mLightHintWindowManager.removeView(mView);
    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
