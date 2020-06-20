package com.procter.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;
import com.procter.R;
import com.procter.model.UserInfo;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;

import static com.procter.retrofit.ApiClient.BASE_URL;

public class MyProfileFragment extends BaseFragment {

    @BindView(R.id.edtMobile)
    EditText edtMobile;

    @BindView(R.id.edtFullName)
    EditText edtFullName;

    @BindView(R.id.edtCompanyName)
    EditText edtCompanyName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtCompanyRegis)
    EditText edtCompanyRegis;


    @BindView(R.id.edtPharmacyAdd)
    EditText edtPharmacyAdd;

    @BindView(R.id.rlDrivingLicense)
    RelativeLayout rlDrivingLicense;


    @BindView(R.id.rlOwnerId)
    RelativeLayout rlOwnerId;

    @BindView(R.id.rlOther)
    RelativeLayout rlOther;

    @BindView(R.id.ivDrivingLicense)
    ImageView ivDrivingLicense;


    @BindView(R.id.ivUpload)
    ImageView ivUpload;

    @BindView(R.id.ivOther)
    ImageView ivOther;

    @BindView(R.id.userProfile)
    ImageView userProfile;


    @BindView(R.id.txtHeader)
    TextView txtHeader;








    @Override
    protected int getLayoutResource() {
        return R.layout.my_profile_fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        UserInfo.DataBean.ProfileBean profileBean= Session.getInstance(mContext).getUserInfo();
        edtMobile.setText(profileBean.getPhone());
        edtFullName.setText(profileBean.getName());
        edtCompanyName.setText(profileBean.getPharmacy_name());
        edtEmail.setText(profileBean.getEmail());
        edtCompanyRegis.setText(profileBean.getPharmacy_id());
        edtPharmacyAdd.setText(profileBean.getAddress());
        String registration=BASE_URL+profileBean.getRegistration_certificate();

        edtMobile.setEnabled(false);
        edtFullName.setEnabled(false);
        edtCompanyName.setEnabled(false);
        edtEmail.setEnabled(false);
        edtCompanyRegis.setEnabled(false);
        String uploadId=BASE_URL+profileBean.getDrug_license();
        txtHeader.setText(R.string.my_profile);
        String image=BASE_URL+profileBean.getImage();
        if (registration.contains(".pdf")) {
           ivDrivingLicense.setImageResource(R.drawable.ic_pdf);
        }else {
            Picasso.get().load(registration).into(ivDrivingLicense);
        }

        if (uploadId.contains(".pdf")) {
            ivUpload.setImageResource(R.drawable.ic_pdf);
        }else {
            Picasso.get().load(uploadId).into(ivUpload);
        }
        Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(userProfile);
       // Picasso.get().load(uploadId).into(ivUpload);
        if (!profileBean.getOther_document().isEmpty()){
            String other=BASE_URL+profileBean.getOther_document();
            if (other.contains(".pdf")){
                ivOther.setImageResource(R.drawable.ic_pdf);
            }else {
                Picasso.get().load(other).into(ivOther);

            }

        }else {
            rlOther.setVisibility(View.GONE);
        }

        ivDrivingLicense.setOnClickListener(v -> {
            String registration1 =BASE_URL+profileBean.getRegistration_certificate();
            showDialog(registration1);
        });

        ivUpload.setOnClickListener(v -> {
            showDialog(uploadId);
        });

        ivOther.setOnClickListener(v -> {
            String other=BASE_URL+profileBean.getOther_document();

            showDialog(other);

        });
    }

    private void showDialog(String myPdfUrl){
        final Dialog bottomSheetDialog = new Dialog(mContext,R.style.full_screen_dialog);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.show_webview);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        WebView webview=bottomSheetDialog.findViewById(R.id.webview);
        webview.requestFocus();
        CusDialogProg cusDialogProg=new CusDialogProg(mContext);
        PhotoView ivImage=bottomSheetDialog.findViewById(R.id.ivImage);
        if (!myPdfUrl.contains(".pdf")){
            ivImage.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            Picasso.get().load(myPdfUrl).into(ivImage);

        }else {
            ivImage.setVisibility(View.GONE);
            String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl(url);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                        cusDialogProg.show();
                    }
                    if (progress == 100) {
                        cusDialogProg.dismiss();
                    }
                }
            });

        }
        ImageView ivImgBack=bottomSheetDialog.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(v -> bottomSheetDialog.dismiss());





        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();
    }
}
