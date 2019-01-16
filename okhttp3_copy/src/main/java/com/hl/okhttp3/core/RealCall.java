package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import com.hl.okhttp3.core.internal.NamedRunnable;
import com.hl.okhttp3.core.internal.cache.CacheInterceptor;
import com.hl.okhttp3.core.internal.connection.ConnectInterceptor;
import com.hl.okhttp3.core.internal.connection.StreamAllocation;
import com.hl.okhttp3.core.internal.http.BridgeInterceptor;
import com.hl.okhttp3.core.internal.http.CallServerInterceptor;
import com.hl.okhttp3.core.internal.http.RealInterceptorChain;
import com.hl.okhttp3.core.internal.http.RetryAndFollowUpInterceptor;
import com.hl.okhttp3.core.internal.platform.Platform;
import okio.AsyncTimeout;
import okio.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static com.hl.okhttp3.core.internal.platform.Platform.INFO;

final class RealCall implements Call {
    final OkHttpClient client;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    final AsyncTimeout timeout;

    /**
     * There is a cycle between the {@link Call} and {@link EventListener} that makes this awkward.
     * This will be set after we create the call instance then create the event listener instance.
     */
    private @Nullable
    EventListener eventListener;

    /** The application's original request unadulterated by redirects or auth headers. */
    final Request originalRequest;
    final boolean forWebSocket;

    // Guarded by this.
    private boolean executed;

    private RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client);
        this.timeout = new AsyncTimeout() {
            @Override protected void timedOut() {
                cancel();
            }
        };
        this.timeout.timeout(client.callTimeoutMillis(), MILLISECONDS);
    }

    static RealCall newRealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        // Safely publish the Call instance to the EventListener.
        RealCall call = new RealCall(client, originalRequest, forWebSocket);
        call.eventListener = client.eventListenerFactory().create(call);
        return call;
    }

    @Override public Request request() {
        return originalRequest;
    }

    @Override public Response execute() throws IOException {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        captureCallStackTrace();
        timeout.enter();
        eventListener.callStart(this);
        try {
            client.dispatcher().executed(this);
            Response result = getResponseWithInterceptorChain();
            if (result == null) throw new IOException("Canceled");
            return result;
        } catch (IOException e) {
            e = timeoutExit(e);
            eventListener.callFailed(this, e);
            throw e;
        } finally {
            client.dispatcher().finished(this);
        }
    }

    @Nullable IOException timeoutExit(@Nullable IOException cause) {
        if (!timeout.exit()) return cause;

        InterruptedIOException e = new InterruptedIOException("timeout");
        if (cause != null) {
            e.initCause(cause);
        }
        return e;
    }

    private void captureCallStackTrace() {
        Object callStackTrace = Platform.get().getStackTraceForCloseable("response.body().close()");
        retryAndFollowUpInterceptor.setCallStackTrace(callStackTrace);
    }

    @Override public void enqueue(Callback responseCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        captureCallStackTrace();
        eventListener.callStart(this);
        client.dispatcher().enqueue(new AsyncCall(responseCallback));
    }

    @Override public void cancel() {
        retryAndFollowUpInterceptor.cancel();
    }

    @Override public Timeout timeout() {
        return timeout;
    }

    @Override public synchronized boolean isExecuted() {
        return executed;
    }

    @Override public boolean isCanceled() {
        return retryAndFollowUpInterceptor.isCanceled();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") // We are a final type & this saves clearing state.
    @Override public RealCall clone() {
        return RealCall.newRealCall(client, originalRequest, forWebSocket);
    }

    StreamAllocation streamAllocation() {
        return retryAndFollowUpInterceptor.streamAllocation();
    }

    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;

        AsyncCall(Callback responseCallback) {
            super("OkHttp %s", redactedUrl());
            this.responseCallback = responseCallback;
        }

        String host() {
            return originalRequest.url().host();
        }

        Request request() {
            return originalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        /**
         * Attempt to enqueue this async call on {@code executorService}. This will attempt to clean up
         * if the executor has been shut down by reporting the call as failed.
         */
        void executeOn(ExecutorService executorService) {
            assert (!Thread.holdsLock(client.dispatcher()));
            boolean success = false;
            try {
                executorService.execute(this);
                success = true;
            } catch (RejectedExecutionException e) {
                InterruptedIOException ioException = new InterruptedIOException("executor rejected");
                ioException.initCause(e);
                eventListener.callFailed(RealCall.this, ioException);
                responseCallback.onFailure(RealCall.this, ioException);
            } finally {
                if (!success) {
                    client.dispatcher().finished(this); // This call is no longer running!
                }
            }
        }

        @Override protected void execute() {
            boolean signalledCallback = false;
            timeout.enter();
            try {
                Response response = getResponseWithInterceptorChain();
                if (retryAndFollowUpInterceptor.isCanceled()) {
                    signalledCallback = true;
                    responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    responseCallback.onResponse(RealCall.this, response);
                }
            } catch (IOException e) {
                e = timeoutExit(e);
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
                } else {
                    eventListener.callFailed(RealCall.this, e);
                    responseCallback.onFailure(RealCall.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    /**
     * Returns a string that describes this call. Doesn't include a full URL as that might contain
     * sensitive information.
     */
    String toLoggableString() {
        return (isCanceled() ? "canceled " : "")
                + (forWebSocket ? "web socket" : "call")
                + " to " + redactedUrl();
    }

    String redactedUrl() {
        return originalRequest.url().redact();
    }

    Response getResponseWithInterceptorChain() throws IOException {
        // Build a full stack of interceptors.
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(client.interceptors());
        interceptors.add(retryAndFollowUpInterceptor);
        interceptors.add(new BridgeInterceptor(client.cookieJar()));
        interceptors.add(new CacheInterceptor(client.internalCache()));
        interceptors.add(new ConnectInterceptor(client));
        if (!forWebSocket) {
            interceptors.addAll(client.networkInterceptors());
        }
        interceptors.add(new CallServerInterceptor(forWebSocket));

        Interceptor.Chain chain = new RealInterceptorChain(interceptors, null, null, null, 0,
                originalRequest, this, eventListener, client.connectTimeoutMillis(),
                client.readTimeoutMillis(), client.writeTimeoutMillis());

        return chain.proceed(originalRequest);
    }
}
