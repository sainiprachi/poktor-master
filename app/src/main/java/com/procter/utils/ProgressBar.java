package com.procter.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.procter.R;

public class ProgressBar {

    Dialog pDialog;
    public void showProgress(Context context) {
        // TODO Auto-generated method stub
        try {
            pDialog = new Dialog(context,R.style.ProgressBarTheme);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.dialogue_layout);
            Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.gone_visibale2);

            pDialog.findViewById(R.id.iv_for_u).setAnimation(animation2);

            pDialog.setCancelable(false);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissProgress() {
        if (pDialog != null) {
            try {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
