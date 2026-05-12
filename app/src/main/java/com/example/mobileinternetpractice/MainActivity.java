package com.example.mobileinternetpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvUser, tvWeather, tvResult;
    private Button btnRandom, btnMenu, btnCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUser = findViewById(R.id.tv_user);
        tvWeather = findViewById(R.id.tv_weather);
        tvResult = findViewById(R.id.tv_result);
        btnRandom = findViewById(R.id.btn_random);
        btnMenu = findViewById(R.id.btn_menu);
        btnCenter = findViewById(R.id.btn_center);

        tvUser.setText("当前登录：" + LoginActivity.currentUser);
        tvWeather.setText("北京实时天气：22℃ 晴");

        btnRandom.setOnClickListener(v -> randomEat());

        btnMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
        btnCenter.setOnClickListener(v -> startActivity(new Intent(this, UserCenterActivity.class)));
    }

    private void randomEat() {
        SharedPreferences sp = getSharedPreferences("Food_" + LoginActivity.currentUser, MODE_PRIVATE);
        // 关键：默认存空字符串，不存“暂无菜单”
        String myFood = sp.getString("list", "");

        String[] arr;
        // 有自己添加的菜品，就只用自己的
        if (!myFood.isEmpty()) {
            arr = myFood.split(",");
        } else {
            // 自己没加任何菜，才用默认
            String defaultStr = "火锅,烧烤,麻辣烫,炸鸡,米线,牛肉面,饺子,炒饭,披萨";
            arr = defaultStr.split(",");
        }

        int idx = new Random().nextInt(arr.length);
        tvResult.setText("今天吃：" + arr[idx]);
    }
}