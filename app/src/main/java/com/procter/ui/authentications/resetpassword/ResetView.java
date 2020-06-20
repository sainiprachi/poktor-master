package com.procter.ui.authentications.resetpassword;

public interface ResetView {
    void setPasswordError();
    void setConfirPasswordError();
    void setEmptyPassError();
    void setEmptyConfirmPassError();
    void  onPasswordSameError();
    void navigateToHome();
    void passEmptyError();

    void setMobilegreatorError();


}
