package com.example.mobileinternetpractice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FunctionActivity extends AppCompatActivity {

    private TextView tvWeatherFunc, tvTitle, tvResult;
    private Button btnRandom, btnMenu, btnCenter;
    private String type; // eat/wear/play

    // 推荐数据（默认）
    private List<String> eatSunny;
    private List<String> eatRainy;
    private List<String> wearHot;
    private List<String> wearWarm;
    private List<String> wearCool;
    private List<String> wearCold;
    private List<String> playSunny;
    private List<String> playRainy;
    private List<String> playHot;
    private List<String> playCold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        initView();
        getIntentData();
        initData();
        initListener();
    }

    // 初始化控件
    private void initView() {
        tvWeatherFunc = findViewById(R.id.tv_weather_func);
        tvTitle = findViewById(R.id.tv_title);
        tvResult = findViewById(R.id.tv_result);
        btnRandom = findViewById(R.id.btn_random);
        btnMenu = findViewById(R.id.btn_menu);
        btnCenter = findViewById(R.id.btn_center);
    }

    // 获取传递的类型
    private void getIntentData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }

    // 初始化数据
    private void initData() {
        // 天气显示（固定城市为北京）
        tvWeatherFunc.setText("北京 " + MainActivity.weather + " " + MainActivity.temp + "℃");

        // 初始化吃的推荐列表
        eatSunny = new ArrayList<>();
        eatSunny.add("烧烤");
        eatSunny.add("奶茶");
        eatSunny.add("火锅");
        eatSunny.add("日料");

        eatRainy = new ArrayList<>();
        eatRainy.add("麻辣烫");
        eatRainy.add("螺蛳粉");
        eatRainy.add("泡面");
        eatRainy.add("煲仔饭");

        // 初始化穿的推荐列表
        wearHot = new ArrayList<>();
        wearHot.add("短袖+短裤");
        wearHot.add("连衣裙");
        wearHot.add("T恤+牛仔");

        wearWarm = new ArrayList<>();
        wearWarm.add("长袖+牛仔裤");
        wearWarm.add("薄卫衣");
        wearWarm.add("衬衫+休闲裤");

        wearCool = new ArrayList<>();
        wearCool.add("毛衣+外套");
        wearCool.add("风衣+长裤");
        wearCool.add("卫衣+阔腿裤");

        wearCold = new ArrayList<>();
        wearCold.add("羽绒服+保暖裤");
        wearCold.add("厚毛衣+大衣");
        wearCold.add("棉衣+加绒裤");

        // 初始化玩的推荐列表
        playSunny = new ArrayList<>();
        playSunny.add("公园散步");
        playSunny.add("爬山");
        playSunny.add("野餐");

        playRainy = new ArrayList<>();
        playRainy.add("看电影");
        playRainy.add("逛商场");
        playRainy.add("宅家追剧");

        playHot = new ArrayList<>();
        playHot.add("去游泳馆");
        playHot.add("逛空调房商场");
        playHot.add("去书店");

        playCold = new ArrayList<>();
        playCold.add("泡温泉");
        playCold.add("室内桌游");
        playCold.add("去咖啡馆");

        // 设置标题
        if ("eat".equals(type)) {
            tvTitle.setText("吃什么");
        } else if ("wear".equals(type)) {
            tvTitle.setText("穿什么");
        } else if ("play".equals(type)) {
            tvTitle.setText("玩什么");
        }
    }

    // 初始化点击事件
    private void initListener() {
        // 随机推荐按钮
        btnRandom.setOnClickListener(v -> {
            if ("eat".equals(type)) {
                randomEat();
            } else if ("wear".equals(type)) {
                randomWear();
            } else if ("play".equals(type)) {
                randomPlay();
            }
        });

        // 我的菜单（统一跳转到AddMenuActivity，支持eat/wear/play）
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(FunctionActivity.this, AddMenuActivity.class);
            intent.putExtra("type", type); // 传递类型（eat/wear/play）
            startActivity(intent);
        });

        // 个人中心
        btnCenter.setOnClickListener(v -> startActivity(new Intent(this, UserCenterActivity.class)));
    }

    // 随机推荐吃的（和穿/玩逻辑完全统一，优先自定义菜单）
    private void randomEat() {
        // 优先使用自定义菜单（修复错误：改为正确的getEatMenu方法）
        List<String> customMenu = CustomMenuManager.getEatMenu(this, LoginActivity.currentUser);
        if (!customMenu.isEmpty()) {
            String result = customMenu.get(new Random().nextInt(customMenu.size()));
            tvResult.setText("🍜 推荐吃：" + result);
            return;
        }

        // 默认推荐（按天气）
        List<String> recommendList;
        if ("雨".equals(MainActivity.weather) || "阴".equals(MainActivity.weather)) {
            recommendList = eatRainy;
        } else {
            recommendList = eatSunny;
        }
        String result = recommendList.get(new Random().nextInt(recommendList.size()));
        tvResult.setText("🍜 推荐吃：" + result);
    }

    // 随机推荐穿的（优先自定义菜单）
    private void randomWear() {
        // 优先使用自定义菜单
        List<String> customMenu = CustomMenuManager.getWearMenu(this, LoginActivity.currentUser);
        if (!customMenu.isEmpty()) {
            String result = customMenu.get(new Random().nextInt(customMenu.size()));
            tvResult.setText("👕 推荐穿：" + result);
            return;
        }

        // 默认推荐（按温度）
        List<String> recommendList;
        if (MainActivity.temp > 28) {
            recommendList = wearHot;
        } else if (MainActivity.temp > 18 && MainActivity.temp <= 28) {
            recommendList = wearWarm;
        } else if (MainActivity.temp > 8 && MainActivity.temp <= 18) {
            recommendList = wearCool;
        } else {
            recommendList = wearCold;
        }
        String result = recommendList.get(new Random().nextInt(recommendList.size()));
        tvResult.setText("👕 推荐穿：" + result);
    }

    // 随机推荐玩的（优先自定义菜单）
    private void randomPlay() {
        // 优先使用自定义菜单
        List<String> customMenu = CustomMenuManager.getPlayMenu(this, LoginActivity.currentUser);
        if (!customMenu.isEmpty()) {
            String result = customMenu.get(new Random().nextInt(customMenu.size()));
            tvResult.setText("🎮 推荐玩：" + result);
            return;
        }

        // 默认推荐（按天气/温度）
        List<String> recommendList;
        if ("晴".equals(MainActivity.weather)) {
            if (MainActivity.temp > 28) {
                recommendList = playHot;
            } else if (MainActivity.temp < 10) {
                recommendList = playCold;
            } else {
                recommendList = playSunny;
            }
        } else if ("雨".equals(MainActivity.weather) || "阴".equals(MainActivity.weather)) {
            recommendList = playRainy;
        } else {
            recommendList = new ArrayList<>();
            recommendList.addAll(playSunny);
            recommendList.addAll(playRainy);
        }
        String result = recommendList.get(new Random().nextInt(recommendList.size()));
        tvResult.setText("🎮 推荐玩：" + result);
    }
}