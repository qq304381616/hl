package com.hl.okhttp3.core;

import java.util.Collections;
import java.util.List;

public interface CookieJar {

    CookieJar NO_COOKIES = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return Collections.emptyList();
        }
    };

    void saveFromResponse(HttpUrl url, List<Cookie> cookies);

    List<Cookie> loadForRequest(HttpUrl url);
}
