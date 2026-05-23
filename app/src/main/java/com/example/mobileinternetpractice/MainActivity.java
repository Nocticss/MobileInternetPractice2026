package com.example.mobileinternetpractice;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileinternetpractice.utils.BgSelectDialog;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvUser, tvWeatherGlobal, tvDiceResult, tvCoinResult;
    private Button btnEat, btnWear, btnPlay, btnDice, btnCoin, btnChangeBg, btnHistory;
    private ImageView ivDice, ivCoin;
    private LinearLayout llMain, llHistoryPanel;
    public static String weather = "晴";
    public static int temp = 22;
    // 固定城市：北京，不可修改
    public static String location = "北京";
    private SharedPreferences sp;
    private SharedPreferences historySp;

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
    }

    private void initView() {
        tvUser = findViewById(R.id.tv_user);
        tvWeatherGlobal = findViewById(R.id.tv_weather_global);
        tvDiceResult = findViewById(R.id.tv_dice_result);
        tvCoinResult = findViewById(R.id.tv_coin_result);

        btnHistory = findViewById(R.id.btn_history);
        btnChangeBg = findViewById(R.id.btn_change_bg);
        llMain = findViewById(R.id.ll_main);
        llHistoryPanel = findViewById(R.id.ll_history_panel);

        ivDice = findViewById(R.id.iv_dice);
        ivCoin = findViewById(R.id.iv_coin);

        btnEat = findViewById(R.id.btn_eat);
        btnWear = findViewById(R.id.btn_wear);
        btnPlay = findViewById(R.id.btn_play);
        btnDice = findViewById(R.id.btn_dice);
        btnCoin = findViewById(R.id.btn_coin);
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

    // ===================== 骰子动画 ======================
    private void rollDice() {
        ObjectAnimator diceAnimator = ObjectAnimator.ofFloat(ivDice, "rotation", 0f, 360f);
        diceAnimator.setDuration(500);
        diceAnimator.setInterpolator(new LinearInterpolator());
        diceAnimator.setRepeatCount(1);
        diceAnimator.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Random random = new Random();
            int randomNum = random.nextInt(6) + 1;
            tvDiceResult.setText(String.valueOf(randomNum));
            Toast.makeText(MainActivity.this, "骰子点数：" + randomNum, Toast.LENGTH_SHORT).show();
            saveHistory("骰子：" + randomNum);
        }, 500);
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

    // ===================== 历史记录 ======================
    private void saveHistory(String content) {
        String old = historySp.getString("history_list", "");
        String newList = old.isEmpty() ? content : old + "\n" + content;
        historySp.edit().putString("history_list", newList).apply();
    }

    private void showHistory() {
        String history = historySp.getString("history_list", "暂无记录");
        TextView tvHistoryContent = findViewById(R.id.tv_history_content);
        tvHistoryContent.setText(history);
        llHistoryPanel.setVisibility(View.VISIBLE);
    }

    // ===================== 点击空白收起历史记录 ======================
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (llHistoryPanel.getVisibility() == View.VISIBLE) {
                llHistoryPanel.setVisibility(View.GONE);
            }
        }
        return super.onTouchEvent(event);
    }

    // ===================== 点击事件 ======================
    private void initListener() {

        // 吃什么（已修改：不读取自定义菜单，直接系统随机）
        btnEat.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "eat");
            // 强制固定北京
            intent.putExtra("city", "北京");
            startActivity(intent);
        });

        btnWear.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "wear");
            intent.putExtra("city", "北京");
            startActivity(intent);
        });

        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionActivity.class);
            intent.putExtra("type", "play");
            intent.putExtra("city", "北京");
            startActivity(intent);
        });

        btnDice.setOnClickListener(v -> rollDice());
        btnCoin.setOnClickListener(v -> flipCoin());

        // 更换背景
        btnChangeBg.setOnClickListener(v -> {
            new BgSelectDialog(MainActivity.this, drawable -> {
                llMain.setBackground(drawable);
            }).show();
        });

        // 历史记录点击显示
        btnHistory.setOnClickListener(v -> {
            if (llHistoryPanel.getVisibility() == View.GONE) {
                showHistory();
            } else {
                llHistoryPanel.setVisibility(View.GONE);
            }
        });
    }
}