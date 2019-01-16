package com.hl.okhttp3.core.internal.platform;

import android.support.annotation.Nullable;

import java.lang.reflect.Method;
import java.net.Socket;

public class AndroidPlatform extends Platform{

    private static final int MAX_LOG_LENGTH = 4000;

    private final Class<?> sslParametersClass;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;

    private final CloseGuard closeGuard = CloseGuard.get();

    AndroidPlatform(Class<?> sslParametersClass, OptionalMethod<Socket> setUseSessionTickets,
                    OptionalMethod<Socket> setHostname, OptionalMethod<Socket> getAlpnSelectedProtocol,
                    OptionalMethod<Socket> setAlpnProtocols) {
        this.sslParametersClass = sslParametersClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
    }

    public static @Nullable
    Platform buildIfSupported() {
        // Attempt to find Android 5+ APIs.
        try {
            Class<?> sslParametersClass = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
            OptionalMethod<Socket> setUseSessionTickets = new OptionalMethod<>(
                    null, "setUseSessionTickets", boolean.class);
            OptionalMethod<Socket> setHostname = new OptionalMethod<>(
                    null, "setHostname", String.class);
            OptionalMethod<Socket> getAlpnSelectedProtocol = new OptionalMethod<>(
                    byte[].class, "getAlpnSelectedProtocol");
            OptionalMethod<Socket> setAlpnProtocols = new OptionalMethod<>(
                    null, "setAlpnProtocols", byte[].class);
            return new AndroidPlatform(sslParametersClass, setUseSessionTickets, setHostname,
                    getAlpnSelectedProtocol, setAlpnProtocols);
        } catch (ClassNotFoundException ignored) {
            return null; // Not an Android runtime.
        }
    }
    static final class CloseGuard {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;

        CloseGuard(Method getMethod, Method openMethod, Method warnIfOpenMethod) {
            this.getMethod = getMethod;
            this.openMethod = openMethod;
            this.warnIfOpenMethod = warnIfOpenMethod;
        }

        static CloseGuard get() {
            Method getMethod;
            Method openMethod;
            Method warnIfOpenMethod;

            try {
                Class<?> closeGuardClass = Class.forName("dalvik.system.CloseGuard");
                getMethod = closeGuardClass.getMethod("get");
                openMethod = closeGuardClass.getMethod("open", String.class);
                warnIfOpenMethod = closeGuardClass.getMethod("warnIfOpen");
            } catch (Exception ignored) {
                getMethod = null;
                openMethod = null;
                warnIfOpenMethod = null;
            }
            return new CloseGuard(getMethod, openMethod, warnIfOpenMethod);
        }
    }
}
