package com.example.mobileinternetpractice;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvUser, tvWeatherGlobal, tvDiceResult, tvCoinResult;
    private Button btnEat, btnWear, btnPlay, btnDice, btnCoin, btnChangeLocation, btnChangeBg;
    // 天气数据（实时更新）
    public static String weather = "晴";
    public static int temp = 22;
    public static String location = "北京";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        // 加载实时天气
        loadRealTimeWeather();
        initData();
        initListener();
    }

    private void initView() {
        tvUser = findViewById(R.id.tv_user);
        tvWeatherGlobal = findViewById(R.id.tv_weather_global);
        tvDiceResult = findViewById(R.id.tv_dice_result);
        tvCoinResult = findViewById(R.id.tv_coin_result);
        btnEat = findViewById(R.id.btn_eat);
        btnWear = findViewById(R.id.btn_wear);
        btnPlay = findViewById(R.id.btn_play);
        btnDice = findViewById(R.id.btn_dice);
        btnCoin = findViewById(R.id.btn_coin);
        // 新增地点/背景按钮
        btnChangeLocation = findViewById(R.id.btn_change_location);
        btnChangeBg = findViewById(R.id.btn_change_bg);
    }

    private void initData() {
        // 设置用户名
        tvUser.setText("当前登录：" + LoginActivity.currentUser);
        // 设置全局天气
        updateWeatherUI();
    }

    // 更新天气UI
    private void updateWeatherUI() {
        tvWeatherGlobal.setText(location + " 实时天气：" + temp + "℃ " + weather);
    }

    // 加载实时天气
    private void loadRealTimeWeather() {
        WeatherUtils.getRealTimeWeather(location, new WeatherUtils.OnWeatherResultListener() {
            @Override
            public void onSuccess(String weatherStr, int tempInt) {
                weather = weatherStr;
                temp = tempInt;
                updateWeatherUI();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                // 失败时用默认值
                updateWeatherUI();
            }
        });
    }

    private void initListener() {
        // 吃什么
        btnEat.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "eat");
            startActivity(intent);
        });

        // 穿什么
        btnWear.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "wear");
            startActivity(intent);
        });

        // 玩什么
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "play");
            startActivity(intent);
        });

        // 骰子
        btnDice.setOnClickListener(v -> {
            Random random = new Random();
            int dice = random.nextInt(6) + 1;
            tvDiceResult.setText(String.valueOf(dice));
            Toast.makeText(this, "骰子点数：" + dice, Toast.LENGTH_SHORT).show();
        });

        // 金币
        btnCoin.setOnClickListener(v -> {
            Random random = new Random();
            String coin = random.nextBoolean() ? "正面" : "反面";
            tvCoinResult.setText(coin);
            Toast.makeText(this, "金币：" + coin, Toast.LENGTH_SHORT).show();
        });

        // 更换地点
        btnChangeLocation.setOnClickListener(v -> {
            new LocationSelectDialog(MainActivity.this, location -> {
                MainActivity.location = location;
                loadRealTimeWeather();
            }).show();
        });

        // 更换背景
        btnChangeBg.setOnClickListener(v -> {
            new BackgroundSelectDialog(MainActivity.this, drawable -> {
                // 设置页面背景
                findViewById(R.id.ll_main).setBackground(drawable);
            }).show();
        });
    }
}