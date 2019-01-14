package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import java.io.Closeable;

public class Response implements Cloneable {

    final @Nullable
    ResponseBody body;


    Response(Builder builder) {
        // TODO
        this.body = builder.body;

    }

    public ResponseBody body() {
        return body;
    }

    public static class Builder {
        @Nullable ResponseBody body;
    }
}
