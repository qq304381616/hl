package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;


public final class Address {
    final HttpUrl url;
    final Dns dns;
    final SocketFactory socketFactory;
    final Authenticator proxyAuthenticator;
    final List<Protocol> protocols;
    final List<ConnectionSpec> connectionSpecs;
    final ProxySelector proxySelector;

    final @Nullable
    Proxy proxy;
    final @Nullable
    SSLSocketFactory sslSocketFactory;
    final @Nullable
    HostnameVerifier hostnameVerifier;
    final @Nullable
    CertificatePinner certificatePinner;


    public Address(String uriHost, int uriPort, Dns dns, SocketFactory socketFactory,
                   @Nullable SSLSocketFactory sslSocketFactory, @Nullable HostnameVerifier hostnameVerifier,
                   @Nullable CertificatePinner certificatePinner, Authenticator proxyAuthenticator,
                   @Nullable Proxy proxy, List<Protocol> protocols, List<ConnectionSpec> connectionSpecs,
                   ProxySelector proxySelector) {
        this.url = new HttpUrl.Builder()
                .scheme(sslSocketFactory != null ? "https" : "http")
                .host(uriHost)
                .port(uriPort)
                .build();
        if (dns == null) throw new NullPointerException("dns == null");
        this.dns = dns;
        if (socketFactory == null) throw new NullPointerException("socketFactory == null");
        this.socketFactory = socketFactory;
        if (proxyAuthenticator == null)
            throw new NullPointerException("proxyAuthenticator == null");
        this.proxyAuthenticator = proxyAuthenticator;
        if (protocols == null) throw new NullPointerException("protocols == null");
        this.protocols = protocols;
        if (connectionSpecs == null) throw new NullPointerException("connectionSpecs == null");
        this.connectionSpecs = connectionSpecs;
        if (proxySelector == null) throw new NullPointerException("proxySelector == null");
        this.proxySelector = proxySelector;

        this.proxy = proxy;
        this.sslSocketFactory = sslSocketFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.certificatePinner = certificatePinner;
    }

    public ProxySelector proxySelector(){
        return proxySelector;
    }
    public HttpUrl url() {
        return url;
    }

    public @Nullable
    Proxy proxy() {
        return proxy;
    }
}
