package com.example.myapplication.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.lang.reflect.Field;

public class DialogUtil {
    public static void buildDialogStyle(AlertDialog alertDialog){
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
                alertDialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button button1 = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button button2 = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setTextColor(0XFF1296DB);
                button1.getPaint().setFakeBoldText(true);
                button2.setTextColor(Color.rgb(194, 194, 194));
                button2.getPaint().setFakeBoldText(true);
            }
        });
    }
    public static void dialogConfirm(String msg, String btn, Activity activity){
        new MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(btn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
