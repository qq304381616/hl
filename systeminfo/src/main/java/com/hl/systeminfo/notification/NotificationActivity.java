package com.hl.systeminfo.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hl.base.BaseActivity;
import com.hl.systeminfo.R;

/**
 * 通知
 */
public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_notification);
        initToolbar(true);

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        findViewById(R.id.btn_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendSimpleNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendActionNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_remote_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendRemoteInputNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_big_picture_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendBigPictureStyleNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_big_text_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendBigTextStyleNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_inbox_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendInboxStyleNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_media_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendMediaStyleNotification(NotificationActivity.this, nm, false);
            }
        });
        findViewById(R.id.btn_messaging_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendMessagingStyleNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, NotificationService.class);
                intent.setAction(NotificationService.ACTION_SEND_PROGRESS_NOTIFICATION);
                startService(intent);
            }
        });
        findViewById(R.id.btn_custom_heads_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendCustomHeadsUpViewNotification(NotificationActivity.this, nm);
            }
        });
        findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().sendCustomViewNotification(NotificationActivity.this, nm, false, true);
            }
        });
        findViewById(R.id.btn_clear_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notifications.getInstance().clearAllNotification(nm);
            }
        });
    }
}
