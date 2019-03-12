package com.hl.api.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.hl.api.R;
import com.hl.base.BaseActivity;
import com.hl.utils.L;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created on 2017/3/24.
 */
public class ThreadActivity extends BaseActivity {

    private Thread t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        View tv_start = findViewById(R.id.tv_start);
        View tv_interrupt = findViewById(R.id.tv_interrupt);

        findViewById(R.id.tv_main_thread_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "Handler Looper方式主线程运行 " + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.tv_main_thread_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplication(), "runOnUiThread 方式主线程运行 " + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        long sum = 0;
                        int i = 0;
                        for (; i < 10; i++) {
                            L.e("isInterrupted：" + t.isInterrupted());

                            try {
                                Thread.sleep(1000); // 当执行 t.interrupt();。 会进入catch语句 结束本线程
                            } catch (InterruptedException e) {
                                L.e("Exception getMessage：" + e.toString());
                            }
                            sum += i;
                        }
                        L.e("sum：" + sum);
                        L.e("i：" + i);
                        L.e("运行时间：" + (System.currentTimeMillis() - l));
                    }
                };
                t = new Thread(r);
                t.start();
            }
        });

        tv_interrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t != null)
                    t.interrupt();
            }
        });

        // 线程池
        findViewById(R.id.tv_thread_pool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskUtils.getInstance().start();
            }
        });

        findViewById(R.id.tv_thread_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread();
            }
        });
    }

    private void thread() {
        MyRunnable a = new MyRunnable();
        new Thread(a).start();
        MyRunnable b = new MyRunnable();
        new Thread(b).start();
    }

    private Lock lock = new ReentrantLock();

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            lock.lock();
//            synchronized (ThreadActivity.this) {
            int i = 0;
            while (i < 10) {
                L.e(Thread.currentThread().getName() + " : " + i++);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
//            }
        }
    }
}
