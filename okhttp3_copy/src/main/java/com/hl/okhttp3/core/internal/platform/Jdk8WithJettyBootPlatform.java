package com.hl.okhttp3.core.internal.platform;

import java.lang.reflect.Method;

import javax.net.ssl.SSLSocket;

public class Jdk8WithJettyBootPlatform extends Platform{

    private final Method putMethod;
    private final Method getMethod;
    private final Method removeMethod;
    private final Class<?> clientProviderClass;
    private final Class<?> serverProviderClass;

    Jdk8WithJettyBootPlatform(Method putMethod, Method getMethod, Method removeMethod,
                              Class<?> clientProviderClass, Class<?> serverProviderClass) {
        this.putMethod = putMethod;
        this.getMethod = getMethod;
        this.removeMethod = removeMethod;
        this.clientProviderClass = clientProviderClass;
        this.serverProviderClass = serverProviderClass;
    }

    public static Platform buildIfSupported() {
        // Find Jetty's ALPN extension for OpenJDK.
        try {
            String alpnClassName = "org.eclipse.jetty.alpn.ALPN";
            Class<?> alpnClass = Class.forName(alpnClassName);
            Class<?> providerClass = Class.forName(alpnClassName + "$Provider");
            Class<?> clientProviderClass = Class.forName(alpnClassName + "$ClientProvider");
            Class<?> serverProviderClass = Class.forName(alpnClassName + "$ServerProvider");
            Method putMethod = alpnClass.getMethod("put", SSLSocket.class, providerClass);
            Method getMethod = alpnClass.getMethod("get", SSLSocket.class);
            Method removeMethod = alpnClass.getMethod("remove", SSLSocket.class);
            return new Jdk8WithJettyBootPlatform(
                    putMethod, getMethod, removeMethod, clientProviderClass, serverProviderClass);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }

        return null;
    }
}
