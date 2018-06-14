package com.meitu.qihangni.lighthinttoastproject.old;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * @author nqh 2018/6/8
 */
public class LightHintWindowManager {

    private String TAG = this.getClass().getName();
    private WindowManager mWindowManager;
    private static LightHintWindowManager mInstance;
    private Context mContext;

    public static synchronized LightHintWindowManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LightHintWindowManager(context);
        }
        return mInstance;
    }

    private LightHintWindowManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 添加自定义window
     *
     * @param view
     * @param layoutParams
     * @return
     */
    protected boolean addView(View view, WindowManager.LayoutParams layoutParams) {
        try {
            mWindowManager.addView(view, layoutParams);
            Log.i(TAG, "addView ...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除自定义window
     *
     * @param view
     * @return
     */
    protected boolean removeView(View view) {
        try {
            mWindowManager.removeViewImmediate(view);
            Log.i(TAG, "removeView ...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新Window布局
     *
     * @param view
     * @param layoutParams
     * @return
     */
    protected boolean updateView(View view, WindowManager.LayoutParams layoutParams) {
        try {
            mWindowManager.updateViewLayout(view, layoutParams);
            Log.i(TAG, "updateView ...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拿屏幕的宽度
     *
     * @return
     */
    protected int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 拿状态栏的高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
