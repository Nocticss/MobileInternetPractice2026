package com.example.mobileinternetpractice;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 天气工具类（基于高德开放平台天气API）
 * 功能：根据城市名获取实时天气和温度
 * 依赖：OkHttp 网络库
 * 文档：https://lbs.amap.com/api/webservice/guide/api/weatherinfo/
 */
public class WeatherUtils {
    // 你提供的 KEY（已正确填入）
    private static final String AMAP_API_KEY = "6d20142983ca49c687be88ca3b3bd498";

    // 高德实时天气API地址
    private static final String WEATHER_API_URL =
            "https://restapi.amap.com/v3/weather/weatherInfo?key=%s&city=%s&extensions=base";

    /**
     * 获取指定城市的实时天气
     * @param city 城市名/城市编码（如"北京"、"110000"）
     * @param listener 结果回调（主线程回调）
     */
    public static void getRealTimeWeather(String city, OnWeatherResultListener listener) {
        // 空值校验
        if (city == null || city.trim().isEmpty()) {
            postErrorToMainThread(listener, "城市名不能为空");
            return;
        }

        // 初始化OkHttp客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        // 拼接完整API地址
        String requestUrl = String.format(WEATHER_API_URL, AMAP_API_KEY, city);

        // 构建请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();

        // 异步发起网络请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postErrorToMainThread(listener, "网络请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    postErrorToMainThread(listener, "API返回空数据");
                    return;
                }

                String responseJson = response.body().string();
                try {
                    JSONObject rootObj = new JSONObject(responseJson);

                    String status = rootObj.getString("status");
                    if (!"1".equals(status)) {
                        String info = rootObj.optString("info", "未知错误");
                        postErrorToMainThread(listener, "API请求失败：" + info);
                        return;
                    }

                    JSONArray livesArray = rootObj.getJSONArray("lives");
                    if (livesArray.length() == 0) {
                        postErrorToMainThread(listener, "未查询到该城市天气数据");
                        return;
                    }

                    JSONObject weatherObj = livesArray.getJSONObject(0);
                    String weather = weatherObj.getString("weather");
                    String tempStr = weatherObj.getString("temperature");
                    int temperature = Integer.parseInt(tempStr);

                    // 成功回调
                    postSuccessToMainThread(listener, weather, temperature);

                } catch (JSONException e) {
                    postErrorToMainThread(listener, "数据解析失败：" + e.getMessage());
                } catch (NumberFormatException e) {
                    postErrorToMainThread(listener, "温度格式错误：" + e.getMessage());
                } catch (Exception e) {
                    postErrorToMainThread(listener, "处理天气数据失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 主线程回调成功结果
     */
    private static void postSuccessToMainThread(OnWeatherResultListener listener, String weather, int temp) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (listener != null) {
                listener.onSuccess(weather, temp);
            }
        });
    }

    /**
     * 主线程回调错误结果
     */
    private static void postErrorToMainThread(OnWeatherResultListener listener, String errorMsg) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (listener != null) {
                listener.onError(errorMsg);
            }
        });
    }

    /**
     * 天气结果回调接口
     */
    public interface OnWeatherResultListener {
        void onSuccess(String weather, int temp);
        void onError(String msg);
    }
}