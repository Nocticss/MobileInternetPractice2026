package com.example.mobileinternetpractice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText user = findViewById(R.id.et_user);
        EditText pwd = findViewById(R.id.et_pwd);
        EditText pwd2 = findViewById(R.id.et_pwd2);
        Button btnReg = findViewById(R.id.btn_reg);
        Button goLogin = findViewById(R.id.btn_go_login);

        btnReg.setOnClickListener(v->{
            String u = user.getText().toString();
            String p = pwd.getText().toString();
            String p2 = pwd2.getText().toString();

            if(u.isEmpty()||p.isEmpty()){
                Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!p.equals(p2)){
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("Users",0);
            if(sp.contains(u)){
                Toast.makeText(this,"账号已存在",Toast.LENGTH_SHORT).show();
                return;
            }

            sp.edit().putString(u,p).apply();
            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            finish();
        });

        goLogin.setOnClickListener(v->finish());
    }
}