package com.example.mobileinternetpractice;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherUtils {
    // 高德天气API（需替换为自己的key：https://lbs.amap.com/）
    private static final String WEATHER_API = "https://restapi.amap.com/v3/weather/weatherInfo?key=你的高德APIKey&city=%s&extensions=base";

    // 获取实时天气
    public static void getRealTimeWeather(String city, OnWeatherResultListener listener) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format(WEATHER_API, city);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        listener.onError("获取天气失败：" + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onError("请求失败：" + response.code()));
                    return;
                }

                String json = response.body().string();
                try {
                    JSONObject obj = new JSONObject(json);
                    if ("1".equals(obj.getString("status"))) {
                        JSONObject liveWeather = obj.getJSONArray("lives").getJSONObject(0);
                        String weather = liveWeather.getString("weather");
                        String temperature = liveWeather.getString("temperature");

                        new Handler(Looper.getMainLooper()).post(() ->
                                listener.onSuccess(weather, Integer.parseInt(temperature)));
                    } else {
                        new Handler(Looper.getMainLooper()).post(() ->
                                listener.onError("城市不存在或API错误"));
                    }
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onError("解析天气失败：" + e.getMessage()));
                }
            }
        });
    }

    public interface OnWeatherResultListener {
        void onSuccess(String weather, int temp);
        void onError(String msg);
    }
}