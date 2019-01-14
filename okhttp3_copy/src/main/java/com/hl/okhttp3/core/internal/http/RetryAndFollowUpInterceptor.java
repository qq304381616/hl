package com.hl.okhttp3.core.internal.http;

import com.hl.okhttp3.core.Interceptor;
import com.hl.okhttp3.core.OkHttpClient;
import com.hl.okhttp3.core.internal.connection.StreamAllocation;

public final class RetryAndFollowUpInterceptor implements Interceptor {

    private final OkHttpClient client;
    private volatile boolean canceled;
    private volatile StreamAllocation streamAllocation;

    public RetryAndFollowUpInterceptor(OkHttpClient client) {
        this.client = client;
    }

    public void cancel() {
        canceled = true;
        StreamAllocation streamAllocatio = this.streamAllocation;
        if(streamAllocatio != null) streamAllocatio.cancel();
    }
}
