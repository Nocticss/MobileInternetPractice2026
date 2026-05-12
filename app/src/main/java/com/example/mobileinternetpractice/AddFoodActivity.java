package com.example.mobileinternetpractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// 必须导入自己的R类
import com.example.mobileinternetpractice.R;
// 必须导入LoginActivity
import com.example.mobileinternetpractice.LoginActivity;

public class AddFoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        EditText etFood = findViewById(R.id.et_food);
        Button btnSave = findViewById(R.id.btn_save);
        String user = LoginActivity.currentUser;

        btnSave.setOnClickListener(v -> {
            String food = etFood.getText().toString().trim();
            if (food.isEmpty()) {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences("Food_" + user, MODE_PRIVATE);
            String old = sp.getString("list", "");
            String newList = old.isEmpty() ? food : old + "," + food;
            sp.edit().putString("list", newList).apply();

            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}