package com.kincai.customseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/**
 * Created by KINCAI
 * <p>
 * Desc 自定义SeekBar 资源配置属性暂未实现
 * <p>
 * Date 2019-10-28 16:38
 */
public class CustomSeekBar extends View {
    private final String TAG = this.getClass().getSimpleName();
    private int seekBarDefaultWidth;
    private int textBgHeight;
    private int textBgWidth;
    private int cacheProgressBarHeight;
    private int progressBarHeight;
    private int cacheProgressBarColor;
    private int progressBarColor;
    private int textColor;
    private int textBgColor;
    private int maxProgress;
    private int progress;
    private int cacheProgress;
    private int minCircleRadius;
    private int textSize;

    private Paint cacheProgressBarPaint = new Paint();
    private Paint progressBarPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint textBgPaint = new Paint();
    /**小圆点直径*/
    private float dia;
    private int textHeight;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 默认宽度为屏幕宽
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        seekBarDefaultWidth = dm.widthPixels;
        textBgWidth = dp2px(200);
        textBgHeight = dp2px(16);
        cacheProgressBarHeight = dp2px(1.5f);
        progressBarHeight = dp2px(1f);
        setCircleParam();
        textSize = sp2px(10);

        progressBarColor = Color.parseColor("#d9ead3");
        cacheProgressBarColor = Color.parseColor("#ffffff");
        textColor = Color.parseColor("#6e6e6e");
        textBgColor = Color.parseColor("#ffffff");

        progressBarPaint.setColor(progressBarColor);
        progressBarPaint.setAntiAlias(false);
        progressBarPaint.setStyle(Paint.Style.FILL);
        progressBarPaint.setStrokeCap(Paint.Cap.ROUND);

        cacheProgressBarPaint.setColor(cacheProgressBarColor);
        cacheProgressBarPaint.setAntiAlias(false);
        cacheProgressBarPaint.setStyle(Paint.Style.FILL);

