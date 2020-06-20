package com.procter.ui.authentications.resetpassword;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.fragment.BaseFragment;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.ui.authentications.login.LoginFragment;
import com.procter.utils.CusDialogProg;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ResetPasswordFragment extends BaseFragment implements ResetView, RetrofitResponseInterface, View.OnClickListener {
    private RetrofitController retrofitController;
    private ResetPresentor resetPresentor;
    private EditText edtPass, edtConfirmPass;
    private RelativeLayout llParent;
    private ImageView ivShowHide, ivShowHideReset;
    private CusDialogProg cusDialogProg;
    private boolean isShowPass = true;
    private boolean isShowPassReset = true;
    private String otp_id;

    public static ResetPasswordFragment newInstance(String otpId) {
        Bundle args = new Bundle();
        args.putString("otpId", otpId);
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.reset_password_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cusDialogProg = new CusDialogProg(mContext);
        resetPresentor = new ResetPresenterImpl(this, new ResetInteractorImpl());
        retrofitController = new RetrofitController(mContext, this);

        initView(view);
    }

    private void initView(View view) {
        edtPass = view.findViewById(R.id.edtPass);
        ivShowHide = view.findViewById(R.id.ivShowHide);
        edtConfirmPass = view.findViewById(R.id.edtConfirmPass);
        Button btnDone = view.findViewById(R.id.btnSave);
        llParent = view.findViewById(R.id.llParent);
        TextInputLayout txtPassInput = view.findViewById(R.id.txtInputPass);
        TextInputLayout txtConfirmPass = view.findViewById(R.id.txtConfirmPass);
        txtConfirmPass.setHintTextAppearance(R.style.mytext);
        txtPassInput.setHintTextAppearance(R.style.mytext);
        btnDone.setOnClickListener(this);
        assert getArguments() != null;
        otp_id = getArguments().getString("otpId");
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivShowHide = view.findViewById(R.id.ivShowHide);
        ivShowHideReset = view.findViewById(R.id.ivShowHideReset);
        ivImgBack.setOnClickListener(this);
        ivShowHide.setOnClickListener(this);
        ivShowHideReset.setOnClickListener(this);
        edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edtConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());


    }

    @Override
    public void setPasswordError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.less_password_validation));
        //MyCustomMessage.getInstance(mContext).snackbar(llParent, getString(R.string.less_password_validation));

    }

    @Override
    public void setConfirPasswordError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.enter_confirm_pass));
        //  MyCustomMessage.getInstance(mContext).snackbar(llParent, getString(R.string.enter_confirm_pass));
    }

    @Override
    public void setEmptyPassError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.enter_password));
        //  MyCustomMessage.getInstance(mContext).snackbar(llParent, getString(R.string.enter_password));
    }

    @Override
    public void setEmptyConfirmPassError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.enter_confirm_pass));
        //  MyCustomMessage.getInstance(mContext).snackbar(llParent, getString(R.string.enter_confirm_pass));
    }

    @Override
    public void onPasswordSameError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.same_pass_validate));


    }


    @Override
    public void navigateToHome() {
        if (isNetworkAvailable()) {
            callResetPass();
        } else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.check_internet));

        }
    }

    @Override
    public void passEmptyError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.enter_password));
    }

    @Override
    public void setMobilegreatorError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImgBack:
                backPress(R.id.framen);
                break;

            case R.id.btnSave:
                resetPresentor.validationCondition(edtPass.getText().toString().trim(), edtConfirmPass.getText().toString().trim());
                break;

            case R.id.ivShowHide:
                if (isShowPass) {
                    ivShowHide.setImageResource(R.drawable.ic_hide);
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPass = false;

                } else {
                    ivShowHide.setImageResource(R.drawable.ic_view);
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPass = true;

                }

                break;


            case R.id.ivShowHideReset:
                if (isShowPassReset) {
                    ivShowHideReset.setImageResource(R.drawable.ic_hide);
                    edtConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassReset = false;

                } else {
                    ivShowHideReset.setImageResource(R.drawable.ic_view);
                    edtConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPassReset = true;

                }


        }

    }

    private void callResetPass() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        String pass = edtPass.getText().toString().trim();
        String confirmpass = edtConfirmPass.getText().toString().trim();
        myResponse = apiService.resetPassword(otp_id, pass, confirmpass);
        retrofitController.callRetrofitApi(myResponse, 1);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("true")) {
                String message = jsonObject.getString("message");
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);
                replaceFragment(R.id.frame, new LoginFragment(),"LoginFragment");
            } else {
                String message = jsonObject.getString("message");
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);

            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
