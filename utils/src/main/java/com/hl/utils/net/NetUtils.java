package com.hl.utils.net;

import android.support.annotation.NonNull;

import com.hl.utils.LogUtils;
import com.hl.utils.NetworkUtils;
import com.hl.utils.base.MyApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
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
//        builder.addInterceptor(sLoggingInterceptor);
//        builder.addInterceptor(sRewriteCacheControlInterceptor);
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

    public static void test4() {

        service.getBaidu().subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.e("TAG", "doOnSubscribe 3");
                    }
                }).
                subscribe(new Observer<String>() {
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
                    public void onNext(String verification) {
                        LogUtils.e("TAG", "onNext 3 : " + verification);
                    }
                });
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            LogUtils.e(TAG, "sRewriteCacheControlInterceptor invoked");
            Request request = chain.request();
            if (!NetworkUtils.isConnected(MyApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            okhttp3.Response originalResponse = chain.proceed(request);

            if (NetworkUtils.isConnected(MyApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            LogUtils.e(TAG, "sLoggingInterceptor invoked");
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                LogUtils.e(TAG, "request.body() == null");
            }
            //打印url信息
            LogUtils.e(TAG, request.url() + (request.body() != null ? "?" + _parseParams(request.body(), requestBuffer) : ""));
            final okhttp3.Response response = chain.proceed(request);

            return response;
        }
    };

    @NonNull
    private static String _parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
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

    //POST请求
    public static void verfacationCodePost(String tel, String pass, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getVerfcationCodePost(tel, pass), observer);
    }

    //POST请求参数以map传入
    public static void verfacationCodePostMap(Map<String, String> map, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getVerfcationCodePostMap(map), observer);
    }

    //Get请求设置缓存
    public static void verfacationCodeGetCache(String tel, String pass, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getVerfcationGetCache(tel, pass), observer);
    }

    //Get请求
    public static void verfacationCodeGet(String tel, String pass, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getVerfcationGet(tel, pass), observer);
    }

    //Get请求
    public static void verfacationCodeGetsub(String tel, String pass, Observer<RetrofitEntity> observer) {
        setSubscribe(service.getVerfcationGet(tel, pass), observer);
    }

    //Get请求
    public static void Getcache(Observer<RetrofitEntity> observer) {
        setSubscribe(service.getMainMenu(), observer);
    }

    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
