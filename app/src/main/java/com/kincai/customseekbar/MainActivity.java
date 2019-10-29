package com.kincai.customseekbar;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    int cacheProgress = 0;
    int progress;
    int maxProgress = 60;
    private CustomSeekBar customSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customSeekBar = findViewById(R.id.seekbar);
        customSeekBar.setMaxProgress(maxProgress);
        customSeekBar.setProgressBarHeight(1.0f);
        customSeekBar.setCacheProgressBarHeight(1.5f);
        customSeekBar.setProgressBarColor(android.R.color.holo_green_light);
        customSeekBar.setCacheProgressBarColor(android.R.color.white);
        customSeekBar.setTextBgColor(android.R.color.white);
        customSeekBar.setTextColor(android.R.color.black);
        customSeekBar.setTextSize(10);
        customSeekBar.setProgressListener(new CustomSeekBar.IProgressListener() {
            @Override
            public void progress(int progress) {
                MainActivity.this.progress = progress;
            }
        });
        start();

    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (cacheProgress < maxProgress) {
                            cacheProgress += 4;
                            customSeekBar.cacheProgress(cacheProgress);
                        }
                        customSeekBar.progress(progress);
                        if (progress >= maxProgress) {
                            break;
                        }
                        progress += 1;
                        Log.e("ss", "ss" + progress);

                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
