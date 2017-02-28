package com.hl.utils.net;


import com.hl.utils.LogUtils;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class RetrofitUtils {

    private static Retrofit retrofit;
    private static String BASE_URL = " http://www.izaodao.com/Api/";

    public static void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 接口地址
     * Created by WZG on 2016/7/16.
     */
    public interface MyApiEndpointInterface {
        @POST("AppFiftyToneGraph/videoLink")
        Call<RetrofitEntity> getAllVedio(@Body boolean once_no);

        Call<List<RetrofitEntity>> listRepos(@Path("user") String user);

        //        @GET("users/list")
        //        @GET("users/list?sort=desc")
        @GET("group/{id}/users")
        Call<List<RetrofitEntity>> groupList(@Path("id") int groupId);


        @GET("group/{id}/users")
        Call<List<RetrofitEntity>> groupList(@Path("id") int groupId, @Query("sort") String sort);

        @GET("group/{id}/users")
        Call<List<RetrofitEntity>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);

        @POST("users/new")
        Call<RetrofitEntity> createUser(@Body RetrofitEntity user);

        @FormUrlEncoded
        @POST("user/edit")
        Call<RetrofitEntity> updateUser(@Field("first_name") String first, @Field("last_name") String last);

        @Multipart
        @PUT("user/photo")
        Call<RetrofitEntity> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

        @Headers("Cache-Control: max-age=640000")
        @GET("widget/list")
        Call<List<RetrofitEntity>> widgetList();

        @Headers({
                "Accept: application/vnd.github.v3.full+json",
                "User-Agent: Retrofit-Sample-App"
        })
        @GET("users/{username}")
        Call<RetrofitEntity> getUser(@Path("username") RetrofitEntity username);

        @GET("user")
        Call<RetrofitEntity> getUser(@Header("Authorization") String authorization);
    }

    public static void test() {
        MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);
        Call<RetrofitEntity> call = apiService.getAllVedio(true);
        call.enqueue(new Callback<RetrofitEntity>() {
            @Override
            public void onResponse(Call<RetrofitEntity> call, Response<RetrofitEntity> response) {
                RetrofitEntity entity = response.body();
                LogUtils.e("tag", "onResponse----->" + entity.getMsg());
            }

            @Override
            public void onFailure(Call<RetrofitEntity> call, Throwable t) {
                LogUtils.e("tag", "onFailure----->" + t.toString());
            }
        });
    }

}
