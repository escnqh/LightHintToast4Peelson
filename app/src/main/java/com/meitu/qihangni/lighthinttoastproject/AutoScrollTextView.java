package com.meitu.qihangni.lighthinttoastproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.ref.WeakReference;


/**
 * 走马灯效果TextView，判断文本长度是否超过控件的宽度，如果超过则滚动显示，否则显示若干秒后执行任务。
 * 这个时间和滚动次数可以控制，结束后会回调接口。
 * 当然你也可以通过提供的方法来控制滚动行为。
 *
 * @author nqh 2018/6/13
 */
public class AutoScrollTextView extends AppCompatTextView {

    private String TAG = this.getClass().getName();
    private final int FLAG_MEASURE_FINISHED = 0;
    private final int FLAG_SCROLL = 1;
    private final int FLAG_NOTSCROLL = 2;
    private int mTextViewWidth;
    private int mTextViewHeight;
    private boolean isMeasured = false;
    private MyHandler myHandler;
    private Paint mPaint;
    private double mRightSpeed = 0;
    private int mScrollSpeed;
    private int mScrollTime;
    private int mDuration;
    private int mCurrentPosition;
    private boolean isStop;
    private int mHadScrolled = 0;
    private String mText;
    private boolean isForceScroll = false;
    private OnTaskStopListener onTaskStopListener;


    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绘制Text
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int x = (int) (mTextViewWidth / 2 - mPaint.measureText(mText) / 2);
        int y = (int) ((mTextViewHeight / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText(mText, x, y, mPaint);
        Log.i(TAG, "onDraw!");
    }

    /**
     * 测量结束后交给滚动任务处理
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            mTextViewHeight = getMeasuredHeight();
            mTextViewWidth = getMeasuredWidth();
            isMeasured = true;
            Message msg = new Message();
            msg.what = FLAG_MEASURE_FINISHED;
            myHandler.sendMessage(msg);
            Log.i(TAG, "measured!");
        }
    }

    /**
     * 处理滚动任务，在用户没有设置强制滚动的情况下，根据{@link #isTextTooLong()}判断是否滚动
     */
    public void handlerScroll() {
        Message msg = new Message();
        if (isForceScroll) {
            msg.what = FLAG_SCROLL;
        } else {
            if (isTextTooLong()) {
                msg.what = FLAG_SCROLL;
            } else {
                msg.what = FLAG_NOTSCROLL;
            }
        }
        myHandler.sendMessage(msg);
    }

    /**
     * 手动开始滚动，与{@link #stopScroll()}相关联
     */
    public void startScroll() {
        isStop = false;
        myHandler.removeCallbacks(mScroll);
        mCurrentPosition = 0;
        mHadScrolled = 0;
        mHadScrolled = 0;
        myHandler.post(mScroll);
    }

    /**
     * 手动结束滚动，与{@link #startScroll()}相关联
     */
    public void stopScroll() {
        isStop = true;
        myHandler.removeCallbacks(mScroll);
    }

    /**
     * 计算文本是否超过控件显示长度
     *
     * @return
     */
    public boolean isTextTooLong() {
        Log.i(TAG, "mTextViewWidth: " + mTextViewWidth + " textWidth: " + mPaint.measureText(mText));
        if (mTextViewWidth < mPaint.measureText(mText)) {
            return true;
        }
        return false;
    }


    /**
     * 初始化控件的相关信息
     *
     * @param context
     * @param text          文本
     * @param duration      显示时间用于{@link #isTextTooLong()}返回false的情况
     * @param textSize      字体大小
     * @param scrollSpeed   滚动速度
     * @param scrollTime    滚动次数
     * @param isForceScroll 是否强制滚动
     */
    private void initView(Context context, String text, int duration, int textSize, int scrollSpeed, int scrollTime, boolean isForceScroll) {
        mPaint = new Paint();
        mPaint.set(this.getPaint());
        Log.i(TAG, "mPaint inited!");
        myHandler = new MyHandler(context);
        mPaint.setTextSize(sp2px(context, textSize));
        this.mScrollSpeed = scrollSpeed;
        this.mScrollTime = scrollTime;
        this.mDuration = duration;
        this.mText = text;
        this.isForceScroll = isForceScroll;
    }

    /**
     * 设置任务结束监听
     *
     * @param onTaskStopListener
     */
    public void setOnTaskStopListener(OnTaskStopListener onTaskStopListener) {
        if (onTaskStopListener != null) {
            this.onTaskStopListener = onTaskStopListener;
        }
    }


    /**
     * 滚动显示任务
     */
    private final Runnable mScroll = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "monScroll!");
            if (mRightSpeed < mScrollSpeed) {
                mRightSpeed = mRightSpeed + 0.04;//通过速度增长的形式让用户有反应时间
            }
            mCurrentPosition -= mRightSpeed;
            scrollTo(mCurrentPosition, 0);
            if (isStop) {
                return;
            }
            if ((mScrollTime - mHadScrolled) <= 0) {
                if (null != onTaskStopListener) {
                    onTaskStopListener.onTaskStop();
                }
                return;
            }
            //当text到达边界之时将会移动到初始位置
            if (mScrollSpeed >= 0 && getScrollX() <= -((int) mPaint.measureText(mText))) {
                scrollTo((int) mPaint.measureText(mText), 0);
                mCurrentPosition = (int) mPaint.measureText(mText);
                mHadScrolled++;
            } else if (mScrollSpeed < 0 && getScrollX() >= (int) mPaint.measureText(mText)) {
                scrollTo(-(int) mPaint.measureText(mText), 0);
                mCurrentPosition = -(int) mPaint.measureText(mText);
                mHadScrolled++;
            }
            invalidate();//刷新界面
            myHandler.postDelayed(mScroll, 17);
        }
    };

    /**
     * 定时显示任务
     */
    private final Runnable mNotScroll = new Runnable() {
        @Override
        public void run() {
            if (null != onTaskStopListener) {
                onTaskStopListener.onTaskStop();
            }
        }
    };

    /**
     * sp转px工具
     *
     * @param context
     * @param spValue
     * @return
     */
    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 初始化建造类
     */
    public static class Builder {
        private final WeakReference<Context> weakContext;
        private String text;
        private int duration = 3;
        private int textSize = 20;
        private int scrollSpeed = 5;
        private int scrollTime = 2;
        private boolean isScroll = false;

        /**
         * 设置Context环境和文本内容
         *
         * @param context
         * @param text
         */
        public Builder(Context context, String text) {
            weakContext = new WeakReference<>(context);
            this.text = text;
        }

        /**
         * 设置定时间隔
         *
         * @param duration
         * @return
         */
        public Builder duration(int duration) {
            if (duration < 0) {
                this.duration = 0;
            } else {
                this.duration = duration;
            }
            return this;
        }

        /**
         * 设置字体大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            if (textSize < 0) {
                this.textSize = 0;
            } else {
                this.textSize = textSize;
            }
            return this;
        }

        /**
         * 设置滚动速度,小于0向左移动，大于0向右
         *
         * @param scrollSpeed
         * @return
         */
        public Builder scrollSpeed(int scrollSpeed) {
            this.scrollSpeed = scrollSpeed;
            return this;
        }

        /**
         * 设置滚动次数
         *
         * @param scrollTime
         * @return
         */
        public Builder scrollTime(int scrollTime) {
            if (scrollTime < 0) {
                this.scrollTime = 0;
            } else {
                this.scrollTime = scrollTime;
            }
            return this;
        }

        /**
         * 设置是否强制滚动
         *
         * @param isScroll
         * @return
         */
        public Builder forceScroll(boolean isScroll) {
            this.isScroll = true;
            return this;
        }

        /**
         * 建造view，需要注入绑定的控件
         *
         * @param autoScrollTextViewFix
         * @return
         */
        public AutoScrollTextView build(AutoScrollTextView autoScrollTextViewFix) {
            autoScrollTextViewFix.initView(weakContext.get(), text, duration, textSize, scrollSpeed, scrollTime, isScroll);
            return autoScrollTextViewFix;
        }
    }

    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler {
        private final WeakReference<Context> weakContext;

        public MyHandler(Context context) {
            weakContext = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = weakContext.get();
            super.handleMessage(msg);
            if (context != null) {
                switch (msg.what) {
                    case FLAG_MEASURE_FINISHED:
                        handlerScroll();
                        break;
                    case FLAG_SCROLL:
                        myHandler.post(mScroll);
                        break;
                    case FLAG_NOTSCROLL:
                        myHandler.postDelayed(mNotScroll, mDuration * 1000);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 结束任务回调接口
     */
    public interface OnTaskStopListener {
        void onTaskStop();
    }
}
