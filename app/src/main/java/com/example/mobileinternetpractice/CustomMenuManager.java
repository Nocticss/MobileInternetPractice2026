package com.example.mobileinternetpractice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.LinkedList;

public class CustomMenuManager {
    // 区分不同类型的菜单
    private static final String SP_NAME_WEAR = "WearMenu_";
    private static final String SP_NAME_PLAY = "PlayMenu_";
    private static final String KEY_LIST = "list";

    // 获取穿什么菜单
    public static LinkedList<String> getWearMenu(Context context, String user) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_WEAR + user, Context.MODE_PRIVATE);
        String menu = sp.getString(KEY_LIST, "");
        if (menu.isEmpty()) {
            return new LinkedList<>();
        }
        return new LinkedList<>(Arrays.asList(menu.split(",")));
    }

    // 保存穿什么菜单
    public static void saveWearMenu(Context context, String user, LinkedList<String> menu) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_WEAR + user, Context.MODE_PRIVATE);
        String menuStr = String.join(",", menu);
        sp.edit().putString(KEY_LIST, menuStr).apply();
    }

    // 获取玩什么菜单
    public static LinkedList<String> getPlayMenu(Context context, String user) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_PLAY + user, Context.MODE_PRIVATE);
        String menu = sp.getString(KEY_LIST, "");
        if (menu.isEmpty()) {
            return new LinkedList<>();
        }
        return new LinkedList<>(Arrays.asList(menu.split(",")));
    }

    // 保存玩什么菜单
    public static void savePlayMenu(Context context, String user, LinkedList<String> menu) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME_PLAY + user, Context.MODE_PRIVATE);
        String menuStr = String.join(",", menu);
        sp.edit().putString(KEY_LIST, menuStr).apply();
    }

    // 清空指定类型菜单
    public static void clearMenu(Context context, String user, String type) {
        String spName = "";
        if ("wear".equals(type)) {
            spName = SP_NAME_WEAR + user;
        } else if ("play".equals(type)) {
            spName = SP_NAME_PLAY + user;
        }
        if (!spName.isEmpty()) {
            context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().clear().apply();
        }
    }
}