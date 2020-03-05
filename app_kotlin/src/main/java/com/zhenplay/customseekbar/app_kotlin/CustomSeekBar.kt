package com.zhenplay.customseekbar.app_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by KINCAI
 * <p>
 * Desc 自定义SeekBar 资源配置属性暂未实现
 * <p>
 * Date 2019-10-28 16:38
 */
class CustomSeekBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    private val TAG: String = this.javaClass.simpleName
    private var seekBarDefaultWidth: Int = 0
    private var textBgHeight: Int = 0
    private var textBgWidth: Int = 0
    private var cacheProgressBarHeight: Int = 0
    private var progressBarHeight: Int = 0
    private var cacheProgressBarColor: Int = 0
    private var progressBarColor: Int = 0
    private var textColor: Int = 0
    private var textBgColor: Int = 0
    private var maxProgress: Int = 0
    private var progress: Int = 0
    private var cacheProgress: Int = 0
    private var minCircleRadius: Float = 0F
    private var textSize: Int = 0

    private val cacheProgressBarPaint: Paint = Paint()
    private val progressBarPaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val textBgPaint: Paint = Paint()
    /**小圆点直径*/
    private var dia: Float = 0F
    private var textHeight: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        init()
    }

    private fun init() {
        // 默认宽度为屏幕宽
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        seekBarDefaultWidth = dm.widthPixels
        textBgWidth = dp2px(200F)
        textBgHeight = dp2px(16F)
        cacheProgressBarHeight = dp2px(1.5f)
        progressBarHeight = dp2px(1f)
        setCircleParam()
        textSize = sp2px(10F)

        progressBarColor = Color.parseColor("#d9ead3")
        cacheProgressBarColor = Color.parseColor("#ffffff")
        textColor = Color.parseColor("#6e6e6e")
        textBgColor = Color.parseColor("#ffffff")

        progressBarPaint.color = progressBarColor
        progressBarPaint.isAntiAlias = false
        progressBarPaint.style = Paint.Style.FILL
        progressBarPaint.strokeCap = Paint.Cap.ROUND

        cacheProgressBarPaint.color = cacheProgressBarColor
        cacheProgressBarPaint.isAntiAlias = false
        cacheProgressBarPaint.style = Paint.Style.FILL

        textBgPaint.color = textBgColor
        textBgPaint.isAntiAlias = true
        textBgPaint.style = Paint.Style.FILL

        textPaint.color = textColor
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.typeface = Typeface.DEFAULT
        textPaint.textSize = textSize.toFloat()
        textHeight = getTextHeight(textPaint, "00:00/00:00")
        textBgHeight = textHeight + dp2px(8F)
    }

    private fun setCircleParam() {
        minCircleRadius = progressBarHeight.toFloat()
        dia = minCircleRadius * 2
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //测量宽高
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        var height: Int

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width = seekBarDefaultWidth
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize)
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            //当wrap_content的时候 高度为textBgHeight
            // 或者是所有部分的最高那个高度
            height = textBgHeight
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val height = height
        val width = width
        val progressBarRealWidth: Float = width - dia * 2
        Log.e(TAG, "width：$width realWidth:$progressBarRealWidth")
        // 鸡巴 两头还有两个圆点
        canvas.drawCircle(minCircleRadius, (height / 2).toFloat(), minCircleRadius, cacheProgressBarPaint)
        canvas.drawCircle(width - minCircleRadius, (height / 2).toFloat(), minCircleRadius, progressBarPaint)
        // 画背景条
        canvas.drawRect(dia, ((height / 2) - (progressBarHeight / 2)).toFloat()
                , width - dia
                , ((height / 2) + (progressBarHeight / 2)).toFloat()
                , progressBarPaint)
        // 画缓存条
        val currentCache: Float = cacheProgress * progressBarRealWidth / maxProgress + dia
        canvas.drawRect(dia, ((height / 2) - (cacheProgressBarHeight / 2)).toFloat()
                , currentCache, ((height / 2) + (cacheProgressBarHeight / 2)).toFloat()
                , cacheProgressBarPaint)

        // 判断界限
        if (progress < 0) {
            progress = 0
        }
        if (progress > maxProgress) {
            progress = maxProgress
        }
        // 计算文字，文字长度可能会变 会不断的测量
        val progressText: String = getProgressText()
        val textWidth: Int = getTextWidth(textPaint, progressText)
        // 画文字背景 背景宽高随文字变化而变化
        textBgWidth = textWidth + dp2px(12F)
        val textBgStartX = progress.toFloat() * (progressBarRealWidth) / maxProgress - textBgWidth.toFloat() / maxProgress * progress + dia
        val textBgEndX = textBgStartX + textBgWidth
        Log.e(TAG, "width $width, textBgWidth $textBgWidth, textBgStartX $textBgStartX")
        canvas.drawRoundRect(textBgStartX
                , ((height / 2) - (textBgHeight / 2)).toFloat()
                , textBgEndX
                , ((height / 2) + (textBgHeight / 2)).toFloat()
                , (textBgWidth / 2).toFloat() //x半径
                , (textBgWidth / 2).toFloat() //y半径
                , textBgPaint)
        //画文字
        //计算起点
        val textStartY: Float = ((height / 2) + (textHeight / 2)).toFloat()
        val textStartX: Float = textBgStartX + ((textBgWidth / 2) - (textWidth / 2))
        canvas.drawText(progressText, textStartX, textStartY, textPaint)

    }

    private fun getTextWidth(paint: Paint, str: String): Int {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.width()
    }

    private fun getTextHeight(paint: Paint, str: String): Int {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.height()
    }

    private fun getProgressText(): String {
        val progressText: String = formatProgress(this.progress)
        val maxProgressText: String = formatProgress(this.maxProgress)
        return "$progressText/$maxProgressText"
    }

    /**
     * @param seconds 进度(秒)
     * @return
     */
    private fun formatProgress(seconds: Int): String {
        val standardTime: String
        if (seconds <= 0) {
            standardTime = "00:00"
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60)
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60)
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60)
        }
        return standardTime
    }


    fun setMaxProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
    }

    /**
     * @param progress 进度(秒)
     */
    fun cacheProgress(progress: Int) {
        this.cacheProgress = progress
        postInvalidate()
    }

    /**
     * @param progress 进度(秒)
     */
    fun progress(progress: Int) {
        if (mDrag) {
            return
        }
        this.progress = progress
        postInvalidate()
    }

    /**
     * @param cacheProgressBarHeight 缓存条高度 单位dp
     */
    fun setCacheProgressBarHeight(cacheProgressBarHeight: Float) {
        this.cacheProgressBarHeight = dp2px(cacheProgressBarHeight)
    }

    /**
     * @param progressBarHeight 进度条高度 单位dp
     */
    fun setProgressBarHeight(progressBarHeight: Float) {
        this.progressBarHeight = dp2px(progressBarHeight)
        setCircleParam()
    }

    fun setCacheProgressBarColor(@ColorRes cacheProgressBarColor: Int) {
        this.cacheProgressBarColor = ContextCompat.getColor(context, cacheProgressBarColor)
        cacheProgressBarPaint.color = this.cacheProgressBarColor
    }

    fun setProgressBarColor(@ColorRes progressBarColor: Int) {
        this.progressBarColor = ContextCompat.getColor(context, progressBarColor)
        progressBarPaint.color = this.progressBarColor
    }

    fun setTextColor(@ColorRes textColor: Int) {
        this.textColor = ContextCompat.getColor(context, textColor)
        textPaint.color = this.textColor
    }

    fun setTextBgColor(@ColorRes textBgColor: Int) {
        this.textBgColor = ContextCompat.getColor(context, textBgColor)
        textBgPaint.color = this.textBgColor
    }

    /**
     * @param textSize 文字大小 单位sp
     */
    fun setTextSize(textSize: Int) {
        this.textSize = sp2px(textSize.toFloat())
    }

    private var mDrag: Boolean = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrag = true
            }

            MotionEvent.ACTION_MOVE -> {
                touchUpdate(event.x)
            }

            MotionEvent.ACTION_UP -> {
                mDrag = false
                touchUpdate(event.x)
                iProgressListener?.progress(progress)
            }
        }

        return true
    }

    private fun touchUpdate(x: Float) {
        progress = ((x * maxProgress / width).toInt())
        postInvalidate()
    }

    private var iProgressListener: IProgressListener? = null

    fun setProgressListener(iProgressListener: IProgressListener) {
        this.iProgressListener = iProgressListener
    }

    interface IProgressListener {
        fun progress(progress: Int)
    }

    private fun dp2px(var1: Float): Int {
        val var2: Float = context.resources.displayMetrics.density
        return ((var1 * var2 + 0.5F).toInt())
    }

    private fun sp2px(var1: Float): Int {
        val var2: Float = context.resources.displayMetrics.scaledDensity
        return ((var1 * var2 + 0.5F).toInt())
    }
}
