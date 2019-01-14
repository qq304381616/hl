package com.hl.okhttp3.core.internal.connection;

import com.hl.okhttp3.core.Address;
import com.hl.okhttp3.core.Call;
import com.hl.okhttp3.core.ConnectionPool;
import com.hl.okhttp3.core.EventListener;
import com.hl.okhttp3.core.internal.http.HttpCodec;
import com.hl.okhttp3.core.internal.ws.Internal;

public final class StreamAllocation {

    private final ConnectionPool connectionPool;
    public final Address address;
    public final Call call ;
    public final EventListener eventListener;
    private final Object callStackTrace;
    private final RouteSelector routeSelector;
    private HttpCodec codec;
    private RealConnection connection;

    private boolean canceled;

    public StreamAllocation(ConnectionPool connectionPool, Address address, Call call, EventListener eventListener, Object callStackTrace) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
        this.routeSelector = new RouteSelector(address, routeDatabase(), call, eventListener);
        this.callStackTrace = callStackTrace;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(connectionPool);
    }

    public void cancel(){
        HttpCodec codecToCancel;
        RealConnection connectionToCancel ;
        synchronized (connectionPool) {
            canceled = true;
            codecToCancel = codec;
            connectionToCancel = connection ;
        }
        if( codecToCancel  != null){
            codecToCancel.cancel();
        } else if (connectionToCancel != null) {
            connectionToCancel.cancel();
        }
    }
}
