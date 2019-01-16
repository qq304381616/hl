package com.hl.okhttp3.core.internal.connection;

import com.hl.okhttp3.core.Route;

import java.util.LinkedHashSet;
import java.util.Set;

public final class RouteDatabase {
    private final Set<Route> failedRoutes = new LinkedHashSet<>();

    /** Records a failure connecting to {@code failedRoute}. */
    public synchronized void failed(Route failedRoute) {
        failedRoutes.add(failedRoute);
    }

    /** Records success connecting to {@code route}. */
    public synchronized void connected(Route route) {
        failedRoutes.remove(route);
    }

    /** Returns true if {@code route} has failed recently and should be avoided. */
    public synchronized boolean shouldPostpone(Route route) {
        return failedRoutes.contains(route);
    }
}
