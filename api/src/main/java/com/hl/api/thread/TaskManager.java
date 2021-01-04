package com.hl.api.thread;

import com.hl.utils.L;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created on 2017/3/24.
 */
public class TaskManager {

    private static TaskManager taskManager;
    // UI请求队列
    private LinkedList<ITaskRunnable> downloadTasks;
    // 任务不能重复
    private Set<String> taskIdSet;

    private TaskManager() {
        downloadTasks = new LinkedList<>();
        taskIdSet = new HashSet<>();
    }

    public static synchronized TaskManager getInstance() {
        if (taskManager == null) {
            taskManager = new TaskManager();
        }
        return taskManager;
    }

    //1.先执行
    public void addDownloadTask(ITaskRunnable downloadTask) {
        synchronized (this) {
            if (!isTaskRepeat(downloadTask.getId())) {
                // 增加下载任务
                downloadTasks.addLast(downloadTask);
            }
        }
    }

    public boolean isTaskRepeat(String fileId) {
        synchronized (this) {
            if (taskIdSet.contains(fileId)) {
                return true;
            } else {
                L.e("下载管理器增加下载任务：" + fileId);
                taskIdSet.add(fileId);
                return false;
            }
        }
    }

    public ITaskRunnable getDownloadTask() {
        synchronized (this) {
            if (downloadTasks.size() > 0) {
                L.e("下载管理器增加下载任务：" + "取出任务");
                return downloadTasks.removeFirst();
            }
        }
        return null;
    }
}
