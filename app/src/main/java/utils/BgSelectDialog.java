package com.example.mobileinternetpractice.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.mobileinternetpractice.R;

public class BgSelectDialog {
    private final Context mContext;
    private final OnBgSelectedListener mListener;
    private final int[] bgResIds = {
            R.drawable.bg_1,
            R.drawable.bg_2,
            R.drawable.bg_3
    };

    public interface OnBgSelectedListener {
        void onBgSelected(Drawable bgDrawable);
    }

    public BgSelectDialog(Context context, OnBgSelectedListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void show() {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_bg_select);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        ImageView ivBg1 = dialog.findViewById(R.id.iv_bg_1);
        ImageView ivBg2 = dialog.findViewById(R.id.iv_bg_2);
        ImageView ivBg3 = dialog.findViewById(R.id.iv_bg_3);

        ivBg1.setImageResource(bgResIds[0]);
        ivBg2.setImageResource(bgResIds[1]);
        ivBg3.setImageResource(bgResIds[2]);

        ivBg1.setOnClickListener(v -> {
            mListener.onBgSelected(mContext.getResources().getDrawable(bgResIds[0], mContext.getTheme()));
            dialog.dismiss();
        });
        ivBg2.setOnClickListener(v -> {
            mListener.onBgSelected(mContext.getResources().getDrawable(bgResIds[1], mContext.getTheme()));
            dialog.dismiss();
        });
        ivBg3.setOnClickListener(v -> {
            mListener.onBgSelected(mContext.getResources().getDrawable(bgResIds[2], mContext.getTheme()));
            dialog.dismiss();
        });

        dialog.show();
    }
}