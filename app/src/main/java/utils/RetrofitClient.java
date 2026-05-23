package com.example.mobileinternetpractice.utils;

import com.example.mobileinternetpractice.api.CityApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Retrofit单例工具类
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://restapi.amap.com/";

    // 获取Retrofit实例
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // 获取CityApiService实例
    public static CityApiService getCityApiService() {
        return getRetrofitInstance().create(CityApiService.class);
    }
}