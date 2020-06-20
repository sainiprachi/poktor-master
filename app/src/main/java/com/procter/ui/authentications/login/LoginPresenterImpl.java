package com.procter.ui.authentications.login;

public class LoginPresenterImpl implements LoginPresenter, LoginIntractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginIntractor loginIntractor;

    public LoginPresenterImpl(LoginView loginView, LoginIntractor loginIntractor) {
        this.loginView = loginView;
        this.loginIntractor = loginIntractor;
    }

    @Override
    public void validationCondition(String username, String password) {
        loginIntractor.login(username, password, this);
    }

    @Override
    public void onDistroy() {
        loginView = null;
    }


    @Override
    public void onMobileNuError() {
        if (loginView != null) {
            loginView.setMobileError();
        }
    }

    @Override
    public void onMobileVError() {
        if (loginView != null) {
            loginView.setMobileVError();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }

    @Override
    public void onNavigator() {
        loginView.navigateToRegistration();
    }

    @Override
    public void setMobileGreaterError() {
        if (loginView != null) {
            loginView.setMobileGreaterError();
        }
    }


}
