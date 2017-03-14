package com.hl.utils.net;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hl.utils.LogUtils;
import com.hl.utils.NetworkUtils;
import com.hl.utils.base.MyApplication;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * okhttp + rxjava + retrofit
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private static String BASE_URL = " http://www.izaodao.com/Api/";
//    private static String BASE_URL = "http://www.baidu.com/";

    protected static final NetService service = getRetrofit().create(NetService.class);


    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    public static Retrofit getRetrofit() {
        OkHttpClient mOkHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(MyApplication.getContext().getCacheDir(), cacheSize);
        builder.cache(cache);
        mOkHttpClient = builder.build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        return mRetrofit;
    }

    public static void test2() {
        getAllVedio(true, new Observer<RetrofitEntity>() {
            @Override
            public void onCompleted() {
                LogUtils.e("TAG", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("TAG", "onError" + e.getMessage());
            }

            @Override
            public void onNext(RetrofitEntity verification) {
                verification.getMsg();
                LogUtils.e("TAG", "onNext : " + verification.getMsg());
            }
        });
    }

    public static void test3() {
        getAllVedio2(true)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("TAG", "doOnSubscribe 3");
                    }
                }).
                subscribe(new Observer<RetrofitEntity>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("TAG", "onCompleted 3");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.e("TAG", "onError 3 " + e.getMessage());
                    }

                    @Override
                    public void onNext(RetrofitEntity verification) {
                        verification.getMsg();
                        LogUtils.e("TAG", "onNext 3 : " + verification.getMsg());
                    }
                });
    }

    /**
     * 方式一
     */
    public static void getAllVedio(boolean b, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getAllVedio(b), observer);
    }

    /**
     * 方式二
     */
    public static Observable<RetrofitEntity> getAllVedio2(boolean b) {
        return service.getAllVedio(b).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread());//回调到主线程
    }

    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

    //----------------------------文件上传-----------------------------------------

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String file) {
        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, file.toString(), requestFile);
    }

    // 文件上传
    public void test5() {
        // 创建文件的part (photo, video, ...)
        MultipartBody.Part body1 = NetUtils.prepareFilePart("video", "/sdcard/");
        MultipartBody.Part body2 = NetUtils.prepareFilePart("thumbnail", "/sdcard/");

        // 添加其他的part
        RequestBody description = NetUtils.createPartFromString("hello, this is description speaking");

        // 最后执行异步请求操作
        Call<ResponseBody> call = service.uploadMultipleFiles(description, body1, body2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    // 文件上传
    public static Observable<RetrofitEntity> test6(String path) {
        MultipartBody.Part body1 = NetUtils.prepareFilePart("video", "/sdcard/");
        RequestBody description = NetUtils.createPartFromString("hello, this is description speaking");
        return service.uploadFile2(description, body1).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
    //----------------------------文件上传-----------------------------------------
}
