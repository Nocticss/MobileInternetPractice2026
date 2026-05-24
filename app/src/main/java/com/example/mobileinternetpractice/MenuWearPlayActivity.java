package com.example.mobileinternetpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class MenuWearPlayActivity extends AppCompatActivity {
    private TextView tvList;
    private Button btnAdd, btnClear;
    private String type; // wear / play / eat
    private String user;
    private LinkedList<String> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wear_play);

        // 获取类型
        type = getIntent().getStringExtra("type");
        user = LoginActivity.currentUser;

        // 添加天气栏
        TextView tvWeather = new TextView(this);
        ((LinearLayout) findViewById(R.id.tv_list).getParent()).addView(tvWeather, 0);
        tvWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tvWeather.setTextColor(0xFFFFFFFF);
        tvWeather.setPadding(8, 8, 8, 8);
        tvWeather.setBackgroundColor(0xFF0066CC);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tvWeather.setLayoutParams(params);
        tvWeather.setGravity(Gravity.CENTER);
        tvWeather.setText(MainActivity.LOCATION + " 实时天气：" + MainActivity.temp + "℃ " + MainActivity.weather);

        // 初始化控件
        tvList = findViewById(R.id.tv_list);
        btnAdd = findViewById(R.id.btn_add);
        btnClear = findViewById(R.id.btn_clear);

        // ===================== 修复：加入吃什么的标题 =====================
        if ("wear".equals(type)) {
            setTitle("我的穿搭菜单");
        } else if ("play".equals(type)) {
            setTitle("我的游玩菜单");
        } else if ("eat".equals(type)) {
            setTitle("我的美食菜单");
        }
        // ===============================================================

        // 加载菜单
        loadMenu();

        // 长按删除最后一个
        tvList.setOnLongClickListener(v -> {
            deleteLastItem();
            return true;
        });

        // 添加按钮
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMenuActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        });

        // 清空按钮
        btnClear.setOnClickListener(v -> {
            CustomMenuManager.clearMenu(this, user, type);
            loadMenu();
            Toast.makeText(this, "已清空菜单", Toast.LENGTH_SHORT).show();
        });
    }

    // ===================== 核心修复：加入 eat 加载逻辑 =====================
    // 加载菜单
    private void loadMenu() {
        if ("wear".equals(type)) {
            menuList = CustomMenuManager.getWearMenu(this, user);
        } else if ("play".equals(type)) {
            menuList = CustomMenuManager.getPlayMenu(this, user);
        } else if ("eat".equals(type)) {
            // 这里补上了 吃什么 的菜单加载！
            menuList = CustomMenuManager.getEatMenu(this, user);
        }

        if (menuList.isEmpty()) {
            tvList.setText("暂无自定义菜单");
        } else {
            tvList.setText(String.join("\n", menuList));
        }
    }
    // ====================================================================

    // ===================== 修复：加入 eat 删除保存逻辑 =====================
    // 删除最后一个
    private void deleteLastItem() {
        if (menuList.isEmpty()) {
            Toast.makeText(this, "没有可删除的内容", Toast.LENGTH_SHORT).show();
            return;
        }

        menuList.removeLast();

        if ("wear".equals(type)) {
            CustomMenuManager.saveWearMenu(this, user, menuList);
        } else if ("play".equals(type)) {
            CustomMenuManager.savePlayMenu(this, user, menuList);
        } else if ("eat".equals(type)) {
            // 这里补上了 吃什么 的删除保存
            CustomMenuManager.saveEatMenu(this, user, menuList);
        }

        loadMenu();
        Toast.makeText(this, "已删除最后一项", Toast.LENGTH_SHORT).show();
    }
    // ====================================================================

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }
}