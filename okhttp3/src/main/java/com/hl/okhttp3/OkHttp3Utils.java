package com.hl.okhttp3;

import android.support.annotation.Nullable;

import com.hl.utils.L;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OkHttp3Utils {

    private OkHttp3Utils() {
    }

    private static OkHttp3Utils instance;
    private static OkHttpClient okHttpClient;

    public static OkHttp3Utils getInstance() {
        if (instance == null) {
            instance = new OkHttp3Utils();
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();
        }
        return instance;
    }

    // 异步GET请求
    private void get(String url, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse: " + response);
                callback.onResponse(call, response);
            }
        });
    }

    // 同步GET请求
    private OkHttpCallback getAsync(String url) {
        final OkHttpCallback callback = new OkHttpCallback();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        callback.setCall(call);
        try {
            callback.setResponse(call.execute());
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(call, e);
        }
        return callback;
    }

    // POST方式提交表单
    private void postForm(String url, Map<String, String> params, final OkHttpCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entries : params.entrySet()) {
                builder.add(entries.getKey(), entries.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("onResponse: " + response);
                callback.onResponse(call, response);
            }
        });
    }

    // POST方式提交表单
    private OkHttpCallback postAsyncForm(String url, Map<String, String> params) {
        final OkHttpCallback callback = new OkHttpCallback();
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entries : params.entrySet()) {
                builder.add(entries.getKey(), entries.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        final Call call = okHttpClient.newCall(request);
        callback.setCall(call);
        try {
            callback.setResponse(call.execute());
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(call, e);
        }
        return callback;
    }

    //  POST方式提交String
    private void postString() {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e(response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    L.e(headers.name(i) + ":" + headers.value(i));
                }
                L.e("onResponse: " + response.body().string());
            }
        });
    }

    // POST方式提交流
    private void postStream() {
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm.");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e(response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    L.e(headers.name(i) + ":" + headers.value(i));
                }
                L.e("onResponse: " + response.body().string());
            }
        });
    }

    // POST提交文件
    private void postFile() {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        File file = new File("test.md");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, file))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e(response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    L.e(headers.name(i) + ":" + headers.value(i));
                }
                L.e("onResponse: " + response.body().string());
            }
        });
    }

    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    //  POST方式提交分块请求
    private void postPart() {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());

            }
        });
    }

    // 拦截器-interceptor
    public static class LoggingInterceptor implements Interceptor {
        private static final String TAG = "LoggingInterceptor";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long startTime = System.nanoTime();
            L.e(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long endTime = System.nanoTime();
            L.e(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (endTime - startTime) / 1e6d, response.headers()));

            return response;
        }
    }
}
