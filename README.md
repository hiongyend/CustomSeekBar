# CustomSeekBar
防喜马拉雅6.6.21.3播放进度条 {java kotlin}

[博客地址](https://blog.csdn.net/hqiong208/article/details/106211885)

## 使用
```xml
<!--布局文件引入-->
<com.kincai.customseekbar.CustomSeekBar
    android:id="@+id/seekbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

java
```java
CustomSeekBar customSeekBar = findViewById(R.id.seekbar);
customSeekBar.setMaxProgress(maxProgress);//最大进度s
customSeekBar.setProgressBarHeight(1.0f);//进度条高度dp
customSeekBar.setCacheProgressBarHeight(1.5f);//缓存条高度dp
customSeekBar.setProgressBarColor(android.R.color.holo_green_light);//进度条颜色colorId
customSeekBar.setCacheProgressBarColor(android.R.color.white);//缓存条颜色colorId
customSeekBar.setTextBgColor(android.R.color.white);//文字背景颜色colorId
customSeekBar.setTextColor(android.R.color.black);//字体颜色colorId
customSeekBar.setTextSize(10);//文字大小sp
// 设置进度拖动监听
// 手动拖动进度条会返回当前进度
customSeekBar.setProgressListener(new CustomSeekBar.IProgressListener() {
    @Override
    public void progress(int progress) {
        MainActivity.this.progress = progress;
    }
});
// 设置缓存进度
customSeekBar.cacheProgress(cacheProgress);
// 设置进度
customSeekBar.progress(progress);
```

kotlin
```kotlin
customSeekBar = findViewById(R.id.seekbar)
customSeekBar!!.setMaxProgress(maxProgress)//最大进度s
customSeekBar!!.setProgressBarHeight(1.0f)//进度条高度dp 默认1.0f
customSeekBar!!.setCacheProgressBarHeight(1.5f)//缓存条高度dp 默认1.5f
customSeekBar!!.setProgressBarColor(android.R.color.darker_gray)//进度条颜色colorId
customSeekBar!!.setCacheProgressBarColor(android.R.color.white)//缓存条颜色colorId
customSeekBar!!.setTextBgColor(android.R.color.white)//文字背景颜色colorId
customSeekBar!!.setTextColor(android.R.color.black)//字体颜色colorId
customSeekBar!!.setTextSize(10)//文字大小sp 默认10sp
// 设置进度拖动监听
// 手动拖动进度条会返回当前进度
customSeekBar!!.setProgressListener(object : CustomSeekBar.IProgressListener {
    override fun progress(progress: Int) {
        this@MainActivity.progress = progress
    }
})
// 设置缓存进度
customSeekBar!!.cacheProgress(cacheProgress)
// 设置进度
customSeekBar!!.progress(progress)
```

![avatar](https://raw.githubusercontent.com/hiongyend/CustomSeekBar/master/demo2.png)
