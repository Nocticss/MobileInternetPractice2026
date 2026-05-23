package com.example.mobileinternetpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public static String currentUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 绑定控件
        EditText user = findViewById(R.id.et_user);
        EditText pwd = findViewById(R.id.et_pwd);
        Button btnLogin = findViewById(R.id.btn_login);
        Button goReg = findViewById(R.id.btn_go_reg);

        // 跳转到注册页面
        goReg.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // 登录逻辑
        btnLogin.setOnClickListener(v -> {
            String u = user.getText().toString().trim();
            String p = pwd.getText().toString().trim();

            // 空值校验
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 修复：SharedPreferences键名和注册页保持一致（User）
            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
            String realPwd = sp.getString(u, "");

            if (realPwd.equals(p)) {
                // 登录成功，保存当前用户并跳转到主页面
                currentUser = u;
                startActivity(new Intent(this, MainActivity.class));
                finish(); // 关闭登录页，防止返回
            } else {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}