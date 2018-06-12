package com.meitu.qihangni.lighthinttoastproject.Window;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.meitu.qihangni.lighthinttoastproject.AutoScrollTextView;
import com.meitu.qihangni.lighthinttoastproject.GetPermissionUtil;
import com.meitu.qihangni.lighthinttoastproject.R;

import java.lang.reflect.Method;

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
    private int mDuration = 1;//默认1s
    private boolean isScroll = false;
    private String mMsg;

    public LightHintWindow(@NonNull Context context, String msg, int duration, int textSize) {
        super(context);
        mContext = context;

        checkPermission();

        mMsg = msg;
        mDuration = duration;
        mLightHintWindowManager = LightHintWindowManager.getInstance(mContext);
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

    /**
     * 检查悬浮窗权限
     */
    private void checkPermission() {
        if (!GetPermissionUtil.checkFloatWindowPermission(mContext)) {
            if (Build.VERSION.SDK_INT >= 23) {//对6.0以上的做统一处理
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    boolean result = (Boolean) canDrawOverlays.invoke(null, mContext);
                    if (!result) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            } else {
                if (GetPermissionUtil.checkIsHuaweiRom()) {
                    GetPermissionUtil.applyHuaweiPermission(mContext);
                } else if (GetPermissionUtil.checkIsMiuiRom()) {
                    GetPermissionUtil.applyMiuiPermission(mContext);
                }
            }
            Toast.makeText(mContext, "请授予悬浮窗权限！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示
     */
    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP;
        mParams.x = 0;
        mParams.y = mLightHintWindowManager.getStatusBarHeight();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        if (isScroll) {
            Log.i(TAG, "is Scroll...");
            mLightHintWindowManager.addView(mView, mParams);
            mAutoScrollTextView.setScrollSpeed(3);
            mAutoScrollTextView.setScrollTime(2);
            mAutoScrollTextView.setOnScrollStopListener(new AutoScrollTextView.OnScrollStopListener() {
                @Override
                public void onScrollStop(@Nullable String param) {
                    close();
                }
            });
            mAutoScrollTextView.startScroll();
        } else {
            mLightHintWindowManager.addView(mView, mParams);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }, mDuration * 1000);
        }
    }

    /**
     * 关闭
     */
    public void close() {
        mLightHintWindowManager.removeView(mView);
    }


    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
