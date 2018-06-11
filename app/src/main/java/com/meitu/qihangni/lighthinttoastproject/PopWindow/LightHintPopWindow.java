package com.meitu.qihangni.lighthinttoastproject.PopWindow;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.meitu.qihangni.lighthinttoastproject.AutoScrollTextView;
import com.meitu.qihangni.lighthinttoastproject.R;

/**
 * @author nqh 2018/6/7
 */
public class LightHintPopWindow extends PopupWindow {
    private Context mContext;
    private View mLightHintPopWindow;
    private AutoScrollTextView mAutoScrollTextView;
    private String mMsg;
    private final String TAG = this.getClass().getName();

    public LightHintPopWindow(Context context, int layoutId, String msg) {
        this.mMsg = msg;
        if (context instanceof Activity) {
            mContext = context.getApplicationContext();
        } else if (context instanceof Application) {
            mContext = context;
        }
//        mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (layoutInflater != null) {
            mLightHintPopWindow = layoutInflater.inflate(layoutId, null);
            this.setContentView(mLightHintPopWindow);
            //设置弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //设置弹出窗体的高
            this.setHeight(dip2px(mContext, 60));
            //  设置弹出窗体可点击
            this.setFocusable(false);
            //   设置背景透明
            this.setBackgroundDrawable(new ColorDrawable(0x00000000));
            Log.i(TAG, "create nice...");
        } else {
            Log.i(TAG, "create error...");
        }

        mAutoScrollTextView = mLightHintPopWindow.findViewById(R.id.toast_msg);
        mAutoScrollTextView.setText(msg);
//        mAutoScrollTextView.setOnScrollStopListener(new AutoScrollTextView.OnScrollStopListener() {
//            @Override
//            public void onScrollStop(@Nullable String param) {
//                close();
//            }
//        });
        Log.i(TAG, "start...");

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
