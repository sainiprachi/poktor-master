package com.procter.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.procter.BuildConfig;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.interfaces.GetLocation;
import com.procter.model.ProfileModel;
import com.procter.utils.Constant;
import com.procter.utils.UploadImageHelper;
import com.procter.utils.Utils;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteProfileFragment extends BaseFragment implements View.OnClickListener, UploadImageHelper.onImageUploadListener, GetLocation {
    private UploadImageHelper uploadImageHelper;
    private Dialog bottomSheetDialog;
    private Bitmap profileImageBitmap;
    private String otpId = "", address = "",latitude="",longitude="";
    private CircleImageView userProfile;
    private File file;
    private EditText edtPharmacyAdd;
    private boolean isShowPass = true;
    private boolean isShowCon = true;
    private ImageView ivShowPass, ivShoeConfirm;
    private EditText edtName, edtCompanyName, edtEmail, edtCompanyRegis, edtCreate, edtConfirmPassword;

    static CompleteProfileFragment newInstance(String otp) {
        Bundle args = new Bundle();
        args.putString("otpId", otp);
        args.putString("address", "");
        CompleteProfileFragment fragment = new CompleteProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.complete_profile_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView(View view) {
        RelativeLayout llCompleteProfile = view.findViewById(R.id.llCompleteProfile);
        llCompleteProfile.setOnClickListener(this);
        LinearLayout llParent = view.findViewById(R.id.llParent);
        userProfile = view.findViewById(R.id.userProfile);
        edtName = view.findViewById(R.id.edtName);
        edtCompanyName = view.findViewById(R.id.edtCompanyName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPharmacyAdd = view.findViewById(R.id.edtPharmacyAdd);
        TextInputLayout txtInputName = view.findViewById(R.id.txtInputName);
        TextInputLayout txtInputPharmacyAdd = view.findViewById(R.id.txtInputPharmacyAdd);
        TextInputLayout txtInputCompanyName = view.findViewById(R.id.txtInputCompanyName);
        TextInputLayout txtInputEmail = view.findViewById(R.id.txtInputEmail);
        TextInputLayout txtInputCompany = view.findViewById(R.id.txtInputCompanyr);
        TextInputLayout txtInputCreate = view.findViewById(R.id.txtInputCreate);
        TextInputLayout txtInputConfirm = view.findViewById(R.id.txtInputConfirm);
        txtInputCompanyName.setHintTextAppearance(R.style.mytext);
        txtInputPharmacyAdd.setHintTextAppearance(R.style.mytext);
        txtInputName.setHintTextAppearance(R.style.mytext);
        txtInputEmail.setHintTextAppearance(R.style.mytext);
        txtInputCompany.setHintTextAppearance(R.style.mytext);
        txtInputCompanyName.setHintTextAppearance(R.style.mytext);
        txtInputCreate.setHintTextAppearance(R.style.mytext);
        txtInputCreate.setHintTextAppearance(R.style.mytext);
        txtInputConfirm.setHintTextAppearance(R.style.mytext);

        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputName);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputPharmacyAdd);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputCompanyName);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputEmail);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputCompany);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputCreate);
        Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputConfirm);
        edtPharmacyAdd.setOnClickListener(this);
        edtCompanyRegis = view.findViewById(R.id.edtCompanyRegis);
        Button btnDone = view.findViewById(R.id.btnSave);
        btnDone.setOnClickListener(this);
        edtCreate = view.findViewById(R.id.edtCreate);
        assert getArguments() != null;
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(this);
        ivShoeConfirm = view.findViewById(R.id.ivShoeConfirm);
        ivShowPass = view.findViewById(R.id.ivShowPass);
        ivShowPass.setOnClickListener(this);
        ivShoeConfirm.setOnClickListener(this);
        otpId = getArguments().getString("otpId");
        address = getArguments().getString("address");
        edtPharmacyAdd.setText(address);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        edtCreate.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        uploadImageHelper = new UploadImageHelper(getActivity(), CompleteProfileFragment.this, this);

    }

    private boolean checkValidaton() {
        if (TextUtils.isEmpty(edtName.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Name");
            return false;
        } else if (TextUtils.isEmpty(edtCompanyName.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Company Name");
            return false;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Email");
            return false;
        } else if (TextUtils.isEmpty(edtCompanyRegis.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Comapany Registration number");
            return false;
        } else if (TextUtils.isEmpty(edtPharmacyAdd.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Address");
            return false;
        } else if (TextUtils.isEmpty(edtCreate.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Enter Password");
            return false;
        } else if (TextUtils.isEmpty(edtConfirmPassword.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Please Confirm Your  Password");
            return false;
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Enter Valid Email");
            return false;
        } else if (edtCreate.getText().toString().length() < 6) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Password Should not be less than 6 digits");
            return false;
        } else if (!edtCreate.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", "Password and Confirm password should be same");

            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCompleteProfile:
                if (uploadImageHelper.permissionAlreadyGranted()) {
                    uploadImageHelper.selectImage();
                } else {
                    uploadImageHelper.requestPermission();
                }
                break;

            case R.id.layoutforGallery:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constant.GALLERY);
                if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
                break;

            case R.id.layoutforCamera:
                try {

                    Intent intentc = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");

                    Uri imageUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    } else {
                        imageUri = Uri.fromFile(file);
                    }
                    intentc.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentc, Constant.CAMERA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case R.id.btnSave:
                if (checkValidaton()) {
                    ProfileModel profileModel = new ProfileModel();
                    profileModel.companyName = edtCompanyName.getText().toString().trim();
                    profileModel.companyRegistrationnu = edtCompanyRegis.getText().toString().trim();
                    profileModel.email = edtEmail.getText().toString().trim();
                    profileModel.fullName = edtName.getText().toString().trim();
                    profileModel.otpId = otpId;
                    profileModel.latitude=latitude;
                    profileModel.longitude=longitude;
                    profileModel.address = edtPharmacyAdd.getText().toString().trim();
                    profileModel.password = edtCreate.getText().toString().trim();
                    profileModel.confirmpass = edtConfirmPassword.getText().toString().trim();
                    if (file != null) {
                        profileModel.file = file;
                    }
                    replaceFragment(R.id.frame, UploadDocumentFragment.newInstance(profileModel),"UploadDocumentFragment");
                }

                break;

            case R.id.ivImgBack:
                backPress(R.id.framen);
                break;

            case R.id.ivShowPass:
                if (isShowPass) {
                    ivShowPass.setImageResource(R.drawable.ic_hide);
                    edtCreate.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPass = false;
                } else {
                    ivShowPass.setImageResource(R.drawable.ic_view);
                    edtCreate.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPass = true;
                }

                break;

            case R.id.ivShoeConfirm:
                if (isShowCon) {
                    ivShoeConfirm.setImageResource(R.drawable.ic_hide);
                    edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowCon = false;

                } else {
                    ivShoeConfirm.setImageResource(R.drawable.ic_view);
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowCon = true;
                }
                break;

            case R.id.edtPharmacyAdd:
                addFragment(R.id.frame, new SelectLocationFragment(CompleteProfileFragment.this),"SelectLocationFragment");
                break;
        }
    }

    private void showBidPopup() {
        bottomSheetDialog = new Dialog(mContext);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottom_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        CardView layoutforGallery = bottomSheetDialog.findViewById(R.id.layoutforGallery);
        CardView layoutforCamera = bottomSheetDialog.findViewById(R.id.layoutforCamera);
        layoutforCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intentc = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");

                    Uri imageUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    } else {
                        imageUri = Uri.fromFile(file);
                    }
                    intentc.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //  intent.putExtra("android.intent.extras.CAMERA_FACING", 1); //for front camera
                    startActivityForResult(intentc, Constant.CAMERA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layoutforGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, Constant.GALLERY);
            if (bottomSheetDialog != null) bottomSheetDialog.dismiss();
        });
        lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);
        lp.windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setAttributes(lp);
        bottomSheetDialog.show();


    }


    @Override
    public void onSuccessBitmap(File imageFile, Bitmap imageBitmap) {
        userProfile.setImageBitmap(imageBitmap);
        file=imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uploadImageHelper.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        uploadImageHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        uploadImageHelper.selectImage();
    }



    @Override
    public void setLocation(String locationStr, String latitude, String longitude) {
        edtPharmacyAdd.setText(locationStr);
        this.address = locationStr;
        this.latitude=latitude;
        this.longitude=longitude;

    }
}
