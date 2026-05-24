package com.example.mobileinternetpractice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserCenterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        // 这里必须和你 XML 里的 id 完全一致
        TextView tvWeather = findViewById(R.id.tv_weather);
        TextView tvInfo = findViewById(R.id.tv_info);
        Button btnLogout = findViewById(R.id.btn_logout);

        // 设置天气文字
        tvWeather.setText(MainActivity.LOCATION + " 实时天气：" + MainActivity.temp + "℃ " + MainActivity.weather);

        // 设置用户信息
        tvInfo.setText("当前用户：" + LoginActivity.currentUser);

        // 退出登录（修正了 Intent 的错误语法）
        btnLogout.setOnClickListener(v -> {
            LoginActivity.currentUser = "";
            startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }
}