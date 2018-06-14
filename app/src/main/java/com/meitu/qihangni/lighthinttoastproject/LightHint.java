package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * 轻提示管理类，主要负责策略选择
 *
 * @author nqh 2018/6/13
 */
public class LightHint {
    private WhatLightHint whatLightHint;
    private static LightHint mInstance;
    private final WeakReference<Context> mWeakContext;
    private String mText;
    private int mDuration = 3;
    private final int mVersionOfDevice = Build.VERSION.SDK_INT;

    public static synchronized LightHint getInstance(Context context, String text, int duration) {
        if (mInstance == null) {
            mInstance = new LightHint(context, text, duration);
        }
        return mInstance;
    }

    private LightHint(Context context, String text, int duration) {
        mWeakContext = new WeakReference<>(context);
        this.mText = text;
        selectLightHint();
    }

    /**
     * 不同权限下的不同策略选择
     */
    private void selectLightHint() {
        if (mVersionOfDevice < 24 && !(isMiui() || isMiuiDevices())) {
            whatLightHint = new WindowLightHint(mWeakContext.get(), mText, mDuration);
        } else {
            if (isNotificationsEnabled()) {
                whatLightHint = new ToastLightHint(mWeakContext.get(), mText, mDuration);
            } else {
                whatLightHint = new DialogLightHint(mWeakContext.get(), mText, mDuration);
            }
        }
    }

    /**
     * 显示
     */
    public void show() {
        if (whatLightHint != null) {
            whatLightHint.show();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (whatLightHint != null) {
            whatLightHint.close();
        }
    }

    /**
     * @return 是否是小米设备
     */
    private static boolean isMiuiDevices() {
        String devicesName = Build.MANUFACTURER;
        if ("Xiaomi".equals(devicesName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return 是否是MIUI
     */
    private static boolean isMiui() {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            if (!TextUtils.isEmpty(version)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return 是否有通知权限
     */
    private boolean isNotificationsEnabled() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mWeakContext.get().getApplicationContext());
        return managerCompat.areNotificationsEnabled();
    }

}
