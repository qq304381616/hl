package com.hl.api.receiver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hl.api.R;
import com.hl.base.BaseActivity;

/**
 * Created on 2017/6/7.
 */

public class ReceiverActivity extends BaseActivity implements MyBroadcastReceiver.EventHandler, MyBroadcastReceiver.Shutdown, MyBroadcastReceiver.BootCompleted, MyBroadcastReceiver.BatteryChanged, MyBroadcastReceiver.PowerConnected, MyBroadcastReceiver.ScreenOff {

    private TextView text;
    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity_receiver);
        receiver = new MyBroadcastReceiver();
        receiver.registerReceiver(this);

        text = findViewById(R.id.text);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unregisterReceiver(this);
    }

    @Override
    public void onNetChange() {
        text.setText(text.getText().toString() + "\n网络状改变");
    }

    @Override
    public void onShutdown() {
        text.setText(text.getText().toString() + "\n关机");
    }

    @Override
    public void onBootCompleted() {
        text.setText(text.getText().toString() + "\n开机");
    }

    @Override
    public void onBatteryChanged() {
        text.setText(text.getText().toString() + "\n充电状态");
    }

    @Override
    public void onPowerConnected() {
        text.setText(text.getText().toString() + "\n插入电源");
    }

    @Override
    public void onScreenOff() {
        text.setText(text.getText().toString() + "\n关闭屏幕");
    }
}
