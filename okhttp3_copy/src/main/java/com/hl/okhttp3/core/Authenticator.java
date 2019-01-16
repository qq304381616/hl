package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import java.io.IOException;

public interface Authenticator {

    @Nullable Request authenticate(@Nullable Route route, Response response) throws IOException;
    Authenticator NONE = new Authenticator() {
        @Override public Request authenticate(@Nullable Route route, Response response) {
            return null;
        }
    };
}
