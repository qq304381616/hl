package com.hl.api.thread;

import com.hl.utils.L;

/**
 * Created on 2017/3/24.
 */
public class DownloadTask implements ITaskRunnable {
    public String name;

    public DownloadTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        L.e(name + " 下载开始!");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        L.e(name + " 下载完成!");
    }

    @Override
    public String getId() {
        return name;
    }
}
