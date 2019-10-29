# CustomSeekBar
防喜马拉雅6.6.21.3播放进度条

## 使用
```xml
<!--布局文件引入-->
<com.kincai.customseekbar.CustomSeekBar
    android:id="@+id/seekbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

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
//设置进度拖动监听
customSeekBar.setProgressListener(new CustomSeekBar.IProgressListener() {
    @Override
    public void progress(int progress) {
        MainActivity.this.progress = progress;
    }
});
```
