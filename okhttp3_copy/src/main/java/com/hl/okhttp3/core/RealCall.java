package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.internal.http.RetryAndFollowUpInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.AsyncTimeout;

final class RealCall implements Call {

    final OkHttpClient client;
    final Request originalRequest;
    final boolean forWebSocket;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    final AsyncTimeout timeout;
    private @Nullable
    EventListener eventListener;

    private RealCall(OkHttpClient client, Request request, boolean forWebSocket) {
        this.client = client;
        this.originalRequest = request;
        this.forWebSocket = forWebSocket;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client);
        this.timeout = new AsyncTimeout() {
            @Override
            protected void timedOut() {
                cancel();
            }
        };
        this.timeout.timeout(client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancel() {
        retryAndFollowUpInterceptor.cancel();
    }

    @Override
    public Response execute() throws IOException {
        // TODO
        return null;
    }

    static RealCall newRealCall(OkHttpClient client, Request request, boolean forWebSocket) {
        RealCall call = new RealCall(client, request, forWebSocket);
        call.eventListener = client.eventListenerFactory().create(call);
        return call;
    }
}
