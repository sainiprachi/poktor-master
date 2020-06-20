package com.procter.ui.authentications.resetpassword;

import android.text.TextUtils;

public class ResetInteractorImpl implements ResetInteractor {
    @Override
    public void forgetPass(String password, String confirmPass, onResetInteractor listener) {
       if (!password.equals(confirmPass)){
           listener.onPasswordSameError();
           return;

       }

        if (TextUtils.isEmpty(password)){
            listener.passEmptyError();
            return;
        }
        if (TextUtils.isEmpty(confirmPass)){
            listener.currentPassEmptyError();
            return;
        }



        if (password.length() <6){
            listener.onPassowrdError();
            return;
        }




        listener.onSuccess();


    }
}
