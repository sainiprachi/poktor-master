package com.procter.ui.authentications.forgetpass;

import android.nfc.Tag;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.fragment.BaseFragment;
import com.procter.fragment.SendOtpFragment;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.utils.CusDialogProg;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ForgetPasswordFragment extends BaseFragment implements ForgetView, RetrofitResponseInterface, View.OnClickListener {
    public CusDialogProg cusDialogProg;
    private ForgetPresentor forgetPassPresentor;
    private RelativeLayout llParent;
    private EditText edOtp;
    private RetrofitController retrofitController;

    public final static String TAG = "ForgetPasswordFragment";

    @Override
    protected int getLayoutResource() {
        return R.layout.forget_pass_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cusDialogProg = new CusDialogProg(mContext);
        forgetPassPresentor = new ForgetPresenterImpl(this, new ForgetInteractorImpl());
        retrofitController = new RetrofitController(mContext, this);
        initView(view);

    }

    private void initView(View view) {
        edOtp = view.findViewById(R.id.edOtp);
        llParent = view.findViewById(R.id.llParent);
        TextInputLayout txtIputMobile = view.findViewById(R.id.txtIputMobile);
        txtIputMobile.setHintTextAppearance(R.style.mytext);
        TextView txtOtp = view.findViewById(R.id.txtOtp);
        txtOtp.setOnClickListener(this);
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(this);

        edOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    forgetPassPresentor.validationCondition(edOtp.getText().toString().trim());

                }
                return false;
            }
        });
    }

    @Override
    public void setMobileError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("ALert", getString(R.string.enter_mobile));
    }

    @Override
    public void setMobileVError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("ALert", "Mobile number should not be less than 10 digits");

    }

    @Override
    public void navigateToHome() {
        if (isNetworkAvailable()) {
            callForgetPass();
        } else {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.check_internet));
        }

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void setMobilegreatorError() {
        MyCustomMessage.getInstance(mContext).showCustomAlert("ALert", "Mobile number should not be greator than 10 digits");

    }

    private void callForgetPass() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        myResponse = apiService.forgetPass(edOtp.getText().toString().trim());
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {

            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("true")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                String otp = jsonObject1.getString("otp");
                String message = jsonObject.getString("message");
                replaceFragment(R.id.frame, SendOtpFragment.newInstance(otp, edOtp.getText().toString().trim(), "forgetPass"),"SendOtpFragment");
                /// MyCustomMessage.getInstance(mContext).snackbar(llParent, message);

                MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                // MyCustomMessage.getInstance(mContext).showCustomAlert("",message);


            } else {
                String message = jsonObject.getString("message");
                MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailureResponse(String response, int flag) {
        cusDialogProg.dismiss();
        Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOtp:
                forgetPassPresentor.validationCondition(edOtp.getText().toString().trim());
                break;

            case R.id.ivImgBack:
                backPress(R.id.frame);
                break;

        }

    }
}
