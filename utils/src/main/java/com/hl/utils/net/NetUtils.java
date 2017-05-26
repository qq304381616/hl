package com.hl.utils.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.hl.utils.base.MyApplication;

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

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * okhttp + rxjava + retrofit
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private static final boolean userFakeData = false; // 使用假数据

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

    private static Gson gson = new Gson();

    //----------------------------接口-----------------------------------------

    public static void login(String timestamp, HttpCallback callback) {
        if (userFakeData) {
            callback.onNext(gson.fromJson(gson.toJson(""), Object.class));
        } else {
            service.login("", "", "")
                    .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(callback).subscribe(callback);

//            // map 参数 。建议使用。方便管理参数
//            Map<String, String> m = new HashMap<String, String>();
//            m.put("timestamp", timestamp);
//            service.login(m)
//                    .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(callback).subscribe(callback);

        }
    }

    public static void syncLogin(String timestamp, HttpCallback callback) throws IOException {
        if (userFakeData) {
            callback.onNext(gson.fromJson(gson.toJson(""), Object.class));
        } else {
            sync(timestamp, callback);
        }
    }

    private static void sync(String timestamp,HttpCallback callback) throws IOException {
        callback.call();
//        // map 参数 。建议使用。方便管理参数
//        Map<String, String> m = new HashMap<String, String>();
//        m.put("timestamp", timestamp);
//        Call<Object> example = service.synclogin(m);

        Call<Object> example = service.synclogin("", "", "");
        Response<Object> response = example.execute();
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
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
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
