package com.example.mobileinternetpractice.api;

import com.example.mobileinternetpractice.model.CityResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// API接口（Java版无suspend，用Call封装）
public interface CityApiService {
    @GET("v3/config/district")
    Call<CityResponse> checkCity(
            @Query("keywords") String cityName,
            @Query("subdistrict") int subdistrict,
            @Query("key") String key
    );

    // 重载方法（简化调用，默认参数）
    default Call<CityResponse> checkCity(String cityName) {
        return checkCity(cityName, 0, "你的高德API密钥"); // 替换为自己的密钥
    }
}