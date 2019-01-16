package com.hl.okhttp3.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class Challenge {

    private final String scheme;
    private final Map<String, String> authParams;

    public Challenge(String scheme, Map<String, String> authParams) {
        if (scheme == null) throw new NullPointerException("scheme == null");
        if (authParams == null) throw new NullPointerException("authParams == null");
        this.scheme = scheme;
        Map<String, String> newAuthParams = new LinkedHashMap<>();
        for (Map.Entry<String, String> authParam : authParams.entrySet()) {
            String key = (authParam.getKey() == null) ? null : authParam.getKey().toLowerCase(Locale.US);
            newAuthParams.put(key, authParam.getValue());
        }
        this.authParams = Collections.unmodifiableMap(newAuthParams);
    }

    public Challenge(String scheme, String realm) {
        if (scheme == null) throw new NullPointerException("scheme == null");
        if (realm == null) throw new NullPointerException("realm == null");
        this.scheme = scheme;
        this.authParams = Collections.singletonMap("realm", realm);
    }
}
