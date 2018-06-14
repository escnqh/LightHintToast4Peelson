package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Window类型的轻提示
 * @author nqh 2018/6/13
 */
class WindowLightHint implements WhatLightHint {

    private final String TAG = this.getClass().getName();
    private Context mContext;
    private String mText;
    private int mDuration = 3;//默认3s
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private AutoScrollTextView mAutoScrollTextView;

    public WindowLightHint(Context context, String text, int duration) {
        this.mContext = context;
        this.mText = text;
        this.mDuration = duration;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initParams();
        initAutoScrollView();
    }

    /**
     * 初始化AutoScrollTextView
     */
    private void initAutoScrollView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.lighthint, null);
        AutoScrollTextView.Builder builder = new AutoScrollTextView.Builder(mContext, mText)
                .duration(3)
                .scrollSpeed(3)
                .scrollTime(2)
                .textSize(20);
        mAutoScrollTextView = builder.build((AutoScrollTextView) mView.findViewById(R.id.toast_msg));
        mAutoScrollTextView.stopScroll();
        mAutoScrollTextView.setOnTaskStopListener(new AutoScrollTextView.OnTaskStopListener() {
            @Override
            public void onTaskStop() {
                close();
            }
        });
    }

    /**
     * 初始化LayoutParams
     */
    private void initParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP;
        mParams.x = 0;
        mParams.y = getStatusBarHeight();
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        mParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 显示windowLightHint
     */
    @Override
    public void show() {
        mWindowManager.addView(mView,mParams);
        mAutoScrollTextView.startScroll();
    }

    /**
     * 关闭windowLightHint
     */
    @Override
    public void close() {
        mAutoScrollTextView.stopScroll();
        mWindowManager.removeViewImmediate(mView);
    }


    /**
     * @return 状态栏的高度
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
