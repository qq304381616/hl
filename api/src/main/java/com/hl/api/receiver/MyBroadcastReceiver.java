package com.hl.api.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 注册网络变化广播 MyBroadcastReceiver.mListeners.add(this);
 * 取消网络变化广播 MyBroadcastReceiver.mListeners.remove(this);
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    public static ArrayList<EventHandler> mListeners = new ArrayList<>();
    public static ArrayList<Shutdown> shutdowns = new ArrayList<>();
    public static ArrayList<BootCompleted> bootCompleteds = new ArrayList<>();
    public static ArrayList<BatteryChanged> batteryChanged = new ArrayList<>();
    public static ArrayList<PowerConnected> powerConnected = new ArrayList<>();
    public static ArrayList<ScreenOff> screenOff = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (mListeners.size() > 0)
                for (EventHandler handler : mListeners) {
                    handler.onNetChange();
                }
        } else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            if (shutdowns.size() > 0)
                for (Shutdown s : shutdowns) {
                    s.onShutdown();
                }
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (bootCompleteds.size() > 0)
                for (BootCompleted b : bootCompleteds) {
                    b.onBootCompleted();
                }
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            if (batteryChanged.size() > 0)
                for (BatteryChanged b : batteryChanged) {
                    b.onBatteryChanged();
                }
        } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            if (powerConnected.size() > 0)
                for (PowerConnected b : powerConnected) {
                    b.onPowerConnected();
                }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (screenOff.size() > 0)
                for (ScreenOff b : screenOff) {
                    b.onScreenOff();
                }
        }
    }

    public interface EventHandler {
        void onNetChange();
    }

    public interface Shutdown {
        void onShutdown();
    }

    public interface BootCompleted {
        void onBootCompleted();
    }

    public interface BatteryChanged {
        void onBatteryChanged();
    }

    //插上外部电源时发出的广播
    public interface PowerConnected {
        void onPowerConnected();
    }

    //关闭屏幕
    public interface ScreenOff {
        void onScreenOff();
    }

    public void registerReceiver(Context c) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        c.registerReceiver(this, filter);

        mListeners.add((EventHandler) c);
        shutdowns.add((Shutdown) c);
        bootCompleteds.add((BootCompleted) c);
        batteryChanged.add((BatteryChanged) c);
        powerConnected.add((PowerConnected) c);
        screenOff.add((ScreenOff) c);
    }

    public void unregisterReceiver(Context c) {
        c.unregisterReceiver(this);

        mListeners.remove(c);
        shutdowns.remove( c);
        bootCompleteds.remove( c);
        batteryChanged.remove( c);
        powerConnected.remove( c);
        screenOff.remove( c);
    }

}
