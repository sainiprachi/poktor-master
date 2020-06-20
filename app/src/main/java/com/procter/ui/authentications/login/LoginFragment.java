package com.procter.ui.authentications.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.fragment.BaseFragment;
import com.procter.fragment.SendOtpFragment;
import com.procter.model.UserInfo;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.ui.authentications.forgetpass.ForgetPasswordFragment;
import com.procter.utils.CusDialogProg;
import com.procter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class LoginFragment extends BaseFragment implements View.OnClickListener, RetrofitResponseInterface, LoginView {
    private RetrofitController retrofitController;
    private LoginPresenter loginPresenter;
    private EditText edtMobileNu, edtPass;
    private CusDialogProg cusDialogProg;
    private boolean isShowPass = true;
    private TextView txtSignIn;
    private TextView txtCreateAccount;
    private View viewCreateAccount;
    private View viewSignIn;
    private LinearLayout llLoginView;
    private LinearLayout llSignUpView;
    private EditText edOtp;

    @Override
    protected int getLayoutResource() {
        return R.layout.login_signup_fragment;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginPresenter = new LoginPresenterImpl(this, new LoginIntractorImpl());
        cusDialogProg = new CusDialogProg(mContext);
        retrofitController = new RetrofitController(mContext, this);
        initView(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        RelativeLayout rlParent = view.findViewById(R.id.framen);

        edtMobileNu = view.findViewById(R.id.edtMobile);
        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        edtPass = view.findViewById(R.id.edtPass);
        edOtp = view.findViewById(R.id.edOtp);
        TextInputLayout txtInputMobile = view.findViewById(R.id.txtInputMobile);
        TextInputLayout txtInputOtp = view.findViewById(R.id.txtInputOtp);
        TextInputLayout txtInputPass = view.findViewById(R.id.txtInputPass);
        txtInputMobile.setHintTextAppearance(R.style.mytext);
        txtInputPass.setHintTextAppearance(R.style.mytext);
        txtInputOtp.setHintTextAppearance(R.style.mytext);
        Utils.setFontFamilyForTextInputLayout(Objects.requireNonNull(getContext()), "poppins_regular.ttf", txtInputMobile);
        Utils.setFontFamilyForTextInputLayout(getContext(), "poppins_regular.ttf", txtInputPass);
        Utils.setFontFamilyForTextInputLayout(getContext(), "poppins_regular.ttf", txtInputOtp);
        rlParent.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        cusDialogProg = new CusDialogProg(mContext);
        edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout llSignIn = view.findViewById(R.id.llSignIn);
        llSignIn.setOnClickListener(this);
        txtSignIn = view.findViewById(R.id.txtSignIn);
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.poppins_regular);
        Objects.requireNonNull(txtInputMobile.getEditText()).setTypeface(typeface);
        Objects.requireNonNull(txtInputPass.getEditText()).setTypeface(typeface);

        txtCreateAccount = view.findViewById(R.id.txtCreateAccount);
        viewCreateAccount = view.findViewById(R.id.viewCreateAccount);
        llLoginView = view.findViewById(R.id.llLoginView);
        llSignUpView = view.findViewById(R.id.llSignUpView);
        TextView txtSendOtp = view.findViewById(R.id.txtSendOtp);
        txtSendOtp.setOnClickListener(this);
        viewSignIn = view.findViewById(R.id.viewSignIn);
        LinearLayout llCreateAccount = view.findViewById(R.id.llCreateAccount);
        llCreateAccount.setOnClickListener(this);
        TextView txtForget = view.findViewById(R.id.txtForget);
        txtForget.setOnClickListener(this);

        edtMobileNu.setFocusableInTouchMode(false);
        edtMobileNu.setFocusable(false);
        edtMobileNu.setFocusableInTouchMode(true);
        edtMobileNu.setFocusable(true);

        edtPass.setFocusableInTouchMode(false);
        edtPass.setFocusable(false);
        edtPass.setFocusableInTouchMode(true);
        edtPass.setFocusable(true);
        view.findViewById(R.id.framen).setOnFocusChangeListener((v, hasFocus) -> ((InputMethodManager) Objects.requireNonNull(mContext.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(edtMobileNu.getWindowToken(), 0));

        edOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    if (isNetworkAvailable()) {
                        if (checkValidation()) {
                            callGenereateOTP();
                        }

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", getString(R.string.check_internet));
                    }

                }
                return false;
            }
        });



     /*   edtMobileNu.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    edtMobileNu.clearFocus();
                    edtPass.requestFocus();
                    return true;
                }
                return false;
            }
        });

        edtMobileNu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == edtMobileNu) {
                    if (hasFocus) {
                        // Open keyboard
                        ((InputMethodManager) Objects.requireNonNull(mContext.getSystemService(Context.INPUT_METHOD_SERVICE))).showSoftInput(edtMobileNu, InputMethodManager.SHOW_FORCED);
                    } else {
                        // Close keyboard
                        ((InputMethodManager) Objects.requireNonNull(mContext.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(edtMobileNu.getWindowToken(), 0);
                    }
                }
            }
        });

        edOtp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == edOtp) {
                    if (hasFocus) {
                        // Open keyboard
                        ((InputMethodManager) Objects.requireNonNull(mContext.getSystemService(Context.INPUT_METHOD_SERVICE))).showSoftInput(edOtp, InputMethodManager.SHOW_FORCED);
                    } else {
                        // Close keyboard
                        ((InputMethodManager) Objects.requireNonNull(mContext.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(edOtp.getWindowToken(), 0);
                    }
                }
            }
        });

*/
    }

    @Override
    public void setMobileError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.enter_mobile));
    }

    @Override
    public void setMobileVError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.less_than));

    }


    @Override
    public void setPasswordError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.enter_password));
    }

    @Override
    public void navigateToHome() {
        if (isNetworkAvailable()) {
            callLoginApi();
        } else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.check_internet));
        }
    }

    @Override
    public void navigateToRegistration() {

    }

    @Override
    public void setMobileGreaterError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Mobile nu should not be greator than 10 digits.");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                loginPresenter.validationCondition(edtMobileNu.getText().toString().trim(), edtPass.getText().toString().trim());
                break;


            case R.id.llSignIn:
                txtSignIn.setTextColor(getResources().getColor(android.R.color.black));
                txtSignIn.setTextSize(20f);
                txtCreateAccount.setPadding(0, -3, 0, 0);
                txtCreateAccount.setTextSize(19f);
                txtCreateAccount.setTextColor(getResources().getColor(R.color.gray2));
                viewCreateAccount.setVisibility(View.GONE);
                llLoginView.setVisibility(View.VISIBLE);
                llSignUpView.setVisibility(View.GONE);
                viewSignIn.setVisibility(View.VISIBLE);
                break;

            case R.id.llCreateAccount:
                txtCreateAccount.setTextColor(getResources().getColor(android.R.color.black));
                txtCreateAccount.setTextSize(20f);
                txtCreateAccount.setPadding(0, 4, 0, 0);
                txtSignIn.setTextSize(19f);
                txtSignIn.setTextColor(getResources().getColor(R.color.gray2));
                viewCreateAccount.setVisibility(View.VISIBLE);
                llLoginView.setVisibility(View.GONE);
                llSignUpView.setVisibility(View.VISIBLE);
                viewSignIn.setVisibility(View.GONE);
                break;

            case R.id.btnSendOtp:
                replaceFragment(R.id.frame, SendOtpFragment.newInstance("", "", ""),"SendOtpFragment");
                break;

            case R.id.txtForget:
                replaceFragment(R.id.frame, new ForgetPasswordFragment(),"ForgetPasswordFragment");

                break;

            case R.id.txtSendOtp:
                if (isNetworkAvailable()) {
                    if (checkValidation()) {
                        callGenereateOTP();
                    }

                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", getString(R.string.check_internet));
                }
                break;

        }
    }

    private boolean checkValidation() {

        if (TextUtils.isEmpty(edOtp.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "Enter Mobile number");
            return false;
        } else if (edOtp.getText().toString().length() < 10) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "Mobile number should not be less than 10 digits");
            return false;

        } else if (edOtp.getText().toString().length() > 10) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "Mobile number should not be greator than 10 digits");
            return false;

        }
        return true;
    }

    private void callLoginApi() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.signIn(edtMobileNu.getText().toString().trim(), edtPass.getText().toString().trim(), "dsfdsfdsf", "1");
        retrofitController.callRetrofitApi(myResponse, 1);
    }


    private void callGenereateOTP() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.generateOtp(edOtp.getText().toString().trim());
        retrofitController.callRetrofitApi(myResponse, 2);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        parseResponse(response, flag);
    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
            //MyCustomMessage.getInstance(mContext).snackbar(rlParent, message);
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
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void parseResponse(String response, int flag) {
        cusDialogProg.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            switch (flag) {
                case 1:
                    Gson gson = new Gson();
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String auth_key = jsonObjectData.getString("auth_key");
                    JSONObject jsonObjectprofile = jsonObjectData.getJSONObject("profile");
                    UserInfo.DataBean.ProfileBean profileBean = gson.fromJson(jsonObjectprofile.toString(), UserInfo.DataBean.ProfileBean.class);
                    profileBean.setAuth_key(auth_key);
                    profileBean.setRefresh_token(jsonObjectData.getString("refresh_token"));
                    Session session = new Session(mContext);
                    session.createSession(profileBean);
                    UserInfo.DataBean.ProfileBean profileBean1 = session.getUserInfo();
                    if (!profileBean1.getAuth_key().isEmpty()) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }


                    break;

                case 2:
                    if (status.equals("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String otp = jsonObject1.getString("otp");
                        String message = jsonObject.getString("message");
                        replaceFragment(R.id.frame, SendOtpFragment.newInstance(otp, edOtp.getText().toString().trim(), "login"),"SendOtpFragment");
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);

                    } else {
                        String message = jsonObject.getString("message");
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }

                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
