package com.hl.systeminfo.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.widget.RemoteViews;

import com.hl.base.ui.TestActivity;
import com.hl.systeminfo.R;

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
public class Notifications {
    public final static int NOTIFICATION_SAMPLE = 0;
    public final static int NOTIFICATION_ACTION = 1;
    public final static int NOTIFICATION_REMOTE_INPUT = 2;
    public final static int NOTIFICATION_BIG_PICTURE_STYLE = 3;
    public final static int NOTIFICATION_BIG_TEXT_STYLE = 4;
    public final static int NOTIFICATION_INBOX_STYLE = 5;
    public final static int NOTIFICATION_MEDIA_STYLE = 6;
    public final static int NOTIFICATION_MESSAGING_STYLE = 7;
    public final static int NOTIFICATION_PROGRESS = 8;
    public final static int NOTIFICATION_CUSTOM_HEADS_UP = 9;
    public final static int NOTIFICATION_CUSTOM = 10;

    public final static String ACTION_SIMPLE = "com.android.notification.ACTION_SIMPLE";
    public final static String ACTION_MEDIA_STYLE = "com.android.notification.ACTION_MEDIA_STYLE";
    public final static String ACTION_CUSTOM_HEADS_UP_VIEW = "com.android.notification.ACTION_CUSTOM_HEADS_UP_VIEW";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_LOVE = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_LOVE";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_PRE = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_PRE";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_NEXT = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_NEXT";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_LYRICS = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_LYRICS";
    public final static String ACTION_CUSTOM_VIEW_OPTIONS_CANCEL = "com.android.notification.ACTION_CUSTOM_VIEW_OPTIONS_CANCEL";

    public final static String ACTION_YES = "com.android.notification.ACTION_YES";
    public final static String ACTION_NO = "com.android.notification.ACTION_NO";
    public final static String ACTION_REPLY = "com.android.notification.ACTION_REPLY";
    public final static String REMOTE_INPUT_RESULT_KEY = "remote_input_content";

    public final static String EXTRA_OPTIONS = "options";
    public final static String MEDIA_STYLE_ACTION_DELETE = "action_delete";
    public final static String MEDIA_STYLE_ACTION_PLAY = "action_play";
    public final static String MEDIA_STYLE_ACTION_PAUSE = "action_pause";
    public final static String MEDIA_STYLE_ACTION_NEXT = "action_next";
    public final static String ACTION_ANSWER = "action_answer";
    public final static String ACTION_REJECT = "action_reject";

    private static volatile Notifications sInstance = null;

    private Notifications() {
    }

    public static Notifications getInstance() {
        if (sInstance == null) {
            synchronized (Notifications.class) {
                if (sInstance == null) {
                    sInstance = new Notifications();
                }
            }
        }
        return sInstance;
    }

    private Notification.Builder getNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.leak_canary_icon); // 左侧小图标
        builder.setContentTitle("notify title"); // 标题
        builder.setContentText("notify text"); // 内容
        builder.setAutoCancel(true); // 设置点击后删除通知
        builder.setShowWhen(true); // 设置显示时间
        builder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));// 设置右侧大图标
        builder.setContentIntent(getNIntent(context, ACTION_SIMPLE)); // 设置点击跳转意图
        builder.setDeleteIntent(getNIntent(context, ACTION_SIMPLE)); // 设置通知删除意图
