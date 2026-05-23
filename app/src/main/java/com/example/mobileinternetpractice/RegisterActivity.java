package com.example.mobileinternetpractice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etUser,etPwd,etPwd2;
    Button btnReg,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUser = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        etPwd2 = findViewById(R.id.et_pwd2);
        btnReg = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_to_login);

        btnReg.setOnClickListener(v->{
            String u = etUser.getText().toString();
            String p = etPwd.getText().toString();
            String p2 = etPwd2.getText().toString();
            if(u.isEmpty()||p.isEmpty()||p2.isEmpty()){
                Toast.makeText(this,"不能为空",Toast.LENGTH_SHORT).show();
            }else if(!p.equals(p2)){
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            }else{
                getSharedPreferences("User",0).edit().putString(u,p).apply();
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnLogin.setOnClickListener(v->finish());
    }
}