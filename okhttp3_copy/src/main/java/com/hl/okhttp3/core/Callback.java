package com.hl.okhttp3.core;

import java.io.IOException;

public interface Callback {

    void onFailure(Call call, IOException e);

    void onResponse(Call call, Response response) throws IOException;
}
