package com.procter.ui.authentications.forgetpass;

public class ForgetPresenterImpl implements ForgetPresentor, ForgetInteractor.onForgetFinishedListener {
    private ForgetView forgetView;
    private ForgetInteractor forgetInteractor;

    ForgetPresenterImpl(final ForgetView forgetView, final ForgetInteractor forgetInteractor){
        this.forgetView=forgetView;
        this.forgetInteractor=forgetInteractor;

    }
    @Override
    public void onMobileNuError() {
        if (forgetView != null){
            forgetView.setMobileError();
        }
    }

    @Override
    public void onMobileVError() {
        if (forgetView != null){
            forgetView.setMobileVError();
        }
    }

    @Override
    public void onPassowrdError() {
      if (forgetView!=null){
          forgetView.setPasswordError();
      }
    }

    @Override
    public void setMobilegreatorError() {
        if (forgetView!=null){
            forgetView.setMobilegreatorError();
        }
    }

    @Override
    public void onSuccess() {
        if (forgetView != null) {
            forgetView.navigateToHome();
        }
    }

    @Override
    public void onNavigator() {
        if (forgetView != null) {
            forgetView.navigateToHome();
        }
    }



    @Override
    public void validationCondition(String mobile) {
        forgetInteractor.forgetPass(mobile,this);
    }
}
