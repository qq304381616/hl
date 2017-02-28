package com.hl.utils.net;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface NetService {

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedio(@Body boolean once_no);

    //GET请求
    @GET("article/2017-02/10211887.html?from=bdwz")
    Observable<String> getBaidu();

    //POST请求
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationCodePost(@Field("tel") String tel, @Field("password") String pass);

    //POST请求
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationCodePostMap(@FieldMap Map<String, String> map);

    //GET请求
    @GET("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);


    //GET请求，设置缓存
    @Headers("Cache-Control: public," + NetUtils.CACHE_CONTROL_CACHE)
    @GET("bjws/app.user/login")
    Observable<RetrofitEntity> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);


    @Headers("Cache-Control: public," + NetUtils.CACHE_CONTROL_NETWORK)
    @GET("bjws/app.menu/getMenu")
    Observable<RetrofitEntity> getMainMenu();

}
