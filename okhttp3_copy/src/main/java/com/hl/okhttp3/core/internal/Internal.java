package com.hl.okhttp3.core.internal;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import com.hl.okhttp3.core.Address;
import com.hl.okhttp3.core.Call;
import com.hl.okhttp3.core.ConnectionPool;
import com.hl.okhttp3.core.ConnectionSpec;
import com.hl.okhttp3.core.Headers;
import com.hl.okhttp3.core.OkHttpClient;
import com.hl.okhttp3.core.Request;
import com.hl.okhttp3.core.Response;
import com.hl.okhttp3.core.Route;
import com.hl.okhttp3.core.internal.cache.InternalCache;
import com.hl.okhttp3.core.internal.connection.RealConnection;
import com.hl.okhttp3.core.internal.connection.RouteDatabase;
import com.hl.okhttp3.core.internal.connection.StreamAllocation;
import com.hl.okhttp3.core.internal.http.HttpCodec;
import okio.BufferedSink;

/**
 * Escalate internal APIs in {@code okhttp3} so they can be used from OkHttp's implementation
 * packages. The only implementation of this interface is in {@link OkHttpClient}.
 */
public abstract class Internal {

    public static void initializeInstanceForTests() {
        // Needed in tests to ensure that the instance is actually pointing to something.
        new OkHttpClient();
    }

    public static Internal instance;

    public abstract void addLenient(Headers.Builder builder, String line);

    public abstract void addLenient(Headers.Builder builder, String name, String value);

    public abstract void setCache(OkHttpClient.Builder builder, InternalCache internalCache);

    public abstract void acquire(ConnectionPool pool, Address address,
                                 StreamAllocation streamAllocation, @Nullable Route route);

    public abstract boolean equalsNonHost(Address a, Address b);

    public abstract @Nullable Socket deduplicate(
            ConnectionPool pool, Address address, StreamAllocation streamAllocation);

    public abstract void put(ConnectionPool pool, RealConnection connection);

    public abstract boolean connectionBecameIdle(ConnectionPool pool, RealConnection connection);

    public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);

    public abstract int code(Response.Builder responseBuilder);

    public abstract void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket,
                               boolean isFallback);

    public abstract boolean isInvalidHttpUrlHost(IllegalArgumentException e);

    public abstract StreamAllocation streamAllocation(Call call);

    public abstract @Nullable IOException timeoutExit(Call call, @Nullable IOException e);

    public abstract Call newWebSocketCall(OkHttpClient client, Request request);

    public abstract void duplex(Request.Builder requestBuilder, String method);

    public abstract void sinkAndCodec(
            Response.Builder responseBuilder, BufferedSink sink, HttpCodec httpCodec);

    public abstract BufferedSink sink(Response response);

    public abstract boolean isDuplex(Request request);
}