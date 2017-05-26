package com.hl.api.thread;

/**
 * Created on 2017/5/25.
 */
public class TaskUtils {

    private static final String TAG = TaskUtils.class.getSimpleName();

    private static TaskUtils instance;

    public static synchronized TaskUtils getInstance() {
        if (instance == null) {
            instance = new TaskUtils();
        }
        return instance;
    }

    public TaskUtils() {
        //1.new一个线程管理队列
        TaskManager.getInstance();
        //2.new一个线程池，并启动
        TaskManagerThread downloadTaskManagerThread = new TaskManagerThread();
        new Thread(downloadTaskManagerThread).start();
    }

    // 检查未上传。
    public void start() {
        //3.请求下载
        String[] items = new String[]{"下载1", "下载2", "下载3", "下载4", "下载5"};

        for (int i = 0; i < items.length; i++) {
            add(items[i]);
        }
    }

    public void add(String task) {
        TaskManager.getInstance().addDownloadTask(new DownloadTask(task));
    }
}
