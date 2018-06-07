package com.meitu.qihangni.lighthinttoastproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


/**
 * @author nqh 2018/6/5
 */
public class LightHintToast {

    private Context mContext;
    private Toast mToast;
    private AutoScrollTextView mAutoScrollTextView;
    private String mMsg;
    private boolean mCanceled = true;
    private Handler mHandler = new Handler();


    public LightHintToast(Context context, @NonNull int layoutId, String msg) {

        if (context instanceof Activity) {
            mContext = context.getApplicationContext();
        } else if (context instanceof Application) {
            mContext = context;
        }

        mMsg = msg;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);

        mAutoScrollTextView = view.findViewById(R.id.toast_msg);
        mAutoScrollTextView.setText(msg);
        if (mToast == null) {
            mToast = new Toast(context);
        }

    }
}
