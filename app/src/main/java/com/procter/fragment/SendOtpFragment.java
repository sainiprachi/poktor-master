package com.procter.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.SplashActivity;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.ui.authentications.resetpassword.ResetPasswordFragment;
import com.procter.utils.CusDialogProg;
import com.procter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.content.ContentValues.TAG;
import static android.view.KeyEvent.KEYCODE_DEL;

public class SendOtpFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener, RetrofitResponseInterface {
    private EditText pin_first_edittext;
    private EditText pin_second_edittext;
    private EditText pin_third_edittext;
    private EditText pin_forth_edittext;
    private RelativeLayout llParent;
    private String otp = "", enterOtp = "",mobile="",from="";
    private CusDialogProg cusDialogProg;
    private RetrofitController retrofitController;

    public static SendOtpFragment newInstance(String response, String mobilenu,String from) {
        Bundle args = new Bundle();
        args.putString("response", response);
        args.putString("mobile", mobilenu);
        args.putString("from",from);
        SendOtpFragment fragment = new SendOtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.send_otp_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        retrofitController = new RetrofitController(mContext, this);
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        pin_first_edittext = view.findViewById(R.id.pin_first_edittext);
        pin_second_edittext = view.findViewById(R.id.pin_second_edittext);
        pin_third_edittext = view.findViewById(R.id.pin_third_edittext);
        pin_forth_edittext = view.findViewById(R.id.pin_forth_edittext);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        llParent = view.findViewById(R.id.llParent);
        btnSubmit.setOnClickListener(this);

        TextView edOtp = view.findViewById(R.id.edOtp);
        ImageView ivImgBack=view.findViewById(R.id.ivImgBack);

        ivImgBack.setOnClickListener(this);

        pin_forth_edittext.setOnKeyListener(this);
        pin_third_edittext.setOnKeyListener(this);
        pin_second_edittext.setOnKeyListener(this);
        pin_first_edittext.setOnKeyListener(this);
        pin_forth_edittext.setOnKeyListener(this);

        cusDialogProg = new CusDialogProg(mContext);
        assert getArguments() != null;
        otp = getArguments().getString("response");
         mobile = getArguments().getString("mobile");
         from=getArguments().getString("from");
        edOtp.setText("+91  "+mobile);
        if (!otp.equals("")) {
            pin_first_edittext.setText(otp.charAt(0) + "");
            pin_second_edittext.setText(otp.charAt(1) + "");
            pin_third_edittext.setText(otp.charAt(2) + "");
            pin_forth_edittext.setText(otp.charAt(3) + "");
            enterOtp = pin_first_edittext.getText().toString().trim()
                    + pin_second_edittext.getText().toString().trim()
                    + pin_third_edittext.getText().toString().trim()
                    + pin_forth_edittext.getText().toString().trim();
        }

        TextView txtResendOtp=view.findViewById(R.id.txtResendOtp);
        txtResendOtp.setOnClickListener(this);


        pin_first_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pin_first_edittext.getText().toString().length() == 1)     //size as per your requirement
                {
                    pin_second_edittext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pin_second_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pin_first_edittext.getText().toString().length() == 1)     //size as per your requirement
                {
                    pin_third_edittext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pin_third_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pin_third_edittext.getText().toString().length() == 1)     //size as per your requirement
                {
                    pin_forth_edittext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pin_forth_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pin_third_edittext.getText().toString().length() == 1)     //size as per your requirement
                {
                    pin_forth_edittext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KEYCODE_DEL) {
            switch (v.getId()) {
                case R.id.pin_second_edittext:
                    if (pin_second_edittext.getText().toString().trim().length() == 0)
                        pin_first_edittext.requestFocus();
                    break;
                case R.id.pin_third_edittext:
                    if (pin_third_edittext.getText().toString().trim().length() == 0)
                        pin_second_edittext.requestFocus();
                    break;
                case R.id.pin_forth_edittext:
                    if (pin_forth_edittext.getText().toString().trim().length() == 0)
                        pin_third_edittext.requestFocus();
                    else if (pin_forth_edittext.getText().toString().trim().length() == 1)
                        try {
                            Utils.hideKeyboard((SplashActivity) mContext);


                        } catch (Exception e) {
                            Log.e(TAG, "afterTextChanged: " + e.toString());
                        }


                    break;

                case R.id.pin_first_edittext:
                    //pin_first_edittext.requestFocus();
                    Utils.hideKeyboard((SplashActivity) mContext);
                    pin_first_edittext.setText("");

                    break;


            }

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:
                if (from.equals("login")){
                    if (TextUtils.isEmpty(pin_first_edittext.getText().toString()) ||
                            TextUtils.isEmpty(pin_second_edittext.getText().toString().trim())
                            || TextUtils.isEmpty(pin_third_edittext.getText().toString().trim()) || TextUtils.isEmpty(pin_forth_edittext.getText().toString().trim())) {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", "Enter OTP");
                    } else {
                        enterOtp = pin_first_edittext.getText().toString().trim()
                                + pin_second_edittext.getText().toString().trim()
                                + pin_third_edittext.getText().toString().trim()
                                + pin_forth_edittext.getText().toString().trim();
                        callVerifyOtp();
                    }
                }else {
                    if (TextUtils.isEmpty(pin_first_edittext.getText().toString()) ||
                            TextUtils.isEmpty(pin_second_edittext.getText().toString().trim())
                            || TextUtils.isEmpty(pin_third_edittext.getText().toString().trim()) || TextUtils.isEmpty(pin_forth_edittext.getText().toString().trim())) {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", "Enter OTP");
                    }  else {
                        enterOtp = pin_first_edittext.getText().toString().trim()
                                + pin_second_edittext.getText().toString().trim()
                                + pin_third_edittext.getText().toString().trim()
                                + pin_forth_edittext.getText().toString().trim();
                        callVerifyOtp();
                    }
                }


                break;

            case R.id.txtResendOtp:
                pin_first_edittext.setText("");
                pin_second_edittext.setText("");
                pin_third_edittext.setText("");
                pin_forth_edittext.setText("");
                if (from.equals("login")){
                    if (isNetworkAvailable()){
                        callReset();
                    }else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("","Please Check internet");
                    }

                }else {
                    if (isNetworkAvailable()){
                        callForgetPass();
                    }else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("","Please Check internet");
                    }

                }

                break;


            case R.id.ivImgBack:
                backPress(R.id.frame);
                break;

        }


    }

    private void callForgetPass() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.forgetPass(mobile);
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    private void callReset() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.generateOtp(mobile);
        retrofitController.callRetrofitApi(myResponse, 3);
    }


    private void callVerifyOtp() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.verifyOtp(mobile,enterOtp);
        retrofitController.callRetrofitApi(myResponse, 2);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        switch (flag){
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        otp = jsonObject1.getString("otp");

                        Toast.makeText(procterAppController, "" + otp, Toast.LENGTH_SHORT).show();
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);

                    }else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 2:
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equals("true")){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        String otp_id=jsonObject1.getString("otp_id");
                       // String message=jsonObject.getString("message");
                        if (from.equals("login")){
                            replaceFragment(R.id.frame, CompleteProfileFragment.newInstance(otp_id),"CompleteProfileFragment");

                        }else {
                            replaceFragment(R.id.frame, ResetPasswordFragment.newInstance(otp_id),"ResetPasswordFragment");
                        }

                    }else {
                        String message=jsonObject.getString("message");
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        otp = jsonObject1.getString("otp");

                        Toast.makeText(procterAppController, "" + otp, Toast.LENGTH_SHORT).show();
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);

                    }else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


        }

    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject=new JSONObject(response);
            String message=jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailureResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject=new JSONObject(response);
            String message=jsonObject.getString("message");
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
