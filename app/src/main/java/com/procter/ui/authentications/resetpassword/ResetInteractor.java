package com.procter.ui.authentications.resetpassword;

public interface ResetInteractor {

    interface onResetInteractor {


        void onPassowrdError();

        void onPasswordSameError();

        void onOnfirmPasswordError();

        void onSuccess();

        void onNavigator();
        void setEmptyConfirmPassError();

        void setMobilegreatorError();





        void passEmptyError();

        void currentPassEmptyError();


    }

    void forgetPass(String password,String confirmPass, ResetInteractor.onResetInteractor listener);
}
