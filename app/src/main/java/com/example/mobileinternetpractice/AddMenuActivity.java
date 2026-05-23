package com.example.mobileinternetpractice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class AddMenuActivity extends AppCompatActivity {
    private EditText etMenu;
    private Button btnSave;
    private String type; // wear/play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        // 获取类型
        type = getIntent().getStringExtra("type");

        etMenu = findViewById(R.id.et_menu);
        btnSave = findViewById(R.id.btn_save);

        // 设置标题
        if ("wear".equals(type)) {
            setTitle("添加穿搭");
            etMenu.setHint("输入穿搭名称（如：卫衣）");
        } else if ("play".equals(type)) {
            setTitle("添加游玩项目");
            etMenu.setHint("输入游玩名称（如：看电影）");
        }

        btnSave.setOnClickListener(v -> {
            String content = etMenu.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            LinkedList<String> menuList;
            if ("wear".equals(type)) {
                menuList = CustomMenuManager.getWearMenu(this, LoginActivity.currentUser);
            } else {
                menuList = CustomMenuManager.getPlayMenu(this, LoginActivity.currentUser);
            }

            menuList.add(content);

            if ("wear".equals(type)) {
                CustomMenuManager.saveWearMenu(this, LoginActivity.currentUser, menuList);
            } else {
                CustomMenuManager.savePlayMenu(this, LoginActivity.currentUser, menuList);
            }

            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}