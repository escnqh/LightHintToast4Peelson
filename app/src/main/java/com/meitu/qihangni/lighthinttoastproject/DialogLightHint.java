package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.widget.Toast;

/**
 * @author nqh 2018/6/13
 */
public class DialogLightHint implements WhatLightHint {

    private Context mContext;
    private String mText;
    private Toast mToast;
    private int mDuration;

    public DialogLightHint(Context context, String text, int duration) {
        this.mContext = context;
        this.mText = text;
        this.mDuration = duration;
    }

    @Override
    public void show() {

    }

    @Override
    public void close() {

    }
}
