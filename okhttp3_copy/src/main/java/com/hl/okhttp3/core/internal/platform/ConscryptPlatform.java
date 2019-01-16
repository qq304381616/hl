package com.hl.okhttp3.core.internal.platform;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.Protocol;

import org.conscrypt.Conscrypt;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Platform using Conscrypt (conscrypt.org) if installed as the first Security Provider.
 * <p>
 * Requires org.conscrypt:conscrypt-openjdk-uber on the classpath.
 */
public class ConscryptPlatform extends Platform {
    private ConscryptPlatform() {
    }

    private Provider getProvider() {
        return Conscrypt.newProviderBuilder().provideTrustManager().build();
    }

    @Override
    public @Nullable
    X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        if (!Conscrypt.isConscrypt(sslSocketFactory)) {
            return super.trustManager(sslSocketFactory);
        }

        try {
            // org.conscrypt.SSLParametersImpl
            Object sp =
                    readFieldOrNull(sslSocketFactory, Object.class, "sslParameters");

            if (sp != null) {
                return readFieldOrNull(sp, X509TrustManager.class, "x509TrustManager");
            }

            return null;
        } catch (Exception e) {
            throw new UnsupportedOperationException(
                    "clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on Conscrypt", e);
        }
    }

    @Override
    public void configureTlsExtensions(
            SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
        if (Conscrypt.isConscrypt(sslSocket)) {
            // Enable SNI and session tickets.
            if (hostname != null) {
                Conscrypt.setUseSessionTickets(sslSocket, true);
                Conscrypt.setHostname(sslSocket, hostname);
            }

            // Enable ALPN.
            List<String> names = Platform.alpnProtocolNames(protocols);
            Conscrypt.setApplicationProtocols(sslSocket, names.toArray(new String[0]));
        } else {
            super.configureTlsExtensions(sslSocket, hostname, protocols);
        }
    }

    @Override
    public @Nullable
    String getSelectedProtocol(SSLSocket sslSocket) {
        if (Conscrypt.isConscrypt(sslSocket)) {
            return Conscrypt.getApplicationProtocol(sslSocket);
        } else {
            return super.getSelectedProtocol(sslSocket);
        }
    }

    @Override
    public SSLContext getSSLContext() {
        try {
            return SSLContext.getInstance("TLSv1.3", getProvider());
        } catch (NoSuchAlgorithmException e) {
            try {
                // Allow for Conscrypt 1.2
                return SSLContext.getInstance("TLS", getProvider());
            } catch (NoSuchAlgorithmException e2) {
                throw new IllegalStateException("No TLS provider", e);
            }
        }
    }

    public static ConscryptPlatform buildIfSupported() {
        try {
            // Trigger an early exception over a fatal error, prefer a RuntimeException over Error.
            Class.forName("org.conscrypt.Conscrypt");

            if (!Conscrypt.isAvailable()) {
                return null;
            }

            return new ConscryptPlatform();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void configureSslSocketFactory(SSLSocketFactory socketFactory) {
        if (Conscrypt.isConscrypt(socketFactory)) {
            Conscrypt.setUseEngineSocket(socketFactory, true);
        }
    }
}
