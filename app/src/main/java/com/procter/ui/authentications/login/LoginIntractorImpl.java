package com.procter.ui.authentications.login;


public class LoginIntractorImpl implements LoginIntractor {

    @Override
    public void login(final String mobilenu, final String password, final OnLoginFinishedListener listener) {

                if (mobilenu.equals("")) {
                    listener.onMobileNuError();
                    return;
                }

                if (mobilenu.length()<10){
                    listener.onMobileVError();
                    return;
                }


        if (mobilenu.length()>10){
            listener.setMobileGreaterError();
            return;
        }

                if (password.equals("")) {
                    listener.onPasswordError();
                    return;
                }

        listener.onSuccess();
    }
}
