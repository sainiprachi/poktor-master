package com.procter.ui.authentications.login;

public interface LoginIntractor {

    interface OnLoginFinishedListener {

        void onMobileNuError();

        void onMobileVError();

        void onPasswordError();

        void onSuccess();

        void onNavigator();

        void setMobileGreaterError();
    }

    void login(String username, String password, OnLoginFinishedListener listener);
}
