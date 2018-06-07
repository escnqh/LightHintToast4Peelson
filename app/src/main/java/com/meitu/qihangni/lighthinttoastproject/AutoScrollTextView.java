package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * 跑马灯效果TextView，继承自AppCompatTextView，TextView该有的他都有！
 * 可设置滚动速度、次数和方向，可通过实现滚动结束接口中的回调方法执行特定操作。
 * 默认不滚动，默认滚动速度为1 pixel，默认滚动完成不还原初始状态。
 *
 * @author nqh 2018.05.30
 */
public class AutoScrollTextView extends AppCompatTextView implements Runnable {

    private OnScrollStopListener onScrollStopListener;

    private int mCurrentPosition;
    private boolean mIsStop = false;
    private int mTextWidth;
    private boolean mIsMeasure = false;
    private int mScrollTime = 0;
    private int mScrollSpeed = 1;
    private int mHadScrolled = 0;
    private boolean mIsFresh = false;
    private boolean mIsListen = false;


    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsMeasure) {
            getTextWidth();
            mIsMeasure = true;
        }
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        startScroll();//触发时机
    }

    @Override
    public void run() {
        mCurrentPosition -= mScrollSpeed;
        scrollTo(mCurrentPosition, 0);
        if (mIsStop) {
            return;
        }
        if ((mScrollTime - mHadScrolled) <= 0) {
            stopScroll(null);
            return;
        }
        if (mScrollSpeed >= 0 && getScrollX() <= -(this.getWidth())) {
            scrollTo(mTextWidth, 0);
            mCurrentPosition = mTextWidth;
            mHadScrolled++;
        } else if (mScrollSpeed < 0 && getScrollX() >= this.getWidth()) {
            scrollTo(-mTextWidth, 0);
            mCurrentPosition = -mTextWidth;
            mHadScrolled++;
        }
        postDelayed(this, 20);
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String string = this.getText().toString();
        mTextWidth = (int) paint.measureText(string);
    }

    /**
     * 开始滚动（在停止之后调用）
     */
    public void startScroll() {
        mIsStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    /**
     * 停止滚动
     *
     * @param param 携带可能需要返回的信息
     */
    public void stopScroll(@Nullable String param) {
        if (mIsFresh) scrollTo(0, 0);
        mIsStop = true;
        if (mIsListen)
            onScrollStopListener.onScrollStop(param);
        this.removeCallbacks(this);
    }

    /**
     * 从头开始滚动（重新开始在这里）
     */
    public void startFromHead() {
        mCurrentPosition = 0;
        mHadScrolled = 0;
        startScroll();
    }

    /**
     * 设置是否刷新初始状态的开关
     */
    public void setFreshWhenStop() {
        mIsFresh = true;
    }

    /**
     * 设定滚动次数
     *
     * @param scrollTime 滚动次数
     */
    public void setScrollTime(int scrollTime) {
        mScrollTime = scrollTime;
    }


    /**
     * 设定滚动速度,正值往右负值往左
     *
     * @param scrollSpeed 滚动速度
     */
    public void setScrollSpeed(int scrollSpeed) {
        mScrollSpeed = scrollSpeed;
    }


    public interface OnScrollStopListener {
        void onScrollStop(@Nullable String param);
    }

    public void setOnScrollStopListener(OnScrollStopListener onScrollStopListener) {
        if (onScrollStopListener != null) {
            mIsListen = true;
            this.onScrollStopListener = onScrollStopListener;
        }

    }


}