//        builder.setOngoing(true); //设置通知不可删除
        builder.setDefaults(Notification.DEFAULT_ALL); // 设置提醒 方式 跟随系统

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 兼容8.0
            builder.setChannelId("cId");
        }
        return builder;
    }

    private PendingIntent getNIntent(Context context, String action) {
        return PendingIntent.getService(context, 0, NotificationService.getIntent(context, action), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void sendSimpleNotification(Context context, NotificationManager nm) {
        Notification.Builder nb = getNotification(context);
        nm.notify(NOTIFICATION_SAMPLE, nb.build());
    }

    public void sendActionNotification(Context context, NotificationManager nm) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);
        Notification.Action yesActionBuilder = new Notification.Action.Builder(
                Icon.createWithResource("", R.mipmap.ic_launcher),
                "YES",
                getNIntent(context, ACTION_YES))
                .build();
        //创建点击通知 NO 按钮时发送的广播
        Notification.Action noActionBuilder = new Notification.Action.Builder(
                Icon.createWithResource("", R.mipmap.ic_launcher),
                "NO",
                getNIntent(context, ACTION_NO))
                .build();
        //为通知添加按钮
        nb.setActions(yesActionBuilder, noActionBuilder);
        nm.notify(NOTIFICATION_ACTION, nb.build());
    }

    public void sendRemoteInputNotification(Context context, NotificationManager nm) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);

        //创建带输入框的按钮
        RemoteInput remoteInput = new RemoteInput.Builder(REMOTE_INPUT_RESULT_KEY).setLabel("Reply").build();
        Notification.Action replyAction = new Notification.Action.Builder(
                Icon.createWithResource("", R.mipmap.ic_launcher),
                "Reply",
                getNIntent(context, ACTION_REPLY))
                .addRemoteInput(remoteInput)
                .build();
        //为通知添加按钮
        nb.setActions(replyAction);
        nm.notify(NOTIFICATION_REMOTE_INPUT, nb.build());
    }

    public void sendBigPictureStyleNotification(Context context, NotificationManager nm) {
        Notification.Builder nb = getNotification(context);
        //创建大视图样式
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .setBigContentTitle("title")
                .setSummaryText("text")
                .bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.check_false));

        nb.setStyle(bigPictureStyle);

        nm.notify(NOTIFICATION_BIG_PICTURE_STYLE, nb.build());
    }

    public void sendBigTextStyleNotification(Context context, NotificationManager nm) {
        Notification.Builder nb = getNotification(context);
        //创建大文字样式
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle()
                .setBigContentTitle("title")
                .setSummaryText("text")
                .bigText("line1    \n" +
                        "line2   \n" +
                        "line3   \n" +
                        "line balabala");

        nb.setStyle(bigTextStyle);

        nm.notify(NOTIFICATION_BIG_TEXT_STYLE, nb.build());
    }

    public void sendInboxStyleNotification(Context context, NotificationManager nm) {
        Notification.Builder nb = getNotification(context);

        //创建信箱样式
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .setBigContentTitle("title")
                .setSummaryText("text")
                //最多六行
                .addLine("1. I am email content.")
                .addLine("2. I am email content.")
                .addLine("3. I am email content.")
                .addLine("4. I am email content.")
                .addLine("5. I am email content.")
                .addLine("6. I am email content.");

        nb.setStyle(inboxStyle);
        //发送通知
        nm.notify(NOTIFICATION_INBOX_STYLE, nb.build());
    }

    public void sendMediaStyleNotification(Context context, NotificationManager nm, boolean isPlaying) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);

        //创建Action按钮
        Intent playOrPauseIntent = new Intent(context, NotificationService.class);
        playOrPauseIntent.setAction(ACTION_MEDIA_STYLE);
        playOrPauseIntent.putExtra(EXTRA_OPTIONS, isPlaying ? MEDIA_STYLE_ACTION_PAUSE : MEDIA_STYLE_ACTION_PLAY);
        PendingIntent playOrPausePendingIntent = PendingIntent.getService(context, 0, playOrPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action playOrPauseAction = new Notification.Action.Builder(
                Icon.createWithResource(context, isPlaying ? R.mipmap.ic_launcher : R.mipmap.ic_launcher),
                isPlaying ? "PAUSE" : "PLAY",
                playOrPausePendingIntent)
                .build();
        Intent nextIntent = new Intent(context, NotificationService.class);
        nextIntent.setAction(ACTION_MEDIA_STYLE);
        nextIntent.putExtra(EXTRA_OPTIONS, MEDIA_STYLE_ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(context, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action nextAction = new Notification.Action.Builder(
                Icon.createWithResource(context, R.mipmap.ic_launcher),
                "Next",
                nextPendingIntent)
                .build();
        Intent deleteIntent = new Intent(context, NotificationService.class);
        deleteIntent.setAction(ACTION_MEDIA_STYLE);
        deleteIntent.putExtra(EXTRA_OPTIONS, MEDIA_STYLE_ACTION_DELETE);
        PendingIntent deletePendingIntent = PendingIntent.getService(context, 2, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action deleteAction = new Notification.Action.Builder(
                Icon.createWithResource(context, R.mipmap.ic_launcher),
                "Delete",
                deletePendingIntent)
                .build();
        //创建媒体样式
        Notification.MediaStyle mediaStyle = new Notification.MediaStyle()
                //最多三个Action
                .setShowActionsInCompactView(0, 1, 2);

        nb.setActions(playOrPauseAction, nextAction, deleteAction)
                //设置信箱样式通知
                .setStyle(mediaStyle);

        //发送通知
        nm.notify(NOTIFICATION_MEDIA_STYLE, nb.build());
    }

    public void sendMessagingStyleNotification(Context context, NotificationManager nm) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);
        //创建信息样式
        Notification.MessagingStyle messagingStyle = new Notification.MessagingStyle("hl")
                .setConversationTitle("title")
                .addMessage("This is a message for you", System.currentTimeMillis(), "hl");

        nb.setStyle(messagingStyle);
        nm.notify(NOTIFICATION_MESSAGING_STYLE, nb.build());
    }

    public void sendProgressViewNotification(Context context, NotificationManager nm, int progress) {
        Notification.Builder nb = getNotification(context);
        nb.setProgress(100, progress, false);
        nm.notify(NOTIFICATION_PROGRESS, nb.build());
    }

    public void sendCustomHeadsUpViewNotification(Context context, NotificationManager nm) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);

        //创建点击通知时发送的广播
        Intent intent = new Intent(context, TestActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        //创建自定义顶部提醒视图
        Intent answerIntent = new Intent(context, NotificationService.class);
        answerIntent.setAction(ACTION_CUSTOM_HEADS_UP_VIEW);
        answerIntent.putExtra(EXTRA_OPTIONS, ACTION_ANSWER);
        PendingIntent answerPendingIntent = PendingIntent.getService(context, 0, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent rejectIntent = new Intent(context, NotificationService.class);
        rejectIntent.setAction(ACTION_CUSTOM_HEADS_UP_VIEW);
        rejectIntent.putExtra(EXTRA_OPTIONS, ACTION_REJECT);
        PendingIntent rejectPendingIntent = PendingIntent.getService(context, 1, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews headsUpView = new RemoteViews(context.getPackageName(), R.layout.system_layout_heads_notify);
        headsUpView.setOnClickPendingIntent(R.id.iv_answer, answerPendingIntent);
        headsUpView.setOnClickPendingIntent(R.id.iv_reject, rejectPendingIntent);

        nb.setContentIntent(pi)
                //设置全屏响应事件;
                .setFullScreenIntent(pi, true)
                //设置自定义顶部提醒视图
                .setCustomHeadsUpContentView(headsUpView);
        //发送通知
        nm.notify(NOTIFICATION_CUSTOM_HEADS_UP, nb.build());
    }

    public void sendCustomViewNotification(Context context, NotificationManager nm, Boolean isLoved, Boolean isPlaying) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Notification.Builder nb = getNotification(context);
        //创建点击通知时发送的广播
        Intent intent = new Intent(context, TestActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        //创建各个按钮的点击响应广播
        Intent intentLove = new Intent(context, NotificationService.class);
        intentLove.setAction(ACTION_CUSTOM_VIEW_OPTIONS_LOVE);
        PendingIntent piLove = PendingIntent.getService(context, 0, intentLove, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPre = new Intent(context, NotificationService.class);
        intentPre.setAction(ACTION_CUSTOM_VIEW_OPTIONS_PRE);
        PendingIntent piPre = PendingIntent.getService(context, 0, intentPre, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlayOrPause = new Intent(context, NotificationService.class);
        intentPlayOrPause.setAction(ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE);
        PendingIntent piPlayOrPause = PendingIntent.getService(context, 0, intentPlayOrPause, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(context, NotificationService.class);
        intentNext.setAction(ACTION_CUSTOM_VIEW_OPTIONS_NEXT);
        PendingIntent piNext = PendingIntent.getService(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentLyrics = new Intent(context, NotificationService.class);
        intentLyrics.setAction(ACTION_CUSTOM_VIEW_OPTIONS_LYRICS);
        PendingIntent piLyrics = PendingIntent.getService(context, 0, intentLyrics, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentCancel = new Intent(context, NotificationService.class);
        intentCancel.setAction(ACTION_CUSTOM_VIEW_OPTIONS_CANCEL);
        PendingIntent piCancel = PendingIntent.getService(context, 0, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建自定义小视图
        RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.system_layout_notify);
        customView.setImageViewBitmap(R.id.iv_play_or_pause, BitmapFactory.decodeResource(context.getResources(),
                isPlaying ? R.mipmap.ic_launcher : R.mipmap.ic_launcher));
        customView.setOnClickPendingIntent(R.id.iv_play_or_pause, piPlayOrPause);
        customView.setOnClickPendingIntent(R.id.iv_next, piNext);
        customView.setOnClickPendingIntent(R.id.iv_lyrics, piLyrics);
        customView.setOnClickPendingIntent(R.id.iv_cancel, piCancel);
        //创建自定义大视图
        RemoteViews customBigView = new RemoteViews(context.getPackageName(), R.layout.system_layout_big_notify);
        customBigView.setImageViewBitmap(R.id.iv_love_big, BitmapFactory.decodeResource(context.getResources(),
                isLoved ? R.mipmap.ic_launcher : R.mipmap.ic_launcher));
        customBigView.setImageViewBitmap(R.id.iv_play_or_pause_big, BitmapFactory.decodeResource(context.getResources(),
                isPlaying ? R.mipmap.ic_launcher : R.mipmap.ic_launcher));
        customBigView.setOnClickPendingIntent(R.id.iv_love_big, piLove);
        customBigView.setOnClickPendingIntent(R.id.iv_pre_big, piPre);
        customBigView.setOnClickPendingIntent(R.id.iv_play_or_pause_big, piPlayOrPause);
        customBigView.setOnClickPendingIntent(R.id.iv_next_big, piNext);
        customBigView.setOnClickPendingIntent(R.id.iv_lyrics_big, piLyrics);
        customBigView.setOnClickPendingIntent(R.id.iv_cancel_big, piCancel);

        //设置通知不可删除
        nb.setOngoing(true)
                //设置自定义小视图
                .setCustomContentView(customView)
                //设置自定义大视图
                .setCustomBigContentView(customBigView);
        //发送通知
        nm.notify(NOTIFICATION_CUSTOM, nb.build());
    }

    public void clearAllNotification(NotificationManager nm) {
        nm.cancelAll();
    }
}