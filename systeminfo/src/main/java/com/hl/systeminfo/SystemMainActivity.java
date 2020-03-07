package com.hl.systeminfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hl.base.BaseActivity;
import com.hl.systeminfo.appinfo.AppsInfoListActivity;
import com.hl.systeminfo.camera2.CameraActivity;
import com.hl.systeminfo.contact.ContactsActivity;
import com.hl.systeminfo.notification.NotificationActivity;
import com.hl.utils.L;
import com.hl.utils.PhotoAlbumUtils;
import com.hl.utils.api.eventbus.EventType;
import com.hl.utils.api.eventbus.MyEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;

public class SystemMainActivity extends BaseActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_ALBUM = 2;

    private File cameraSavePath; // 拍照照片路径
    private Uri uri; // 照片uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_activity_main);
        initToolbar(true);

        // 传感器
        findViewById(R.id.tv_sensor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SystemMainActivity.this, SensorMainActivity.class));
            }
        });
        // 闹钟
        findViewById(R.id.tv_create_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarm("", 1, 1);
            }
        });
        //  定时器
        findViewById(R.id.tv_start_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer("", 5);
            }
        });
        // 显示所有闹钟
        findViewById(R.id.tv_show_alarms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarms();
            }
        });
        //日历事件
        findViewById(R.id.tv_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                Calendar c1 = Calendar.getInstance();
                c1.setTimeInMillis(System.currentTimeMillis() - 1000 * 60);
                addEvent("title", "des", c, c1);
            }
        });
        //调用照相
        findViewById(R.id.camera1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSavePath = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //如果是7.0以上，使用FileProvider，否则会报错
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", cameraSavePath);
                } else {
                    uri = Uri.fromFile(cameraSavePath);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //设置拍照后图片保存的位置
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        //camera2 预览和拍照
        findViewById(R.id.camera2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemMainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        //应用列表
        findViewById(R.id.tv_applist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemMainActivity.this, AppsInfoListActivity.class);
                startActivity(intent);
            }
        });
        //震动
        findViewById(R.id.tv_vibrator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
                vibrator.vibrate(pattern, -1); // 重复次数， -1不重复
                // vibrator.cancel(); // 取消震动
            }
        });

        CheckBox cb_wakelock = findViewById(R.id.cb_wakelock);
        cb_wakelock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });

        //通讯录列表
        findViewById(R.id.tv_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemMainActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        // 选择相册
        findViewById(R.id.tv_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_ALBUM);
            }
        });

        // 横屏
        findViewById(R.id.tv_screen_landscape).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        // 竖屏
        findViewById(R.id.tv_screen_portrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
        // 支持旋转
        findViewById(R.id.tv_screen_sensor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        });
        // 震动
        findViewById(R.id.tv_vibrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); //获取系统震动服务
                vib.vibrate(100);
            }
        });
        // 文字转语音
        findViewById(R.id.tv_text_to_speech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(SystemMainActivity.this, TextToSpeechActivity.class)));
            }
        });

        // 短信收发
        findViewById(R.id.tv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(SystemMainActivity.this, MessageActivity.class)));
            }
        });

        // 视频播放
        findViewById(R.id.tv_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(SystemMainActivity.this, VideoViewActivity.class)));
            }
        });

        // 通知
        findViewById(R.id.tv_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(SystemMainActivity.this, NotificationActivity.class)));
            }
        });

        // 存储目录
        findViewById(R.id.tv_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new Intent(SystemMainActivity.this, PathActivity.class)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("SystemMainActivity onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                String photoPath;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    photoPath = String.valueOf(cameraSavePath);
                } else {
                    photoPath = uri.getEncodedPath();
                }
                L.e("拍照返回图片路径:" + photoPath);

                EventBus.getDefault().post(new MyEvent(EventType.TYPE_1000, photoPath));
            }
        } else if (requestCode == REQUEST_IMAGE_ALBUM) {
            if (resultCode == RESULT_OK) {
                String photoPath = PhotoAlbumUtils.getRealPathFromUri(this, data.getData());
                L.e("相册返回图片路径:" + photoPath);

                EventBus.getDefault().post(new MyEvent(EventType.TYPE_1000, photoPath));
            }
        }
    }
}