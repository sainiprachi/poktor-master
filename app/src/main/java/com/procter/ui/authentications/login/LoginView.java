package com.procter.ui.authentications.login;

/**
 * Created by mindiii on 2/4/18.
 */

public interface LoginView {
    void setMobileError();
    void setMobileVError();
    void setPasswordError();
    void navigateToHome();
    void navigateToRegistration();
    void setMobileGreaterError();
}
