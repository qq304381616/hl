package com.hl.api.thread;

import android.util.Log;

/**
 * Created on 2017/3/24.
 */
public class DownloadTask implements ITaskRunnable {

    private static final String TAG = "DownloadTask";
    public String name;

    public DownloadTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        Log.e(TAG, name + "stoooooooooooooooooooooooop 下载开始!");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, name + "stoooooooooooooooooooooooop 下载完成!");
    }

    @Override
    public String getId() {
        return name;
    }
}
