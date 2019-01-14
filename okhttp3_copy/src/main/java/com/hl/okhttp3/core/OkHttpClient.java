package com.hl.okhttp3.core;

import com.hl.tab.ui.fragment.FragmentFactory;

import java.util.Random;

public class OkHttpClient implements Cloneable, Call.Factory, WebSocket.Factory{

    final EventListener.Factory eventListenerFactory;
    final int callTimeout;

    public OkHttpClient() {
        this(new Builder());
    }

    OkHttpClient(Builder builder) {
        this.eventListenerFactory = builder.eventListenerFactory;
        this.callTimeout = builder.callTimeout;
    }

    @Override
    public Call newCall(Request request) {
        return RealCall.newRealCall(this, request, false);
    }

    @Override
    public WebSocket newWebSocket(Request request, WebSocketListener listener) {
        // TODO
//        RealWebSocket webSocket = new RealWebSocket(request, listener, new Random(), pingInterval);
//        webSocket.connect(this);
//        return webSocket;
        return null;
    }

    public long callTimeoutMillis() {
        return callTimeout;
    }

    public EventListener.Factory eventListenerFactory() {
        return eventListenerFactory;
    }

    public static final class Builder {
        // TODO
        EventListener.Factory eventListenerFactory;
        int callTimeout;
    }
}
