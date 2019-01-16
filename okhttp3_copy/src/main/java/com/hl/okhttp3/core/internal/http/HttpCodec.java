package com.hl.okhttp3.core.internal.http;

import com.hl.okhttp3.core.Headers;
import com.hl.okhttp3.core.Request;
import com.hl.okhttp3.core.Response;
import com.hl.okhttp3.core.ResponseBody;

import java.io.IOException;

import okio.Sink;

public interface HttpCodec {
    int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    Sink createRequestBody(Request request, long contentLength);

    void writeRequestHeaders(Request request) throws IOException;

    void flushRequest() throws IOException;

    void finishRequest() throws IOException;

    Response.Builder readResponseHeaders(boolean expectContinue) throws IOException;

    ResponseBody openResponseBody(Response response) throws IOException;

    Headers trailers() throws IOException;

    void cancel();
}
