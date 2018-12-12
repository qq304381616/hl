package com.hl.api.thread;

import java.util.Random;

/**
 * Created on 2017/5/25.
 */
public class TaskUtils {

    private static TaskUtils instance;

    public TaskUtils() {
        //1.new一个线程管理队列
        TaskManager.getInstance();
        //2.new一个线程池，并启动
        TaskManagerThread downloadTaskManagerThread = new TaskManagerThread();
        new Thread(downloadTaskManagerThread).start();
    }

    public static synchronized TaskUtils getInstance() {
        if (instance == null) {
            instance = new TaskUtils();
        }
        return instance;
    }

    public void start() {
        Random r = new Random();
        for (int i = 0; i < 5; i++) { // 增加5个下载到队列。注意名称重复的不会重新下载
            int id = r.nextInt(100);
            add("下载" + id);
        }
    }

    public void add(String task) {
        TaskManager.getInstance().addDownloadTask(new DownloadTask(task));
    }
}
