package com.example.mobileinternetpractice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class BackgroundSelectDialog extends Dialog implements View.OnClickListener {
    private LinearLayout llBgList;
    private OnBgSelectListener listener;
    // 内置柔和背景图（可替换为自己的资源）
    private final List<Integer> bgResList = new ArrayList<Integer>() {{
        add(R.drawable.bg_soft1);
        add(R.drawable.bg_soft2);
        add(R.drawable.bg_soft3);
    }};

    public interface OnBgSelectListener {
        void onSelect(Drawable drawable);
    }

    public BackgroundSelectDialog(Context context, OnBgSelectListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_background_select);

        // 设置弹窗宽度
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        llBgList = findViewById(R.id.ll_bg_list);
        Button btnCancel = findViewById(R.id.btn_cancel);

        // 加载内置背景图
        for (int resId : bgResList) {
            ImageView ivBg = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    200, 200
            );
            params.setMargins(10, 0, 10, 0);
            ivBg.setLayoutParams(params);
            ivBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivBg.setTag(resId);

            Glide.with(getContext())
                    .load(resId)
                    .into(ivBg);

            ivBg.setOnClickListener(v -> {
                int selectRes = (int) v.getTag();
                Glide.with(getContext())
                        .asBitmap()
                        .load(selectRes)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                listener.onSelect(new BitmapDrawable(getContext().getResources(), resource));
                                dismiss();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {}
                        });
            });

            llBgList.addView(ivBg);
        }

        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }
    }
}
