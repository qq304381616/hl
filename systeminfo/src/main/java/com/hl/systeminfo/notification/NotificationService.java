package com.hl.systeminfo.notification;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hl.base.ui.TestActivity;
import com.hl.utils.L;

import static com.hl.systeminfo.notification.Notifications.ACTION_ANSWER;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_HEADS_UP_VIEW;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_CANCEL;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_LOVE;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_LYRICS;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_NEXT;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE;
import static com.hl.systeminfo.notification.Notifications.ACTION_CUSTOM_VIEW_OPTIONS_PRE;
import static com.hl.systeminfo.notification.Notifications.ACTION_MEDIA_STYLE;
import static com.hl.systeminfo.notification.Notifications.ACTION_NO;
import static com.hl.systeminfo.notification.Notifications.ACTION_REJECT;
import static com.hl.systeminfo.notification.Notifications.ACTION_REPLY;
import static com.hl.systeminfo.notification.Notifications.ACTION_SIMPLE;
import static com.hl.systeminfo.notification.Notifications.ACTION_YES;
import static com.hl.systeminfo.notification.Notifications.EXTRA_OPTIONS;
import static com.hl.systeminfo.notification.Notifications.MEDIA_STYLE_ACTION_DELETE;
import static com.hl.systeminfo.notification.Notifications.MEDIA_STYLE_ACTION_NEXT;
import static com.hl.systeminfo.notification.Notifications.MEDIA_STYLE_ACTION_PAUSE;
import static com.hl.systeminfo.notification.Notifications.MEDIA_STYLE_ACTION_PLAY;
import static com.hl.systeminfo.notification.Notifications.NOTIFICATION_ACTION;
import static com.hl.systeminfo.notification.Notifications.NOTIFICATION_CUSTOM;
import static com.hl.systeminfo.notification.Notifications.NOTIFICATION_CUSTOM_HEADS_UP;
import static com.hl.systeminfo.notification.Notifications.NOTIFICATION_MEDIA_STYLE;
import static com.hl.systeminfo.notification.Notifications.NOTIFICATION_REMOTE_INPUT;
import static com.hl.systeminfo.notification.Notifications.REMOTE_INPUT_RESULT_KEY;

/**
 *
 */
public class NotificationService extends Service {

    public final static String ACTION_SEND_PROGRESS_NOTIFICATION = "com.android.notification.ACTION_SEND_PROGRESS_NOTIFICATION";

    private Context mContext;
    private NotificationManager mNM;
    private boolean mIsLoved;
    private boolean mIsPlaying = true;

    public static Intent getIntent(Context c, String action) {
        Intent intent = new Intent(c, NotificationService.class);
        intent.setAction(action);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            L.e("onStartCommand action = " + intent.getAction());
            switch (intent.getAction()) {
                case ACTION_SIMPLE:
                    startTestActivity(ACTION_SIMPLE);
                    break;
                case ACTION_MEDIA_STYLE:
                    dealWithActionMediaStyle(intent);
                    break;
                case ACTION_YES:
                    startTestActivity(ACTION_YES);
                    mNM.cancel(NOTIFICATION_ACTION);
                    break;
                case ACTION_NO:
                    startTestActivity(ACTION_NO);
                    mNM.cancel(NOTIFICATION_ACTION);
                    break;
                case ACTION_REPLY:
                    dealWithActionReplay(intent);
                    break;
                case ACTION_SEND_PROGRESS_NOTIFICATION:
                    dealWithActionSendProgressNotification();
                    break;
                case ACTION_CUSTOM_HEADS_UP_VIEW:
                    dealWithActionCustomHeadsUpView(intent);
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_LOVE:
                    Notifications.getInstance().sendCustomViewNotification(this, mNM, !mIsLoved, mIsPlaying);
                    mIsLoved = !mIsLoved;
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_PRE:
                    Notifications.getInstance().sendCustomViewNotification(this, mNM, mIsLoved, mIsPlaying);
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE:
                    Notifications.getInstance().sendCustomViewNotification(this, mNM, mIsLoved, !mIsPlaying);
                    mIsPlaying = !mIsPlaying;
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_NEXT:
                    Notifications.getInstance().sendCustomViewNotification(this, mNM, mIsLoved, mIsPlaying);
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_LYRICS:
                    break;
                case ACTION_CUSTOM_VIEW_OPTIONS_CANCEL:
                    mNM.cancel(NOTIFICATION_CUSTOM);
                    break;
                default:
                    //do nothing
            }
        }
        return START_STICKY;
    }

    private void startTestActivity(String message) {
        Intent testIntent = TestActivity.getIntent(mContext, message);
        testIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(testIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void dealWithActionMediaStyle(Intent intent) {
        String option = intent.getStringExtra(EXTRA_OPTIONS);
        L.e("media option = " + option);
        if (option == null) {
            return;
        }
        switch (option) {
            case MEDIA_STYLE_ACTION_PAUSE:
                Notifications.getInstance().sendMediaStyleNotification(this, mNM, false);
                break;
            case MEDIA_STYLE_ACTION_PLAY:
                Notifications.getInstance().sendMediaStyleNotification(this, mNM, true);
                break;
            case MEDIA_STYLE_ACTION_NEXT:
                break;
            case MEDIA_STYLE_ACTION_DELETE:
                mNM.cancel(NOTIFICATION_MEDIA_STYLE);
                break;
            default:
                //do nothing
        }
    }

    private void dealWithActionReplay(Intent intent) {
        Bundle result = RemoteInput.getResultsFromIntent(intent);
        if (result != null) {
            String content = result.getString(REMOTE_INPUT_RESULT_KEY);
            L.e("content = " + content);
            mNM.cancel(NOTIFICATION_REMOTE_INPUT);
        }
    }

    private void dealWithActionSendProgressNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    Notifications.getInstance().sendProgressViewNotification(mContext, mNM, i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void dealWithActionCustomHeadsUpView(Intent intent) {
        String headsUpOption = intent.getStringExtra(EXTRA_OPTIONS);
        L.e("heads up option = " + headsUpOption);
        if (headsUpOption == null) {
            return;
        }
        switch (headsUpOption) {
            case ACTION_ANSWER:
                mNM.cancel(NOTIFICATION_CUSTOM_HEADS_UP);
                break;
            case ACTION_REJECT:
                mNM.cancel(NOTIFICATION_CUSTOM_HEADS_UP);
                break;
            default:
                //do nothing
        }
    }
}