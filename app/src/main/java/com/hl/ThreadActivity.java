package com.hl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * 线程类
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
                t.interrupt();
            }
        });


    }
}