        textBgPaint.setColor(textBgColor);
        textBgPaint.setAntiAlias(true);
        textBgPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(textSize);
        textHeight = getTextHeight(textPaint, "00:00/00:00");
        textBgHeight = textHeight + dp2px(8);
    }

    private void setCircleParam() {
        minCircleRadius = progressBarHeight;
        dia = minCircleRadius * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = seekBarDefaultWidth;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //当wrap_content的时候 高度为textBgHeight
            // 或者是所有部分的最高那个高度
            height = textBgHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        float progressBarRealWidth = width - dia * 2;
        Log.e(TAG, "width：" + width + " realWidth:" + progressBarRealWidth);
        // 鸡巴 两头还有两个圆点
        canvas.drawCircle(minCircleRadius, height >> 1, minCircleRadius, cacheProgressBarPaint);
        canvas.drawCircle(width - minCircleRadius, height >> 1, minCircleRadius, progressBarPaint);
        // 画背景条
        canvas.drawRect(dia
                , (height >> 1) - (progressBarHeight >> 1)
                , width - dia
                , (height >> 1) + (progressBarHeight >> 1)
                , progressBarPaint);
        // 画缓存条
        float currentCache = (float) cacheProgress * progressBarRealWidth / maxProgress + dia;
        canvas.drawRect(dia
                , (height >> 1) - (cacheProgressBarHeight >> 1)
                , currentCache, (height >> 1) + (cacheProgressBarHeight >> 1)
                , cacheProgressBarPaint);

        // 判断界限
        if (progress < 0) {
            progress = 0;
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        // 计算文字，文字长度可能会变 会不断的测量
        String progressText = getProgressText();
        int textWidth = getTextWidth(textPaint, progressText);
        // 画文字背景 背景宽高随文字变化而变化
        textBgWidth = textWidth + dp2px(12);
        float textBgStartX = (float) progress * (progressBarRealWidth) / maxProgress - (float) textBgWidth / maxProgress * progress + dia;
        float textBgEndX = textBgStartX + textBgWidth;
        Log.e(TAG, "width " + width + ", textBgWidth " + textBgWidth + ", textBgStartX " + textBgStartX);
        canvas.drawRoundRect(textBgStartX
                , (height >> 1) - (textBgHeight >> 1)
                , textBgEndX
                , (height >> 1) + (textBgHeight >> 1)
                , textBgWidth >> 1 //x半径
                , textBgWidth >> 1 //y半径
                , textBgPaint);
        //画文字
        //计算起点
        float textStartY = (height >> 1) + (textHeight >> 1);
        float textStartX = textBgStartX + ((textBgWidth >> 1) - (textWidth >> 1));
        canvas.drawText(progressText, textStartX, textStartY, textPaint);

    }

    private int getTextWidth(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    private int getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    private String getProgressText() {
        String progressText = formatProgress(this.progress);
        String maxProgressText = formatProgress(this.maxProgress);
        return progressText + "/" + maxProgressText;
    }

    /**
     * @param seconds 进度(秒)
     * @return
     */
    private String formatProgress(int seconds) {
        String standardTime;
        if (seconds <= 0) {
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }


    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * @param progress 进度(秒)
     */
    public void cacheProgress(int progress) {
        this.cacheProgress = progress;
        postInvalidate();
    }

    /**
     * @param progress 进度(秒)
     */
    public void progress(int progress) {
        if (mDrag) {
            return;
        }
        this.progress = progress;
        postInvalidate();
    }

    /**
     * @param cacheProgressBarHeight 缓存条高度 单位dp
     */
    public void setCacheProgressBarHeight(float cacheProgressBarHeight) {
        this.cacheProgressBarHeight = dp2px(cacheProgressBarHeight);
    }

    /**
     * @param progressBarHeight 进度条高度 单位dp
     */
    public void setProgressBarHeight(float progressBarHeight) {
        this.progressBarHeight = dp2px(progressBarHeight);
        setCircleParam();
    }

    public void setCacheProgressBarColor(@ColorRes int cacheProgressBarColor) {
        this.cacheProgressBarColor = ContextCompat.getColor(getContext(), cacheProgressBarColor);
        cacheProgressBarPaint.setColor(this.cacheProgressBarColor);
    }

    public void setProgressBarColor(@ColorRes int progressBarColor) {
        this.progressBarColor = ContextCompat.getColor(getContext(), progressBarColor);
        progressBarPaint.setColor(this.progressBarColor);
    }

    public void setTextColor(@ColorRes int textColor) {
        this.textColor = ContextCompat.getColor(getContext(), textColor);
        textPaint.setColor(this.textColor);
    }

    public void setTextBgColor(@ColorRes int textBgColor) {
        this.textBgColor = ContextCompat.getColor(getContext(), textBgColor);
        textBgPaint.setColor(this.textBgColor);
    }

    /**
     * @param textSize 文字大小 单位sp
     */
    public void setTextSize(int textSize) {
        this.textSize = sp2px(textSize);
    }

    private boolean mDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touchUpdate(event.getX());
                break;
            case MotionEvent.ACTION_UP:
                mDrag = false;
                touchUpdate(event.getX());
                if (iProgressListener != null) {
                    iProgressListener.progress(progress);
                }
                break;
        }
        return true;
    }

    private void touchUpdate(float x) {
        progress = (int) (x * maxProgress / getWidth());
        postInvalidate();
    }

    IProgressListener iProgressListener;

    public void setProgressListener(IProgressListener iProgressListener) {
        this.iProgressListener = iProgressListener;
    }

    public interface IProgressListener {
        void progress(int progress);
    }

    private int dp2px(float var1) {
        float var2 = getContext().getResources().getDisplayMetrics().density;
        return (int) (var1 * var2 + 0.5F);
    }

    private int sp2px(float var1) {
        float var2 = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 * var2 + 0.5F);
    }
}
