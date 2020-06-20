package com.procter.Singleton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.procter.R;

public class MyCustomMessage {

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static MyCustomMessage instance;

    /**
     * @param context as parent context
     */
    private MyCustomMessage(Context context) {
        this.context = context;
    }

    /**
     * @param context as parent contex
     * @return instance
     */
    public synchronized static MyCustomMessage getInstance(Context context) {
        if (instance == null) {
            instance = new MyCustomMessage(context);
        }
        return instance;
    }

    public void showCustomAlert(String title, String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.custom_alert_layout, null);
        TextView msgTv = layout.findViewById(R.id.tv_msg);

        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showLogoutAlert(String title, String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.custom_alert_layout, null);

        TextView msgTv = layout.findViewById(R.id.tv_msg);

        msgTv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void customToast(final String msg) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView tv_msg = layout.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, (int) context.getResources().getDimension(R.dimen.fab_margin));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showToast(String message, int len) {
        Toast.makeText(context, message, len).show();
    }

    public void snackbar(View coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView =  sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setGravity(Gravity.CENTER);
        snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));
        sbView.setBackgroundColor(Color.parseColor("#F37474"));
        snackbar.show();
    }


}
