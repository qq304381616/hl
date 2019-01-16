package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import okio.ByteString;

public interface WebSocket {
    Request request();

    long queueSize();

    boolean send(String text);

    boolean send(ByteString bytes);

    boolean close(int code, @Nullable String reason);

    void cancel();

    interface Factory {
        WebSocket newWebSocket(Request request, WebSocketListener listener);
    }
}
