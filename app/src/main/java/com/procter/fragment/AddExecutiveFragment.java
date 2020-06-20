package com.procter.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.UploadDocsAdapter;
import com.procter.model.UploadedImageModel;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.utils.CusDialogProg;
import com.procter.utils.FilePath;
import com.procter.utils.UploadImageHelper;
import com.procter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procter.utils.Constant.PIC_IDCARD;

public class AddExecutiveFragment extends BaseFragment implements View.OnClickListener,
        RetrofitResponseInterface, UploadImageHelper.onImageUploadListener {

    private UploadImageHelper uploadImageHelper;
    private ArrayList<UploadedImageModel> uploadedImageModels = new ArrayList<>();
    @BindView(R.id.docs_RecyclerView)
    RecyclerView docsRecyclerView;
    @BindView(R.id.add_more_layout)
    LinearLayout addMoreLayout;
    private LinearLayout llUploadDoc;
    private UploadDocsAdapter uploadDocsAdapter;
    private Bitmap fileBitmap;
    private EditText edtName, edtMobile, edtEmail, edtVehicle, edtVehicles, edtCity, edtState, edtPinCode;
    private String isDrivingLicense = "", isUploadID = "", isOthers = "", commonIds = "",getIsDrivingLicense="",getIsUpload="",getIsOther="";
    private Uri commonUri;
    private File commonFile;
    private File profileImage;
    private TextView txtDocumentName;
    private ImageView ivImgType;
    private CusDialogProg cusDialogProg;
    private RetrofitController retrofitController;
    private ImageView userProfile;
    private String deleteUploadedImages = "";
    private Handler handler=new Handler(Looper.myLooper());
    private Dialog bottomSheetDialog;

    @OnClick(R.id.add_more_layout)
    void setAddMoreLayout() {
        if (uploadedImageModels.size() < 3) {
            selectImage();
        } else {
            addMoreLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.add_executive;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        handler.post(() -> {
            TextView txtHeader = view.findViewById(R.id.txtHeader);
            ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
            ivImgBack.setOnClickListener(this);
            txtHeader.setText(R.string.add_exec);
            ivImgBack.setVisibility(View.VISIBLE);
            TextInputLayout txtInputFirstName = view.findViewById(R.id.txtInputFirstName);
            TextInputLayout txtInputMobile = view.findViewById(R.id.txtInputMobile);
            TextInputLayout txtInputEmail = view.findViewById(R.id.txtInputEmail);
            TextInputLayout txtInputVehiclenu = view.findViewById(R.id.txtInputVehiclenu);
            TextInputLayout txtInputVehiclenuother = view.findViewById(R.id.txtInputVehiclenuother);
            TextInputLayout txtInpuPin = view.findViewById(R.id.txtInpuPin);
            TextInputLayout txtInputCity = view.findViewById(R.id.txtInputCity);
            TextInputLayout txtInputState = view.findViewById(R.id.txtInputState);
            edtName = view.findViewById(R.id.edtName);
            edtMobile = view.findViewById(R.id.edtMobile);
            edtEmail = view.findViewById(R.id.edtEmail);
            edtVehicles = view.findViewById(R.id.edtVehiclen);
            edtVehicle = view.findViewById(R.id.edtVehicle);
            edtState = view.findViewById(R.id.edtState);
            edtCity = view.findViewById(R.id.edtCity);
            edtPinCode = view.findViewById(R.id.edtPinCode);
            llUploadDoc = view.findViewById(R.id.llUploadDoc);
            llUploadDoc.setOnClickListener(this);
            txtInputFirstName.setHintTextAppearance(R.style.mytext);
            txtInputMobile.setHintTextAppearance(R.style.mytext);
            txtInputEmail.setHintTextAppearance(R.style.mytext);
            txtInputVehiclenu.setHintTextAppearance(R.style.mytext);
            txtInputVehiclenuother.setHintTextAppearance(R.style.mytext);
            txtInpuPin.setHintTextAppearance(R.style.mytext);
            txtInputCity.setHintTextAppearance(R.style.mytext);
            txtInputState.setHintTextAppearance(R.style.mytext);
            txtInputState.setHintTextAppearance(R.style.mytext);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputFirstName);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputMobile);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputEmail);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputVehiclenu);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputVehiclenuother);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInpuPin);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputCity);
            Utils.setFontFamilyForTextInputLayout(mContext, "poppins_regular.ttf", txtInputState);

            Button btnDone = view.findViewById(R.id.btnSave);
            uploadImageHelper = new UploadImageHelper(getActivity(), AddExecutiveFragment.this, this);
            btnDone.setOnClickListener(this);
            cusDialogProg = new CusDialogProg(mContext);
            retrofitController = new RetrofitController(getContext(), AddExecutiveFragment.this);
            RelativeLayout rlImage = view.findViewById(R.id.rlImage);
            userProfile = view.findViewById(R.id.userProfile);
            rlImage.setOnClickListener(this);
            Utils.horizontalLayout(docsRecyclerView, getActivity());
            uploadDocsAdapter = new UploadDocsAdapter(getActivity(), uploadedImageModels, AddExecutiveFragment.this);
            docsRecyclerView.setAdapter(uploadDocsAdapter);

            if (uploadedImageModels.size() > 0) {
                llUploadDoc.setVisibility(View.GONE);
                addMoreLayout.setVisibility(View.VISIBLE);
            } else {
                addMoreLayout.setVisibility(View.GONE);
                llUploadDoc.setVisibility(View.VISIBLE);
            }

            bottomSheetDialog = new Dialog(mContext);
            bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            bottomSheetDialog.setContentView(R.layout.bottom_upload_doc);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(Objects.requireNonNull(bottomSheetDialog.getWindow()).getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_dialog_view);
            lp.windowAnimations = R.style.DialogAnimation;
            bottomSheetDialog.getWindow().setAttributes(lp);

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImgBack:
                backPress(R.id.frame);
                ((MainActivity) mContext).cardTabs.setVisibility(View.VISIBLE);
                break;

            case R.id.llUploadDoc:
                selectImage();
                break;

            case R.id.btnSave:
                if (isNetworkAvailable()) {
                    if (checkValidation()) {
                        callAddExecutive();
                    }
                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.check_internet));
                }

                break;

            case R.id.rlImage:
                if (uploadImageHelper.permissionAlreadyGranted()) {
                    uploadImageHelper.selectImage();
                } else {
                    uploadImageHelper.requestPermission();
                }
                break;
        }
    }


    /*Pic Image and Files*/

    private void picDriving() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PIC_IDCARD);
      //  uploadImageHelper.selectImage();
    }




    /*Call Dialog of tag image*/

    private void selectImage() {

        final CardView llDrivingLicense = bottomSheetDialog.findViewById(R.id.llDrivingLicense);
        final CardView llIdCard = bottomSheetDialog.findViewById(R.id.llIdCard);
        final CardView llOthers = bottomSheetDialog.findViewById(R.id.llOthers);
        final TextView txtDrivingLicense = bottomSheetDialog.findViewById(R.id.txtDrivingLicense);
        final TextView txtOther = bottomSheetDialog.findViewById(R.id.txtOther);
        final TextView txtUploadId = bottomSheetDialog.findViewById(R.id.txtUploadId);
        final RelativeLayout uploadDocs = bottomSheetDialog.findViewById(R.id.upload_docs);
        Button btnDone = bottomSheetDialog.findViewById(R.id.btnSave);
        llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
        txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
        txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
        txtDocumentName = bottomSheetDialog.findViewById(R.id.txtDocumentName);
        ivImgType = bottomSheetDialog.findViewById(R.id.ivImgType);

        btnDone.setOnClickListener(v -> {

            if (commonUri == null) {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", "Plesae Select one file");
            } else if (isDrivingLicense.isEmpty() && isUploadID.isEmpty() && isOthers.isEmpty()) {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", "Plesae Select one file");
            } else {
                bottomSheetDialog.dismiss();
                uploadedImageModels.add(new UploadedImageModel(commonIds, fileBitmap, commonFile, "", commonUri));
                uploadDocsAdapter.notifyDataSetChanged();
                if (uploadedImageModels.size() < 3) {
                    if (uploadedImageModels.size() > 0) {
                        llUploadDoc.setVisibility(View.GONE);
                        addMoreLayout.setVisibility(View.VISIBLE);
                    } else {
                        addMoreLayout.setVisibility(View.GONE);
                        llUploadDoc.setVisibility(View.VISIBLE);
                    }

                    if (isDrivingLicense.equals("drivingLicense")) {
                        llDrivingLicense.setVisibility(View.GONE);
                    }
                    if (isUploadID.equals("uploadId")) {
                        llIdCard.setVisibility(View.GONE);
                    }
                    if (isOthers.equals("otherId")) {
                        llOthers.setVisibility(View.GONE);
                    }
                } else {
                    addMoreLayout.setVisibility(View.GONE);
                }
                commonFile = null;
                commonUri = null;
                txtDocumentName.setText("");
                ivImgType.setImageBitmap(null);
                isDrivingLicense = "";
                isUploadID = "";
                isOthers = "";
            }


        });

        uploadDocs.setOnClickListener(v -> picDriving());

        llDrivingLicense.setVisibility(View.VISIBLE);
        llIdCard.setVisibility(View.VISIBLE);
        llOthers.setVisibility(View.VISIBLE);


        for (int i = 0; i < uploadedImageModels.size(); i++) {
            if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase("drivingLicense")) {
                llDrivingLicense.setVisibility(View.GONE);
            } else if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase("uploadId")) {
                llIdCard.setVisibility(View.GONE);
            } else if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase("otherId")) {
                llOthers.setVisibility(View.GONE);
            }
        }


        llDrivingLicense.setOnClickListener(v -> {
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.pink));
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            isDrivingLicense = "drivingLicense";
            getIsDrivingLicense="drivingLicense";
            commonIds = isDrivingLicense;

        });

        llIdCard.setOnClickListener(v -> {
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.pink));
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            isUploadID = "uploadId";
            commonIds = isUploadID;
            getIsUpload="uploadId";

        });

        llOthers.setOnClickListener(v -> {
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.pink));
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            isOthers = "otherId";
            commonIds = isOthers;
            getIsOther="otherId";

        });

        bottomSheetDialog.show();

    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(edtName.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Name");
            return false;
        } else if (TextUtils.isEmpty(edtMobile.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Mobile number");
            return false;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Email");
            return false;
        } else if (TextUtils.isEmpty(edtVehicle.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Vehicle number");
            return false;
        } else if (TextUtils.isEmpty(edtVehicles.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Address");
            return false;
        } else if (TextUtils.isEmpty(edtCity.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter City");
            return false;
        } else if (TextUtils.isEmpty(edtState.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter State");
            return false;
        } else if (TextUtils.isEmpty(edtPinCode.getText().toString().trim())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Pin code");
            return false;
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Valid Email");
            return false;

        }else if (!(Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Valid Email");
            return false;

        }else if (edtMobile.getText().toString().length()<10) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Mobile number should not be less than 10 digits.");
            return false;

        } else if (!getIsDrivingLicense.equals("drivingLicense")) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Add Driving License");
            return false;

        }else if (!getIsUpload.equals("uploadId")) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Add ID Card");
            return false;

        }
        return true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uploadImageHelper.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PIC_IDCARD) {
            commonUri = data.getData();
            String registrationFile;
            assert commonUri != null;

            int sdkVersion = android.os.Build.VERSION.SDK_INT;

            if (sdkVersion == 24 || sdkVersion == 25 || sdkVersion == 28) {
                registrationFile = FilePath.getFilePathForN(commonUri, mContext);
            } else {
                registrationFile = FilePath.getPath(getActivity(), commonUri);

            }

            if (registrationFile != null) {
                commonFile = new File(registrationFile);
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only .jpg, .png, .docx, .pdf, .jpeg file");

            }

            String uploadedFileName = commonFile.getName();
            if (uploadedFileName.contains(".pdf") || uploadedFileName.contains(".png")
                    || uploadedFileName.contains(".docx") || uploadedFileName.contains(".jpeg") || uploadedFileName.contains(".jpg") || uploadedFileName.contains(".doc")) {
                if (uploadedFileName.contains(".pdf")) {
                    txtDocumentName.setText(commonFile.getName());
                    ivImgType.setImageResource(R.drawable.ic_pdf);

                } else if (uploadedFileName.contains(".docx")) {
                    ivImgType.setImageResource(R.drawable.ic_word);
                    txtDocumentName.setText(commonFile.getName());
                } else if (uploadedFileName.contains(".doc")) {
                    ivImgType.setImageResource(R.drawable.ic_word);
                    txtDocumentName.setText(commonFile.getName());
                } else {
                    ivImgType.setImageURI(commonUri);
                    txtDocumentName.setText(commonFile.getName());
                }
                try {
                    fileBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only ,.jpg,.png,.docx,.pdf,.jpeg file");
            }

        }
    }

    public void removeImage(int position) {
        if (uploadedImageModels != null && uploadedImageModels.size() > 0) {
            for (int i = 0; i < uploadedImageModels.size(); i++) {
                if (i == position && uploadedImageModels.get(i).getImageId() != null && !uploadedImageModels.get(i).getImageId().equals("")) {
                    if (deleteUploadedImages.length() > 0) {
                        deleteUploadedImages += ",";
                    }
                    deleteUploadedImages += uploadedImageModels.get(i).getImageId();
                }else {

                }
            }
        }
        assert uploadedImageModels != null;
        if (uploadedImageModels.get(position).getImageId().equals("drivingLicense")){
            getIsDrivingLicense="";
        }else if (uploadedImageModels.get(position).getImageId().equals("uploadId")){
            getIsUpload="";
        }
        uploadedImageModels.remove(position);

        uploadDocsAdapter.notifyDataSetChanged();
        if (uploadedImageModels.size() > 0) {
            llUploadDoc.setVisibility(View.GONE);
            addMoreLayout.setVisibility(View.VISIBLE);
        } else {
            addMoreLayout.setVisibility(View.GONE);
            llUploadDoc.setVisibility(View.VISIBLE);
        }
    }


    /*Call Api of Add Executive*/

    private void callAddExecutive() {
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        MultipartBody.Part lFileBody = null;
        MultipartBody.Part owner_id = null;
        MultipartBody.Part image = null;
        MultipartBody.Part other_document = null;
        for (int i = 0; i < uploadedImageModels.size(); i++) {
            if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase(getIsOther)) {
                if (uploadedImageModels.get(i).getImageFile() != null) {
                    String othern = getMimeType(uploadedImageModels.get(i).getImageFile());
                    RequestBody other = RequestBody.create(MediaType.parse(othern), uploadedImageModels.get(i).getImageFile());
                    other_document = MultipartBody.Part.createFormData("other_document", uploadedImageModels.get(i).getImageFile().getName(), other);
                }
            }

            if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase(getIsDrivingLicense)) {
                if (uploadedImageModels.get(i).getImageFile() != null) {
                    String othern = getMimeType(uploadedImageModels.get(i).getImageFile());
                    RequestBody other = RequestBody.create(MediaType.parse(othern), uploadedImageModels.get(i).getImageFile());
                    lFileBody = MultipartBody.Part.createFormData("driving_license", uploadedImageModels.get(i).getImageFile().getName(), other);
                }
            }

            if (getIsUpload.equalsIgnoreCase(uploadedImageModels.get(i).getImageId())){
                if (uploadedImageModels.get(i).getImageFile() != null) {
                    String othern = getMimeType(uploadedImageModels.get(i).getImageFile());
                    RequestBody other = RequestBody.create(MediaType.parse(othern), uploadedImageModels.get(i).getImageFile());
                    owner_id = MultipartBody.Part.createFormData("owner_id", uploadedImageModels.get(i).getImageFile().getName(), other);
                }

            }
        }

        if (profileImage != null) {
            String profile = getMimeType(profileImage);
            RequestBody profileIm = RequestBody.create(MediaType.parse(profile), profileImage);
            image = MultipartBody.Part.createFormData("image", profileImage.getName(), profileIm);

        }

        RequestBody mobile = RequestBody.create(okhttp3.MultipartBody.FORM, edtMobile.getText().toString().trim());
        RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, edtName.getText().toString().trim());
        RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, edtEmail.getText().toString().trim());
        RequestBody city = RequestBody.create(okhttp3.MultipartBody.FORM, edtCity.getText().toString().trim());
        RequestBody state = RequestBody.create(okhttp3.MultipartBody.FORM, edtState.getText().toString().trim());
        RequestBody pincode = RequestBody.create(okhttp3.MultipartBody.FORM, edtPinCode.getText().toString().trim());
        RequestBody vehicle_number = RequestBody.create(okhttp3.MultipartBody.FORM, edtVehicle.getText().toString().trim());
        RequestBody vehicle_name = RequestBody.create(okhttp3.MultipartBody.FORM, edtVehicles.getText().toString().trim());
        Call<ResponseBody> myResponse = apiService.addExecutive(mobile,
                name,
                email,
                city,
                state,
                pincode,
                vehicle_number,
                vehicle_name,
                lFileBody,
                owner_id,
                other_document,
                image);
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String messade = jsonObject.getString("message");
            if (status.equals("true")) {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", messade);
                ((MainActivity) mContext).cardTabs.setVisibility(View.VISIBLE);
                backPress(R.id.frame);


            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("", messade);

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
            MyCustomMessage.getInstance(mContext).showCustomAlert("", message);

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
            // MyCustomMessage.getInstance(mContext).snackbar(rlParent, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private String getMimeType(File lFile) {
        Uri selectedUri = Uri.fromFile(lFile);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    @Override
    public void onSuccessBitmap(File imageFile, Bitmap imageBitmap) {
        userProfile.setImageBitmap(imageBitmap);
        profileImage = imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        uploadImageHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
