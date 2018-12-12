package com.hl.api.thread;

import com.hl.utils.L;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created on 2017/3/24.
 */
public class TaskManager {
    // UI请求队列
    private LinkedList<ITaskRunnable> downloadTasks;
    // 任务不能重复
    private Set<String> taskIdSet;

    private static TaskManager taskMananger;

    private TaskManager() {

        downloadTasks = new LinkedList<ITaskRunnable>();
        taskIdSet = new HashSet<String>();

    }

    public static synchronized TaskManager getInstance() {
        if (taskMananger == null) {
            taskMananger = new TaskManager();
        }
        return taskMananger;
    }

    //1.先执行
    public void addDownloadTask(ITaskRunnable downloadTask) {
        synchronized (downloadTasks) {
            if (!isTaskRepeat(downloadTask.getId())) {
                // 增加下载任务
                downloadTasks.addLast(downloadTask);
            }
        }
    }

    public boolean isTaskRepeat(String fileId) {
        synchronized (taskIdSet) {
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
        synchronized (downloadTasks) {
            if (downloadTasks.size() > 0) {
                L.e("下载管理器增加下载任务：" + "取出任务");
                ITaskRunnable downloadTask = downloadTasks.removeFirst();
                return downloadTask;
            }
        }
        return null;
    }
}
