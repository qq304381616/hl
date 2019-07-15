package com.hl.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class OkHttpCallback {

    private Response response;
    private Call call;
    private IOException e;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public IOException getE() {
        return e;
    }

    public void setE(IOException e) {
        this.e = e;
    }

    void onFailure(Call call, IOException e) {

    }

    void onResponse(Call call, Response response) throws IOException {

    }
}
