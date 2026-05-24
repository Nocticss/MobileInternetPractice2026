package com.example.mobileinternetpractice;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationSelectDialog extends Dialog implements View.OnClickListener {
    private EditText etLocation;
    private OnLocationConfirmListener listener;

    public interface OnLocationConfirmListener {
        void onConfirm(String location);
    }

    public LocationSelectDialog(Context context, OnLocationConfirmListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_location_select);

        // 设置弹窗宽度
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        etLocation = findViewById(R.id.et_location);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        Button btnCancel = findViewById(R.id.btn_cancel);

        // 填充当前地点
        etLocation.setText(MainActivity.LOCATION);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            String location = etLocation.getText().toString().trim();
            if (location.isEmpty()) {
                Toast.makeText(getContext(), "地点不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onConfirm(location);
            dismiss();
        } else if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }
    }
}