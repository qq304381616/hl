package com.hl.okhttp3.core.internal.cache;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.Request;
import com.hl.okhttp3.core.Response;

import java.io.IOException;

public interface InternalCache {
    @Nullable
    Response get(Request request) throws IOException;

    @Nullable CacheRequest put(Response response) throws IOException;

    /**
     * Remove any cache entries for the supplied {@code request}. This is invoked when the client
     * invalidates the cache, such as when making POST requests.CacheStrategy
     */
    void remove(Request request) throws IOException;

    /**
     * Handles a conditional request hit by updating the stored cache response with the headers from
     * {@code network}. The cached response body is not updated. If the stored response has changed
     * since {@code cached} was returned, this does nothing.
     */
    void update(Response cached, Response network);

    /** Track an conditional GET that was satisfied by this cache. */
    void trackConditionalCacheHit();

    /** Track an HTTP response being satisfied with {@code cacheStrategy}. */
    void trackResponse(CacheStrategy cacheStrategy);
}
