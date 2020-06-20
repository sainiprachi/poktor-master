package com.procter.ui.authentications.forgetpass;

public interface ForgetInteractor {

    interface onForgetFinishedListener {

        void onMobileNuError();
        void onMobileVError();

        void onPassowrdError();

        void setMobilegreatorError();

        void onSuccess();

        void onNavigator();
    }

    void forgetPass(String mobilenu, onForgetFinishedListener listener);
}
