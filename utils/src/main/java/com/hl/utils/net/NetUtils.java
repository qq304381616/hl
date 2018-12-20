package com.hl.utils.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hl.utils.L;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * okhttp + rxjava + retrofit
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private static final boolean userFakeData = false; // 使用假数据

    private static String BASE_URL = "http://wthrcdn.etouch.cn/";

    protected static final NetService service = getRetrofit().create(NetService.class);

    private static Gson gson = new Gson();

    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    public static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private static Retrofit getRetrofit() {
        OkHttpClient mOkHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(Baseapp.getContext().getCacheDir(), cacheSize);
//        builder.cache(cache);
        mOkHttpClient = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    //----------------------------接口-----------------------------------------

    public static void weather(String citykey, HttpCallback callback) {
        if (userFakeData) {
            callback.onNext(gson.fromJson(CacheData.weather, JsonElement.class));
        } else {
            Map<String, String> m = new HashMap<>();
            m.put("citykey", citykey);
            service.weather(m)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(callback);
        }
    }

    public static void syncWeather(String citykey, HttpCallback callback) {
        if (userFakeData) {
            callback.onNext(gson.fromJson(CacheData.weather, JsonElement.class));
        } else {
            Map<String, String> m = new HashMap<>();
            m.put("citykey", citykey);
            Call<JsonElement> example = service.syncWeather(m);
            sync(example, callback);
        }
    }

    private static void sync(Call<JsonElement> example, HttpCallback callback) {
        callback.onSubscribe(null);
        Response<JsonElement> response = null;
        try {
            response = example.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.body() != null) {
            callback.onNext(response.body());
        } else {
            callback.onError(new IllegalArgumentException());
        }
    }

    //----------------------------文件上传-----------------------------------------

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String file) {
        File f = new File(file);
        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), f);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, f.getName(), requestFile);
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
                L.e("success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                L.e(t.getMessage());
            }
        });
    }

    // 文件上传
    public static Observable<HttpBaseEntity> test6(String path) {
//        MultipartBody.Part id = MultipartBody.Part.createFormData("userid", "userid");  // 对应  @Part MultipartBody.Part userid,
//        RequestBody id = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), "userid"); // 对应  @Part("description") RequestBody description,
        MultipartBody.Part body1 = NetUtils.prepareFilePart("video", "/sdcard/");
        RequestBody description = NetUtils.createPartFromString("hello, this is description speaking");
        return service.uploadFile2(description, body1).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 上传文件,android 实现
     */
    public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------7d4a6d158c9"; // boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n"); // inputName 对应表单name
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    String contentType = "application/octet-stream";

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
