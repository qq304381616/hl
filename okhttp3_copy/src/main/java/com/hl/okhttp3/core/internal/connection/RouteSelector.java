package com.hl.okhttp3.core.internal.connection;

import com.hl.okhttp3.core.Address;
import com.hl.okhttp3.core.Call;
import com.hl.okhttp3.core.EventListener;
import com.hl.okhttp3.core.HttpUrl;
import com.hl.okhttp3.core.internal.ws.Util;

import java.net.Proxy;
import java.util.Collections;
import java.util.List;

public final class RouteSelector {
    private final Address address;
    private final RouteDatabase routeDatabase;
    private final Call call ;
    private final EventListener eventListener;
    private List<Proxy> proxies = Collections.emptyList();
    private int nextProxyIndex;

    public RouteSelector(Address address, RouteDatabase routeDatabase, Call call, EventListener eventListener) {
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.call = call;
        this.eventListener = eventListener;

        resetNextProxy(address.url(), address.proxy());
    }

    private void resetNextProxy(HttpUrl url, Proxy proxy) {
        if (proxy != null) {
            proxies = Collections.singletonList(proxy);
        }else {
            List<Proxy> proxiesOrNull = address.proxySelector().select(url.uri());
            proxies = proxiesOrNull != null && !proxiesOrNull.isEmpty()
                    ? Util.immutableList(proxiesOrNull)
                    : Util.immutableList(Proxy.NO_PROXY);
        }
        nextProxyIndex = 0;
    }
}
