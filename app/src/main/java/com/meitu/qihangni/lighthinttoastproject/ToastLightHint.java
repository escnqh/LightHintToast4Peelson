package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Toast类型的
 * @author nqh 2018/6/13
 */
class ToastLightHint implements WhatLightHint {

    private final String TAG = this.getClass().getName();
    private Context mContext;
    private String mText;
    private int mDuration;
    private AutoScrollTextView mAutoScrollTextView;
    private Toast mToast;
    private View mView;
    private Handler mHandler = new Handler();
    private boolean isCanceled = false;

    public ToastLightHint(Context context, String text, int duration) {
        this.mContext = context;
        this.mText = text;
        this.mDuration = duration;
        initAutoScrollTextView();
        initToast();
    }

    /**
     * 初始化Toast
     */
    private void initToast() {
        if (mToast == null) {
            mToast = new Toast(mContext);
            Log.i(TAG, "create...");
        }
        mToast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(mView);
    }

    /**
     * 初始化AutoScrollTextView
     */
    private void initAutoScrollTextView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = null;
        if (inflater != null) {
            mView = inflater.inflate(R.layout.lighthint, null);
        }
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
     * 显示任务
     */
    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            mToast.show();
            if (!isCanceled) {
                mHandler.postDelayed(mShow, Toast.LENGTH_LONG);
            }
        }
    };

    /**
     * 关闭任务
     */
    private final Runnable mClose = new Runnable() {
        @Override
        public void run() {
            mToast.cancel();
        }
    };

    /**
     * 手动开始
     */
    @Override
    public void show() {
        mHandler.post(mShow);
    }

    /**
     * 手动结束
     */
    @Override
    public void close() {
        mHandler.post(mClose);
        isCanceled = true;
    }
}
