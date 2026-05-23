package com.example.mobileinternetpractice;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileinternetpractice.api.CityApiService;
import com.example.mobileinternetpractice.model.CityResponse;
import com.example.mobileinternetpractice.model.District;
import com.example.mobileinternetpractice.utils.BgSelectDialog;
import com.example.mobileinternetpractice.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvUser, tvWeatherGlobal, tvDiceResult, tvCoinResult;
    private Button btnEat, btnWear, btnPlay, btnDice, btnCoin, btnChangeLocation, btnChangeBg, btnHistory;
    private EditText etCityName;
    private ImageView ivDice, ivCoin;
    private LinearLayout llMain;
    public static String weather = "晴";
    public static int temp = 22;
    public static String location = "北京";
    private SharedPreferences sp;
    private SharedPreferences historySp;
    private PopupWindow popupWindow;
    private TextView tvHistoryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("custom_food", MODE_PRIVATE);
        historySp = getSharedPreferences("decision_history", MODE_PRIVATE);

        initView();
        loadRealTimeWeather();
        initData();
        initListener();
        initHistoryPopup();
    }

    private void initView() {
        tvUser = findViewById(R.id.tv_user);
        tvWeatherGlobal = findViewById(R.id.tv_weather_global);
        tvDiceResult = findViewById(R.id.tv_dice_result);
        tvCoinResult = findViewById(R.id.tv_coin_result);

        etCityName = findViewById(R.id.et_city_name);
        btnHistory = findViewById(R.id.btn_history);
        btnChangeBg = findViewById(R.id.btn_change_bg);
        llMain = findViewById(R.id.ll_main);

        ivDice = findViewById(R.id.iv_dice);
        ivCoin = findViewById(R.id.iv_coin);

        btnEat = findViewById(R.id.btn_eat);
        btnWear = findViewById(R.id.btn_wear);
        btnPlay = findViewById(R.id.btn_play);
        btnDice = findViewById(R.id.btn_dice);
        btnCoin = findViewById(R.id.btn_coin);
        btnChangeLocation = findViewById(R.id.btn_change_location);
    }

    private void initData() {
        tvUser.setText("当前登录：" + LoginActivity.currentUser);
        updateWeatherUI();
    }

    private void updateWeatherUI() {
        tvWeatherGlobal.setText(location + " 实时天气：" + temp + "℃ " + weather);
    }

    private void loadRealTimeWeather() {
        WeatherUtils.getRealTimeWeather(location, new WeatherUtils.OnWeatherResultListener() {
            @Override
            public void onSuccess(String weatherStr, int tempInt) {
                weather = weatherStr;
                temp = tempInt;
                updateWeatherUI();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                updateWeatherUI();
            }
        });
    }

    // ===================== 骰子动画（已缩短时间） ======================
    private void rollDice() {
        // 转动时间从 5圈 → 1圈
        ObjectAnimator diceAnimator = ObjectAnimator.ofFloat(ivDice, "rotation", 0f, 360f);
        diceAnimator.setDuration(500); // 转动速度变快
        diceAnimator.setInterpolator(new LinearInterpolator());
        diceAnimator.setRepeatCount(1); // 只转1圈
        diceAnimator.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Random random = new Random();
            int randomNum = random.nextInt(6) + 1;
            tvDiceResult.setText(String.valueOf(randomNum));
            Toast.makeText(MainActivity.this, "骰子点数：" + randomNum, Toast.LENGTH_SHORT).show();
            saveHistory("骰子：" + randomNum);
        }, 500); // 等待时间同步缩短
    }

    // ===================== 金币动画 ======================
    private void flipCoin() {
        ObjectAnimator coinAnimator = ObjectAnimator.ofFloat(ivCoin, "rotationY", 0f, 360f);
        coinAnimator.setDuration(200);
        coinAnimator.setInterpolator(new LinearInterpolator());
        coinAnimator.setRepeatCount(1);
        coinAnimator.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Random random = new Random();
            boolean isFront = random.nextBoolean();
            String result = isFront ? "正面" : "反面";
            tvCoinResult.setText(result);
            Toast.makeText(MainActivity.this, "金币：" + result, Toast.LENGTH_SHORT).show();
            saveHistory("金币：" + result);
        }, 200);
    }

    // ===================== 历史记录弹窗 ======================
    private void initHistoryPopup() {
        View historyView = LayoutInflater.from(this).inflate(R.layout.layout_history, null);
        tvHistoryContent = historyView.findViewById(R.id.tv_history_content);
        popupWindow = new PopupWindow(
                historyView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private void saveHistory(String content) {
        String old = historySp.getString("history_list", "");
        String newList = old.isEmpty() ? content : old + "\n" + content;
        historySp.edit().putString("history_list", newList).apply();
    }

    // ===================== 点击事件 ======================
    private void initListener() {

        btnEat.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "eat");
            startActivity(intent);
        });

        btnWear.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "wear");
            startActivity(intent);
        });

        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "play");
            startActivity(intent);
        });

        btnDice.setOnClickListener(v -> rollDice());
        btnCoin.setOnClickListener(v -> flipCoin());

        // 更换城市（输入框已显示，直接使用）
        btnChangeLocation.setOnClickListener(v -> {
            String city = etCityName.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "请输入城市名", Toast.LENGTH_SHORT).show();
                return;
            }
            CityApiService api = RetrofitClient.getCityApiService();
            api.checkCity(city).enqueue(new Callback<CityResponse>() {
                @Override
                public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CityResponse body = response.body();
                        if ("1".equals(body.getStatus()) && body.getDistricts() != null && !body.getDistricts().isEmpty()) {
                            District d = body.getDistricts().get(0);
                            location = d.getName();
                            Toast.makeText(MainActivity.this, "已切换城市：" + d.getName(), Toast.LENGTH_SHORT).show();
                            loadRealTimeWeather();
                            saveHistory("城市：" + d.getName());
                        } else {
                            Toast.makeText(MainActivity.this, "城市不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CityResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "请求失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 更换背景
        btnChangeBg.setOnClickListener(v -> {
            new BgSelectDialog(MainActivity.this, drawable -> {
                llMain.setBackground(drawable);
            }).show();
        });

        // 历史记录
        btnHistory.setOnClickListener(v -> {
            String history = historySp.getString("history_list", "暂无记录");
            tvHistoryContent.setText(history);
            popupWindow.showAtLocation(llMain, Gravity.TOP | Gravity.START, 20, 80);
        });
    }
}