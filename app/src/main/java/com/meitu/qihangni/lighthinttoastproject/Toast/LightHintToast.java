package com.meitu.qihangni.lighthinttoastproject.Toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.meitu.qihangni.lighthinttoastproject.AutoScrollTextView;
import com.meitu.qihangni.lighthinttoastproject.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;


/**
 * @author nqh 2018/6/5
 */
public class LightHintToast {

    private Context mContext;
    private Toast mToast;
    private AutoScrollTextView mAutoScrollTextView;
    private String mMsg;
    private boolean mCanceled = true;
    private final String TAG = this.getClass().getName();
    private Handler mHandler = new Handler();



    public LightHintToast(Context context, @NonNull int layoutId, String msg) {

        if (context instanceof Activity) {
            mContext = context.getApplicationContext();
        } else if (context instanceof Application) {
            mContext = context;
        }

        mMsg = msg;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(layoutId, null);
        }

        mAutoScrollTextView = view.findViewById(R.id.toast_msg);
        mAutoScrollTextView.setText(msg);
//        mAutoScrollTextView.setOnScrollStopListener(new AutoScrollTextView.OnScrollStopListener() {
//            @Override
//            public void onScrollStop(@Nullable String param) {
//                hide();
//            }
//        });
        Log.i(TAG, "start...");
        if (mToast == null) {
            mToast = new Toast(context);
            Log.i(TAG, "create...");
        }
        mToast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);


    }

    public void show() {
        mToast.show();
        Log.i(TAG, "show...");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },Toast.LENGTH_LONG);

    }

    public void show2(){
        try{
            Field field = mToast.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            Object obj = field.get(mToast);
            //  TN对象中获得了show方法
            Method method =  obj.getClass().getDeclaredMethod("show", null);
            //  调用show方法来显示Toast信息提示框
            method.invoke(obj, mContext);
        }catch (Exception e){

        }
    }

    public void show(int duration) {

    }

    private void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
        mCanceled = true;
        Log.i(TAG, "hide...");
    }


}
