package com.example.mobileinternetpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        }else{
            tvList.setText(foods.replace(",","\n"));
        }

        foodList = new LinkedList<>(Arrays.asList(foods.split(",")));
        tvList.setText(foods.replace(",", "\n"));
    }

    // 删除最后一个（可改成任意删除，这里长按安全）
    private void deleteSingleFood() {
        if (foodList.isEmpty() || foodList.get(0).equals("暂无自定义菜单")) {
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