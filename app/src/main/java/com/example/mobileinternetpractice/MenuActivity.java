package com.example.mobileinternetpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.LinkedList;

public class MenuActivity extends AppCompatActivity {
    private TextView tvList;
    private Button btnAdd, btnClear;
    private String user = LoginActivity.currentUser;
    private LinkedList<String> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 添加全局天气
        TextView tvWeather = new TextView(this);
        // 修正1：强转父布局为LinearLayout，并添加View
        ((LinearLayout) findViewById(R.id.tv_list).getParent()).addView(tvWeather, 0);

        // 修正2：setTextSize 正确用法（sp单位）
        tvWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        // 修正3：颜色值需要 0xAARRGGBB 格式
        tvWeather.setTextColor(0xFFFFFFFF);
        tvWeather.setPadding(8, 8, 8, 8);
        tvWeather.setBackgroundColor(0xFF0066CC);
        // 修正4：动态设置宽高，使用LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tvWeather.setLayoutParams(params);
        // 修正5：Gravity需要完整类名
        tvWeather.setGravity(Gravity.CENTER);
        tvWeather.setText(MainActivity.location + " 实时天气：" + MainActivity.temp + "℃ " + MainActivity.weather);

        tvList = findViewById(R.id.tv_list);
        btnAdd = findViewById(R.id.btn_add);
        btnClear = findViewById(R.id.btn_clear);

        loadFoods();

        // 长按删除某一个菜品
        tvList.setOnLongClickListener(v -> {
            deleteSingleFood();
            return true;
        });

        btnAdd.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, AddFoodActivity.class)));
        btnClear.setOnClickListener(v -> {
            getSharedPreferences("Food_" + user, MODE_PRIVATE).edit().clear().apply();
            loadFoods();
            Toast.makeText(MenuActivity.this, "已清空全部菜单", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadFoods() {
        SharedPreferences sp = getSharedPreferences("Food_" + user, MODE_PRIVATE);
        String foods = sp.getString("list", "");
        if(foods.isEmpty()){
            tvList.setText("暂无自定义菜单");
            foodList = new LinkedList<>(); // 修正：空列表也要初始化，避免后续操作空指针
        }else{
            tvList.setText(foods.replace(",","\n"));
            foodList = new LinkedList<>(Arrays.asList(foods.split(",")));
        }
    }

    // 删除最后一个（可改成任意删除，这里长按安全）
    private void deleteSingleFood() {
        if (foodList.isEmpty()) {
            Toast.makeText(this, "没有可删除的菜品", Toast.LENGTH_SHORT).show();
            return;
        }

        foodList.removeLast();
        String newList = String.join(",", foodList);

        getSharedPreferences("Food_" + user, MODE_PRIVATE)
                .edit()
                .putString("list", newList)
                .apply();

        loadFoods();
        Toast.makeText(this, "已删除最后一个菜品", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoods();
    }
}