package com.hl.utils.net;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface NetService {

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedio(@Body boolean once_no);

    //GET请求 Query  key-value形式拼接在url后边
    @GET("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);

    //GET请求 Query  集合key-value形式拼接在url后边
    @GET("book/search")
    Observable<RetrofitEntity> getSearchBooks(@QueryMap Map<String, String> options);

    //GET请求 Query  一个key 对应 多个value
    @GET("book/search")
    Call<RetrofitEntity> getSearchBooks(@Query("q") List<String> name);

    //GET请求 path ,需要在url中拼接参数， book/1001
    @GET("book/{id}")
    Call<RetrofitEntity> getBook(@Path("id") String id);

    //POST请求 Field 参数会放在请求体中
    // @FormUrlEncoded将会自动将请求参数的类型调整为application/x-www-form-urlencoded// content=Good+Luck
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationCodePost(@Field("tel") String tel, @Field("password") String pass);

    //POST请求 boolean 类型
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<RetrofitEntity> getV(@Field(value = "book", encoded = true) String book);

    //POST请求
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationCodePostMap(@FieldMap Map<String, String> map);

    //POST请求 body 实体参数传递
    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviews(@Body RetrofitEntity reviews);

    //GET请求，设置缓存
    @Headers("Cache-Control: public," + NetUtils.CACHE_CONTROL_CACHE)
    @GET("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);

    @Headers("Cache-Control: public," + NetUtils.CACHE_CONTROL_NETWORK)
    @GET("bjws/app.menu/getMenu")
    Observable<RetrofitEntity> getMainMenu();

    // 上传单个文件
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    // 上传单个文件 自定义URL，带参数。
    @Multipart
    @POST
    Observable<RetrofitEntity> upload(
            @Url String url,
            @Query("access_token") String access_token,
            @Part MultipartBody.Part file);

    // 上传单个文件
    @Multipart
    @POST("upload")
    Observable<RetrofitEntity> uploadFile2(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    // 上传多个文件
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadMultipleFiles(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2);

}
