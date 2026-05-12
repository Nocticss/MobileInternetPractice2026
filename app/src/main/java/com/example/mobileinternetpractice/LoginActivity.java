package com.example.mobileinternetpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// 关键：导入自己的R类
import com.example.mobileinternetpractice.R;

public class LoginActivity extends AppCompatActivity {
    public static String currentUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText user = findViewById(R.id.et_user);
        EditText pwd = findViewById(R.id.et_pwd);
        Button btnLogin = findViewById(R.id.btn_login);
        Button goReg = findViewById(R.id.btn_go_reg);

        goReg.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            String u = user.getText().toString();
            String p = pwd.getText().toString();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("Users", MODE_PRIVATE);
            String realPwd = sp.getString(u, "");

            if (realPwd.equals(p)) {
                currentUser = u;
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}