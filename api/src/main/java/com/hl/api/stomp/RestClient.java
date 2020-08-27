package com.hl.api.stomp;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 未测试
 */
public class RestClient {

    public static final String ANDROID_EMULATOR_LOCALHOST = "live.xiaotunyun.com/liveclassroomTX";
    public static final String SERVER_PORT = "80";
    private static final Object lock = new Object();
    private static RestClient instance;
    private final ExampleRepository mExampleRepository;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + ANDROID_EMULATOR_LOCALHOST + ":" + SERVER_PORT + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mExampleRepository = retrofit.create(ExampleRepository.class);
    }

    public static RestClient getInstance() {
        RestClient instance = RestClient.instance;
        if (instance == null) {
            synchronized (lock) {
                instance = RestClient.instance;
                if (instance == null) {
                    RestClient.instance = instance = new RestClient();
                }
            }
        }
        return instance;
    }

    public ExampleRepository getExampleRepository() {
        return mExampleRepository;
    }
}
