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

        TextView tvName = findViewById(R.id.tv_name);
        Button btnLogout = findViewById(R.id.btn_logout);

        tvName.setText("当前用户：" + LoginActivity.currentUser);

        btnLogout.setOnClickListener(v -> {
            LoginActivity.currentUser = "";
            startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }
}