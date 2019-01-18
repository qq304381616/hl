package com.hl.okhttp3.core;


import java.io.IOException;

import okio.Timeout;

public interface Call extends Cloneable {
    Request request();

    /**
     * 发起请求
     */
    Response execute() throws IOException;

    void enqueue(Callback responseCallback);

    void cancel();

    boolean isExecuted();

    boolean isCanceled();

    Timeout timeout();

    Call clone();

    interface Factory {
        Call newCall(Request request);
    }
}
