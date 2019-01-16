package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import java.net.Socket;

public interface Connection {
    Route route();

    Socket socket();

    @Nullable
    Handshake handshake();

    Protocol protocol();
}
