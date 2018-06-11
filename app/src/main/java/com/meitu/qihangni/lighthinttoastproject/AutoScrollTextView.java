package com.meitu.qihangni.lighthinttoastproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * 跑马灯效果TextView，继承自AppCompatTextView，TextView该有的他都有！
 * 可设置滚动速度、次数和方向，可通过实现滚动结束接口中的回调方法执行特定操作。
 * 默认不滚动，默认滚动速度为1 pixel，默认滚动完成不还原初始状态。
 *
 * @author nqh 2018.05.30
 */
public class AutoScrollTextView extends AppCompatTextView implements Runnable {

    private final String TAG = this.getClass().getName();

    private OnScrollStopListener onScrollStopListener;

    private int mCurrentPosition;
    private boolean mIsStop = false;
    private int mTextWidth;
    private boolean mIsMeasure = false;
    private int mScrollTime = 0;
    private int mScrollSpeed = 1;
    private int mHadScrolled = 0;
    private boolean mIsFresh = false;
    private double mRightSpeed = 0;
    private Paint mPaint;
    private float mTextSize;
    private String mText;
    private float mTextViewWidth;
    private float mTextViewHeight;
    private Context mContext;


    public AutoScrollTextView(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.set(this.getPaint());
        Log.i(TAG, "mPaint inited!");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsMeasure) {
            getTextWidth();
            mIsMeasure = true;
        }
        mPaint.setTextSize(sp2px(mContext, mTextSize));
        int x = (int) (mTextViewWidth / 2 - mPaint.measureText(this.getText().toString()) / 2);
        int y = (int) ((mTextViewHeight / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText(this.getText().toString(), x, y, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "widthMeasureSpec:" + widthMeasureSpec);
        Log.i(TAG, "heightMeasureSpec:" + heightMeasureSpec);

        mTextViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mTextViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension((int) mTextViewWidth, (int) mTextViewHeight);
    }

    @Override
    public void setTextSize(float size) {
        mTextSize = size;
        super.setTextSize(size);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopScroll(null);
        Log.i(TAG, "AutoScrollTextView is onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mText = text.toString();
        super.setText(text, type);
    }

    @Override
    public void run() {
        if (mRightSpeed < mScrollSpeed) {
            mRightSpeed = mRightSpeed + 0.04;//通过速度增长的形式让用户有反应时间
        }
        mCurrentPosition -= mRightSpeed;
        scrollTo(mCurrentPosition, 0);
        if (mIsStop) {
            return;
        }
        if ((mScrollTime - mHadScrolled) <= 0) {
            stopScroll(null);
            return;
        }
        if (mScrollSpeed >= 0 && getScrollX() <= -((int)mPaint.measureText(mText))) {
            scrollTo((int) mPaint.measureText(mText), 0);
            mCurrentPosition = (int) mPaint.measureText(mText);
            mHadScrolled++;
        } else if (mScrollSpeed < 0 && getScrollX() >= (int)mPaint.measureText(mText)) {
            scrollTo(-(int) mPaint.measureText(mText), 0);
            mCurrentPosition = -(int) mPaint.measureText(mText);
            mHadScrolled++;
        }
        postDelayed(this, 20);
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        mTextWidth = sp2px(mContext, mPaint.measureText(mText));
//        mTextWidth =(int)mPaint.measureText(mText);
    }

    /**
     * 开始滚动（在停止之后调用）
     */
    public void startScroll() {
        mIsStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    public void stopScroll(){
        mIsStop = true;
        this.removeCallbacks(this);
    }

    /**
     * 结束滚动
     *
     * @param param 携带可能需要返回的信息
     */
    public void stopScroll(@Nullable String param) {
        if (mIsFresh) {
            scrollTo(0, 0);
        }
        mIsStop = true;
        if (null != onScrollStopListener) {
            onScrollStopListener.onScrollStop(param);
        }
        this.removeCallbacks(this);
    }

    /**
     * 从头开始滚动（重新开始在这里）
     */
    public void reStartScroll() {
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
            this.onScrollStopListener = onScrollStopListener;
        }

    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
