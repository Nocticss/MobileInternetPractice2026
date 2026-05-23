package com.example.mobileinternetpractice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddFoodActivity extends AppCompatActivity {
    EditText etFood;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        etFood = findViewById(R.id.et_food);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v->{
            String food = etFood.getText().toString();
            if(food.isEmpty()){
                Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sp = getSharedPreferences("Food_"+LoginActivity.currentUser,0);
            String old = sp.getString("list","");
            String newList = old.isEmpty() ? food : old + "," + food;
            sp.edit().putString("list",newList).apply();
            Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}