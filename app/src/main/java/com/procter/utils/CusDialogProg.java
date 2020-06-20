package com.procter.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.procter.R;

public class CusDialogProg extends Dialog {


    private RelativeLayout rl;


    public CusDialogProg(Context context) {
        super(context, R.style.ProgressBarTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialogue_layout);
        AppCompatImageView appCompatImageView = this.findViewById(R.id.iv_for_u);
        rl = this.findViewById(R.id.rl);

        Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.gone_visibale);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.gone_visibale2);
        appCompatImageView.setAnimation(animation2);

    }

    public void visibilty(Boolean value){

        if (value){
            rl.setVisibility(View.VISIBLE);
        }else {
            rl.setVisibility(View.GONE);
        }

    }


}
