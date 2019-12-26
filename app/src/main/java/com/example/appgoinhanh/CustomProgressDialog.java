package com.example.appgoinhanh;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

public class CustomProgressDialog extends AlertDialog {

    public CustomProgressDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.bg_load);
    }
}
