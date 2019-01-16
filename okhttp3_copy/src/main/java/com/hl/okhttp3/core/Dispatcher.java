package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.internal.Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Dispatcher {

    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;

    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();
    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
    private @Nullable
    Runnable idleCallback;
    private @Nullable
    ExecutorService executorService;

    public Dispatcher() {
    }

    synchronized void executed(RealCall call) {
        runningSyncCalls.add(call);
    }

    void finished(RealCall call) {
        finished(runningSyncCalls, call);
    }

    void finished(RealCall.AsyncCall call) {
        finished(runningAsyncCalls, call);
    }

    private <T> void finished(Deque<T> calls, T call) {
        Runnable idleCallback;
        synchronized (this) {
            if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
            idleCallback = this.idleCallback;
        }

        boolean isRunning = promoteAndExecute();

        if (!isRunning && idleCallback != null) {
            idleCallback.run();
        }
    }

    private boolean promoteAndExecute() {
        assert (!Thread.holdsLock(this));

        List<RealCall.AsyncCall> executableCalls = new ArrayList<>();
        boolean isRunning;
        synchronized (this) {
            for (Iterator<RealCall.AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
                RealCall.AsyncCall asyncCall = i.next();

                if (runningAsyncCalls.size() >= maxRequests) break; // Max capacity.
                if (runningCallsForHost(asyncCall) >= maxRequestsPerHost)
                    continue; // Host max capacity.

                i.remove();
                executableCalls.add(asyncCall);
                runningAsyncCalls.add(asyncCall);
            }
            isRunning = runningCallsCount() > 0;
        }

        for (int i = 0, size = executableCalls.size(); i < size; i++) {
            RealCall.AsyncCall asyncCall = executableCalls.get(i);
            asyncCall.executeOn(executorService());
        }

        return isRunning;
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return executorService;
    }

    private int runningCallsForHost(RealCall.AsyncCall call) {
        int result = 0;
        for (RealCall.AsyncCall c : runningAsyncCalls) {
            if (c.get().forWebSocket) continue;
            if (c.host().equals(call.host())) result++;
        }
        return result;
    }

    public synchronized int runningCallsCount() {
        return runningAsyncCalls.size() + runningSyncCalls.size();
    }

    void enqueue(RealCall.AsyncCall call) {
        synchronized (this) {
            readyAsyncCalls.add(call);
        }
        promoteAndExecute();
    }
}
