package com.procter.ui.authentications.forgetpass;

public class ForgetInteractorImpl implements ForgetInteractor {
    @Override
    public void forgetPass(String mobilenu, onForgetFinishedListener listener) {
        if (mobilenu.equals("")) {
            listener.onMobileNuError();
            return;
        }

        if (mobilenu.length()<10){
            listener.onMobileVError();
            return;
        }

        if (mobilenu.length()>10){
            listener.setMobilegreatorError();
            return;
        }

        listener.onSuccess();
    }



}
