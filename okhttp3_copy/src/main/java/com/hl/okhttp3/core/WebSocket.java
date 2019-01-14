package com.hl.okhttp3.core;

public interface WebSocket {

    interface Factory{
        WebSocket newWebSocket(Request request, WebSocketListener listener);
    }
}
