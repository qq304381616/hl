package com.hl.okhttp3.core;

import java.io.IOException;

public interface Call extends Cloneable{

    void cancel();

    Response execute() throws IOException;

    interface Factory{
        Call newCall(Request request);
    }
}
