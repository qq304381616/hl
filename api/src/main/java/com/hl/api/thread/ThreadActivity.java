package com.hl.api.thread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hl.api.R;

/**
 * Created by hl on 2017/3/24.
 */

public class ThreadActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ThreadActivity";

    private Thread t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        View tv_start = findViewById(R.id.tv_start);
        View tv_interrupt = findViewById(R.id.tv_interrupt);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            long l = System.currentTimeMillis();
                            long sum = 0;
                            int i = 0;
                            for (; i < 10; i++) {
                                Log.e(LOG_TAG, "isInterrupted：" + t.isInterrupted());

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Log.e(LOG_TAG, "Exception getMessage：" + e.toString());
                                }
                                sum += i;
                            }
                            Log.e(LOG_TAG, "sum：" + sum);
                            Log.e(LOG_TAG, "i：" + i);
                            Log.e(LOG_TAG, "运行时间：" + (System.currentTimeMillis() - l));
                        }
                    };
                    t = new Thread(r);
                    t.start();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception getMessage：" + e.getMessage());
                }
            }
        });

        tv_interrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t != null)
                    t.interrupt();
            }
        });

        // ---------------------------------------------------------------------------

        findViewById(R.id.tv_thread_pool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.new一个线程管理队列
                DownloadTaskManager.getInstance();
                //2.new一个线程池，并启动
                DownloadTaskManagerThread downloadTaskManagerThread = new DownloadTaskManagerThread();
                new Thread(downloadTaskManagerThread).start();

                //3.请求下载
                String[] items = new String[]{"下载1", "下载2", "下载3", "下载4", "下载5"};

                for (int i = 0; i < items.length; i++) {
                    DownloadTaskManager downloadTaskMananger = DownloadTaskManager.getInstance();
                    downloadTaskMananger.addDownloadTask(new DownloadTask(items[i]));
                }
            }
        });

    }

}
