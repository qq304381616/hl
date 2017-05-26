package com.hl.api.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManagerThread implements Runnable {

    private TaskManager taskManager;
    private ExecutorService pool;   // 创建一个可重用固定线程数的线程池
    private final int POOL_SIZE = 1; // 线程池大小
    private final int SLEEP_TIME = 1000;  // 轮询时间
    private boolean isStop = false;// 是否停止

    public TaskManagerThread() {
        taskManager = TaskManager.getInstance();
        pool = Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public void run() {
        while (!isStop) {
            ITaskRunnable downloadTask = taskManager.getDownloadTask();
            if (downloadTask != null) {
                pool.execute(downloadTask);
            } else {  //如果当前未有downloadTask在任务队列中
                try {
                    // 查询任务完成失败的,重新加载任务队列
                    // 轮询,
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (isStop) pool.shutdown();
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

}