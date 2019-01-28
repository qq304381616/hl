package com.hl.systeminfo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;

import com.hl.base.BaseActivity;
import com.hl.base.ui.TestActivity;

/**
 * 通知
 * <p>
 * 设置提示
 * setSound(Uri sound)：设定一个铃声，用于在通知的时候响应。传递一个Uri的参数，格式为“file:///mnt/sdcard/Xxx.mp3”。
 * setLights(int argb, int onMs, int offMs)：设定前置LED灯的闪烁速率，持续毫秒数，停顿毫秒数。
 * setVibrate(long[] pattern)：设定震动的模式，以一个long数组保存毫秒级间隔的震动。
 * <p>
 * 一般设置 setDefaults()
 * DEFAULT_ALL：铃声、闪光、震动均系统默认。
 * DEFAULT_SOUND：系统默认铃声。
 * DEFAULT_VIBRATE：系统默认震动。
 * DEFAULT_LIGHTS：系统默认闪光。
 */
public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_notification);
        initToolbar(true);


        findViewById(R.id.tv_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = getManager();
                Notification.Builder builder = getNotification();

                // 设置通知主题的意图 跳转页
                builder.setContentIntent(getNIntent());

                // 大视图  / 1 BigPictureStyle 展示图片， 2 BigTextStyle 展示大文本块 3 InboxStyle 行文本
                builder.setStyle(new Notification.InboxStyle().addLine("line 1").addLine("line 2").addLine("line 3").addLine("line 4")
                        .setBigContentTitle("4 new message").setSummaryText("summary text"));

                builder.setDefaults(Notification.DEFAULT_ALL);
                manager.notify(0, builder.build());
            }
        });

        findViewById(R.id.tv_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NotificationManager manager = getManager();
                final Notification.Builder builder = getNotification();

                //通过一个子线程，动态增加进度条刻度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int incr = 0; incr <= 100; incr += 5) {
                            builder.setProgress(100, incr, false);
                            manager.notify(0, builder.build());
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                        }
                        // 完成
                        builder.setContentText("Download complete")
                                .setProgress(0, 0, false);
                        manager.notify(2, builder.build());
                    }
                }).start();
            }
        });

        findViewById(R.id.tv_while).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NotificationManager manager = getManager();
                final Notification.Builder builder = getNotification();
                builder.setProgress(0, 0, true);//设置为true，表示流动
                manager.notify(0, builder.build());

                //5秒之后还停止流动
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        builder.setProgress(100, 100, false);//设置为true，表示刻度
                        manager.notify(3, builder.build());
                    }
                }).start();
            }
        });

        findViewById(R.id.tv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager manager = getManager();
                Notification.Builder builder = getNotification();
                RemoteViews contentViews = new RemoteViews(getPackageName(), R.layout.base_layout_notification);
                //通过控件的Id设置属性
                contentViews.setImageViewResource(R.id.imageNo, R.mipmap.ic_launcher);
                contentViews.setTextViewText(R.id.titleNo, "自定义通知标题");
                contentViews.setTextViewText(R.id.textNo, "自定义通知内容");

                builder.setContent(contentViews);
                manager.notify(4, builder.build());
            }
        });
    }

    private NotificationManager getManager() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // 兼容8.0
            NotificationChannel mChannel = new NotificationChannel("cId", "cName", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("cDes");
            mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            manager.createNotificationChannel(mChannel);
        }
        return manager;
    }

    private Notification.Builder getNotification() {
        Notification.Builder builder = new Notification.Builder(NotificationActivity.this);
        builder.setSmallIcon(R.mipmap.leak_canary_icon).setContentTitle("notify title").setContentText("notify text");
        builder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
        builder.setNumber(12);
        builder.setAutoCancel(true);//自己维护通知的消失
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 兼容8.0
            builder.setChannelId("cId");
        }
        return builder;
    }

    private PendingIntent getNIntent() {
        return PendingIntent.getActivity(
                NotificationActivity.this, 0, TestActivity.getIntent(NotificationActivity.this, "notification"),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
