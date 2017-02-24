package com.hl;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.hl.systeminfo.contact.ContactsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SystemActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private RecyclerAdapter mAdapter;

    // 屏幕常亮状态
    private boolean isWakeLock;

    protected void initData() {
        mDatas = new ArrayList<String>();

        mDatas.add("闹钟");
        mDatas.add("定时器");
        mDatas.add("显示所有闹钟");
        mDatas.add("日历事件");
        mDatas.add("调用照相");
        mDatas.add("调用照相2");

        mDatas.add("activity");
        mDatas.add("service");

        mDatas.add("应用列表");
        mDatas.add("PageSlidingTab");

        mDatas.add("震动");
        mDatas.add("屏幕常亮");
        
        mDatas.add("通讯录列表");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        initData();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter(this, mDatas));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
                if (position == 0) {
                    createAlarm("", 1, 1);
                } else if (position == 1) {
                    startTimer("", 5);
                } else if (position == 2) {
                    showAlarms();
                } else if (position == 3) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(System.currentTimeMillis());
                    Calendar c1 = Calendar.getInstance();
                    c1.setTimeInMillis(System.currentTimeMillis() - 1000 * 60);
                    addEvent("title", "des", c, c1);
                } else if (position == 4) {
                    capturePhoto();
                } else if (position == 5) {
                    capturePhoto2();
                } else if (position == 6) {
                    Intent intent = new Intent(SystemActivity.this, LaunchModeActivityA.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else if (position == 7) {
                    Intent intent = new Intent(SystemActivity.this, MyService.class);
                    startService(intent);
                } else if (position == 8) {
                    Intent intent = new Intent(SystemActivity.this, AppsInfoListActivity.class);
                    startActivity(intent);
                } else if (position == 9) {
                    Intent intent = new Intent(SystemActivity.this, com.hl.tab.ui.activity.MainActivity.class);
                    startActivity(intent);
                } else if (position == 10) {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
                    vibrator.vibrate(pattern, -1); // 重复次数， -1不重复
                    // vibrator.cancel(); // 取消震动
                } else if (position == 11) {
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
                    if (isWakeLock) {
                        Toast.makeText(SystemActivity.this, "打开屏幕常亮", Toast.LENGTH_SHORT).show();
                        mWakeLock.acquire();
                    }else {
                        Toast.makeText(SystemActivity.this, "关闭屏幕常亮", Toast.LENGTH_SHORT).show();
                        mWakeLock.acquire();
                    }
                } else if (position == 12) {
                    Intent intent = new Intent(SystemActivity.this, ContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showAlarms() {
        Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * 操作 ACTION_INSERT
     * 数据 URI Events.CONTENT_URI
     * MIME 类型 "vnd.android.cursor.dir/event"
     * Extra
     * EXTRA_EVENT_ALL_DAY 一个布尔型值，指定此事件是否为全天事件。
     * EXTRA_EVENT_BEGIN_TIME 事件的开始时间（从新纪年开始计算的毫秒数）。
     * EXTRA_EVENT_END_TIME 事件的结束时间（从新纪年开始计算的毫秒数）。
     * TITLE 事件标题。
     * DESCRIPTION 事件说明。
     * EVENT_LOCATION 事件地点。
     * EXTRA_EMAIL 以逗号分隔的受邀者电子邮件地址列表。
     * 可使用 CalendarContract.EventsColumns 类中定义的常量指定许多其他事件详细信息。
     * <activity ...>
     * <intent-filter>
     * <action android:name="android.intent.action.INSERT" />
     * <data android:mimeType="vnd.android.cursor.dir/event" />
     * <category android:name="android.intent.category.DEFAULT" />
     * </intent-filter>
     * </activity>
     */
    public void addEvent(String title, String location, Calendar begin, Calendar end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
//    static final Uri mLocationForPhotos;

    /**
     * <activity ...>
     * <intent-filter>
     * <action android:name="android.media.action.IMAGE_CAPTURE" />
     * <category android:name="android.intent.category.DEFAULT" />
     * </intent-filter>
     * </activity>
     */
    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * 静态模式拍照
     */
    public void capturePhoto2() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(LOG_TAG, "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");

            // Do other work with full size photo saved in mLocationForPhotos

        }
    }

}