package com.hl.api.thread;

/**
 * Created by hl on 2017/3/24.
 */

public class DownloadTask implements Runnable {
    public String name;

    public DownloadTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " staaaaaaaaaaaaaaaaaaaaaaart 下载开始!");
//      String name=Thread.currentThread().getName();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + "stoooooooooooooooooooooooop 下载完成!");
    }

    public String getFileId() {
        return name;
    }
}
