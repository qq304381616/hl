package com.hl.okhttp3.core.internal.ws;

import com.hl.okhttp3.core.ConnectionPool;
import com.hl.okhttp3.core.internal.connection.RouteDatabase;

public abstract class Internal {

    public static Internal instance;

    public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);
}
