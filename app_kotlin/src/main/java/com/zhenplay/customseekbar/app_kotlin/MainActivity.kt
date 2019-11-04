package com.zhenplay.customseekbar.app_kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by KINCAI
 *
 * Desc TODO
 *
 * Date 2019-11-04 18:24
 */
class MainActivity : AppCompatActivity() {
    private var cacheProgress = 0
    internal var progress: Int = 0
    private var maxProgress = 60
    private var customSeekBar: CustomSeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customSeekBar = findViewById(R.id.seekbar)
        customSeekBar!!.setMaxProgress(maxProgress)//最大进度s
        customSeekBar!!.setProgressBarHeight(1.0f)//进度条高度dp 默认1.0f
        customSeekBar!!.setCacheProgressBarHeight(1.5f)//缓存条高度dp 默认1.5f
        customSeekBar!!.setProgressBarColor(android.R.color.darker_gray)//进度条颜色colorId
        customSeekBar!!.setCacheProgressBarColor(android.R.color.white)//缓存条颜色colorId
        customSeekBar!!.setTextBgColor(android.R.color.white)//文字背景颜色colorId
        customSeekBar!!.setTextColor(android.R.color.black)//字体颜色colorId
        customSeekBar!!.setTextSize(10)//文字大小sp 默认10sp
        //设置进度拖动监听
        customSeekBar!!.setProgressListener(object : CustomSeekBar.IProgressListener {
            override fun progress(progress: Int) {
                this@MainActivity.progress = progress
            }
        })
        start()

    }

    private fun start() {
        Thread(Runnable {
            while (true) {
                try {
                    if (cacheProgress < maxProgress) {
                        cacheProgress += 4
                        customSeekBar!!.cacheProgress(cacheProgress)
                    }
                    customSeekBar!!.progress(progress)
                    if (progress >= maxProgress) {
                        break
                    }
                    progress += 1
                    Log.e("ss", "ss$progress")

                    Thread.sleep(1000)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()
    }
}
