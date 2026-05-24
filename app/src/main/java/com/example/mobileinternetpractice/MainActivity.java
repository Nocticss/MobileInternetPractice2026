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

    private TextView tvUser, tvWeather, tvTemperature, tvDiceResult, tvCoinResult, tvHistoryContent;
    private Button btnEat, btnWear, btnPlay, btnDice, btnCoin, btnChangeBg, btnHistory, btnClearHistory;
    private ImageView ivDice, ivCoin;
    private LinearLayout llMain, llHistoryPanel;

    public static String weather = "晴";
    public static int temp = 22;
    public static final String LOCATION = "北京";

    private SharedPreferences sp;
    private SharedPreferences historySp;

    private static final int DICE_ANIM_DURATION = 500;
    private static final int COIN_ANIM_DURATION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("custom_food", MODE_PRIVATE);
        historySp = getSharedPreferences("decision_history", MODE_PRIVATE);

        initView();
        initData();
        loadRealTimeWeather();
        initListener();
    }

    private void initView() {
        tvUser = findViewById(R.id.tv_user);
        tvWeather = findViewById(R.id.tv_weather);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDiceResult = findViewById(R.id.tv_dice_result);
        tvCoinResult = findViewById(R.id.tv_coin_result);
        tvHistoryContent = findViewById(R.id.tv_history_content);

        btnEat = findViewById(R.id.btn_eat);
        btnWear = findViewById(R.id.btn_wear);
        btnPlay = findViewById(R.id.btn_play);
        btnDice = findViewById(R.id.btn_dice);
        btnCoin = findViewById(R.id.btn_coin);
        btnChangeBg = findViewById(R.id.btn_change_bg);
        btnHistory = findViewById(R.id.btn_history);
        btnClearHistory = findViewById(R.id.btn_clear_history); // ✅ 清除按钮

        ivDice = findViewById(R.id.iv_dice);
        ivCoin = findViewById(R.id.iv_coin);

        llMain = findViewById(R.id.ll_main);
        llHistoryPanel = findViewById(R.id.ll_history_panel);
    }

    private void initData() {
        if (LoginActivity.currentUser != null && !LoginActivity.currentUser.isEmpty()) {
            tvUser.setText("当前登录：" + LoginActivity.currentUser);
        } else {
            tvUser.setText("当前登录：游客");
        }
        updateWeatherUI();
        llHistoryPanel.setVisibility(View.GONE);
    }

    private void updateWeatherUI() {
        tvWeather.setText(LOCATION + " 实时天气");
        tvTemperature.setText(temp + "℃ " + weather);
    }

    private void loadRealTimeWeather() {
        WeatherUtils.getRealTimeWeather(LOCATION, new WeatherUtils.OnWeatherResultListener() {
            @Override
            public void onSuccess(String weatherStr, int tempInt) {
                runOnUiThread(() -> {
                    weather = weatherStr;
                    temp = tempInt;
                    updateWeatherUI();
                });
            }

            @Override
            public void onError(String msg) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "天气加载失败", Toast.LENGTH_SHORT).show();
                    updateWeatherUI();
                });
            }
        });
    }

    private void rollDice() {
        btnDice.setEnabled(false);
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivDice, "rotation", 0, 360f);
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(1);
        anim.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            int num = new Random().nextInt(6) + 1;
            tvDiceResult.setText(String.valueOf(num));
            saveHistory("骰子：" + num);
            Toast.makeText(this, "点数：" + num, Toast.LENGTH_SHORT).show();
            btnDice.setEnabled(true);
        }, 500);
    }

    private void flipCoin() {
        btnCoin.setEnabled(false);
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivCoin, "rotationY", 0, 360f);
        anim.setDuration(300);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(1);
        anim.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String res = new Random().nextBoolean() ? "正面" : "反面";
            tvCoinResult.setText(res);
            saveHistory("金币：" + res);
            Toast.makeText(this, "结果：" + res, Toast.LENGTH_SHORT).show();
            btnCoin.setEnabled(true);
        }, 300);
    }

    private void saveHistory(String content) {
        String old = historySp.getString("history_list", "");
        String newData = old.isEmpty() ? content : old + "\n" + content;
        historySp.edit().putString("history_list", newData).apply();
    }

    private void showHistory() {
        String data = historySp.getString("history_list", "暂无记录");
        tvHistoryContent.setText(data);
        llHistoryPanel.setVisibility(View.VISIBLE);
    }

    // ===================== ✅ 清除历史记录方法 =====================
    private void clearHistory() {
        historySp.edit().remove("history_list").apply();
        tvHistoryContent.setText("暂无记录");
        Toast.makeText(this, "历史记录已清空", Toast.LENGTH_SHORT).show();
    }
    // ==============================================================

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (llHistoryPanel.getVisibility() == View.VISIBLE) {
                llHistoryPanel.setVisibility(View.GONE);
            }
        }
        return super.onTouchEvent(event);
    }

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

        btnChangeBg.setOnClickListener(v -> {
            new BgSelectDialog(MainActivity.this, drawable -> {
                if (drawable != null) llMain.setBackground(drawable);
            }).show();
        });

        btnHistory.setOnClickListener(v -> {
            if (llHistoryPanel.getVisibility() == View.GONE) showHistory();
            else llHistoryPanel.setVisibility(View.GONE);
        });

        // ===================== ✅ 清除按钮点击事件 =====================
        btnClearHistory.setOnClickListener(v -> clearHistory());
        // ==============================================================
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null);
    }
}