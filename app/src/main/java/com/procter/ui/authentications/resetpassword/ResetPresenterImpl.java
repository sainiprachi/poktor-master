package com.procter.ui.authentications.resetpassword;

public class ResetPresenterImpl implements ResetPresentor,ResetInteractor.onResetInteractor {

    private ResetView resetView;
    private ResetInteractor resetInteractor;

    public ResetPresenterImpl(final ResetView resetView, final ResetInteractor resetInteractor){
        this.resetView=resetView;
        this.resetInteractor=resetInteractor ;

    }
    @Override
    public void validationCondition(String password, String confirmpassword) {
        resetInteractor.forgetPass(password,confirmpassword,this);
    }

    @Override
    public void onPassowrdError() {
        if (resetView != null){
            resetView.setPasswordError();
        }
    }

    @Override
    public void onPasswordSameError() {

        if (resetView!=null){
            resetView.onPasswordSameError();
        }

    }

    @Override
    public void onOnfirmPasswordError() {
       if (resetView!=null){
           resetView.setConfirPasswordError();
       }
    }

    @Override
    public void onSuccess() {
      if (resetView!=null){
          resetView.navigateToHome();
      }
    }

    @Override
    public void onNavigator() {
        if (resetView!=null){
            resetView.navigateToHome();
        }
    }

    @Override
    public void setEmptyConfirmPassError() {
       if (resetView!=null){
           resetView.setEmptyConfirmPassError();
       }
    }

    @Override
    public void setMobilegreatorError() {
     if (resetView!=null){
         resetView.setMobilegreatorError();
     }
    }

    @Override
    public void passEmptyError() {
      if (resetView!=null){
          resetView.passEmptyError();
      }
    }

    @Override
    public void currentPassEmptyError() {
       if (resetView!=null){
           resetView.setConfirPasswordError();
       }
    }
}
