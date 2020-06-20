package com.procter.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.activity.MainActivity;
import com.procter.adapter.UploadEditDocsAdapter;
import com.procter.model.DriverDetailModel;
import com.procter.model.UploadedImageModel;
import com.procter.model.UserInfo;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;
import com.procter.utils.FilePath;
import com.procter.utils.UploadImageHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procter.retrofit.ApiClient.BASE_URL;
import static com.procter.utils.Constant.PIC_IDCARD;

public class EditExecutiveFragment extends BaseFragment implements View.OnClickListener, RetrofitResponseInterface, UploadImageHelper.onImageUploadListener {
    @BindView(R.id.add_more_layout)
    LinearLayout addMoreLayout;
    private EditText edt_name, edtMobile, edtEmail, edtVehicle, edtCity, edtAddress, edtState, edtPinCode;
    private CusDialogProg cusDialogProg;
    private String driverId;
    private ArrayList<UploadedImageModel> uploadedImageModels = new ArrayList<>();
    private RetrofitController retrofitController;
    private CircleImageView userProfile;
    private TextView txtHeader;
    private Dialog bottomSheetDialog;
    private TextView txtDocumentName;
    private String deleteUploadedImages = "";
    private Bitmap fileBitmap;
    private UploadEditDocsAdapter uploadDocsAdapter;
    private File commonFile;
    private ImageView ivImgType;
    private LinearLayout llUploadDoc;
    private RecyclerView docs_RecyclerView;
    private File profileImage;
    private UploadImageHelper uploadImageHelper;

    private String isDrivingLicense = "", isUploadID = "", isOthers = "",
            commonIds = "", getIsDrivingLicense = "", getIsUpload = "",
            isClickDrivingLicense = "", isClickUploadId = "", isClickOther = "",getIsOther="";


    private Uri commonUri;


    EditExecutiveFragment(String driverId) {
        this.driverId = driverId;
    }

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
        return R.layout.edit_executive_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    void initView(View view) {
        edt_name = view.findViewById(R.id.edt_name);
        edtMobile = view.findViewById(R.id.edtMobile);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtVehicle = view.findViewById(R.id.edtVehicle);
        edtCity = view.findViewById(R.id.edtCity);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtState = view.findViewById(R.id.edtState);
        edtPinCode = view.findViewById(R.id.edtPinCode);
        cusDialogProg = new CusDialogProg(mContext);
        userProfile = view.findViewById(R.id.userProfile);
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setVisibility(View.VISIBLE);
        txtHeader = view.findViewById(R.id.txtHeader);
        docs_RecyclerView = view.findViewById(R.id.docs_RecyclerView);
        ivImgBack.setOnClickListener(this);
        retrofitController = new RetrofitController(getContext(), EditExecutiveFragment.this);
        getDriverDetail();
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
        userProfile.setOnClickListener(this);
        uploadImageHelper = new UploadImageHelper(getActivity(), EditExecutiveFragment.this, this);

        Button btnSave = view.findViewById(R.id.btnSave);
        llUploadDoc=view.findViewById(R.id.llUploadDoc);
        llUploadDoc.setOnClickListener(this);
        btnSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImgBack:
                backPress(R.id.frame);
                ((MainActivity) mContext).cardTabs.setVisibility(View.VISIBLE);
                break;

            case R.id.userProfile:
                if (uploadImageHelper.permissionAlreadyGranted()) {
                    uploadImageHelper.selectImage();
                } else {
                    uploadImageHelper.requestPermission();
                }
                break;


            case R.id.btnSave:
                if (isNetworkAvailable()) {
                    if (checkValidation()) {
                        callApiofUpdateExecutive();
                    }
                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.check_internet));
                }

                break;


            case R.id.llUploadDoc:
                selectImage();
                break;

        }
    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        switch (flag){
            case 1:
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        DriverDetailModel.DataBean dataBean = gson.fromJson(data.toString(), DriverDetailModel.DataBean.class);
                        setData(dataBean);

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 2:
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        ((MainActivity)mContext).cardTabs.setVisibility(View.VISIBLE);
                       replaceFragment(R.id.frame,new ExecutiveFragment(),"ExecutiveFragment");

                    } else {
                        MyCustomMessage.getInstance(mContext).showCustomAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setData(DriverDetailModel.DataBean dataBean) {
        edt_name.setText(dataBean.getName());
        edtCity.setText(dataBean.getCity());
        edtEmail.setText(dataBean.getEmail());
        edtPinCode.setText(dataBean.getPincode());
        edtVehicle.setText(dataBean.getVehicle_number());
        edtAddress.setText(dataBean.getAddress());
        edtMobile.setText(dataBean.getPhone());
        edtState.setText(dataBean.getState());
        String image = BASE_URL + dataBean.getImage();
        Picasso.get().load(image).placeholder(R.drawable.user_placeholder).into(userProfile);
        ArrayList<String> arrayList = new ArrayList<>();



        if (!TextUtils.isEmpty(dataBean.getDriving_license())) {
            String drivingLicense = BASE_URL + dataBean.getDriving_license();
            isDrivingLicense = "drivingLicense";
            getIsDrivingLicense = "drivingLicense";
            commonIds = isDrivingLicense;
            arrayList.add(drivingLicense);


        }
        if (!TextUtils.isEmpty(dataBean.getOwner_id())) {
            String owunerId = BASE_URL + dataBean.getOwner_id();
            getIsUpload = "uploadId";
            isUploadID = "uploadId";
            commonIds = isUploadID;
            arrayList.add(owunerId);
        }
        if (!TextUtils.isEmpty(dataBean.getOther_document())) {
            String owunerId = BASE_URL + dataBean.getOther_document();
            isOthers = "otherId";
            isClickOther="otherId";

            commonIds = isOthers;

            arrayList.add(owunerId);

        }
        for (int i = 0; i < arrayList.size(); i++) {
            String imageData = arrayList.get(i);
            String commonId = "";
            if (imageData.contains("driving_license_")) {
                commonId = isDrivingLicense;
            }
            if (imageData.contains("owner_id_")) {
                commonId = isUploadID;
            }
            if (imageData.contains("other_document_")) {
                commonId = isOthers;
            }

            UploadedImageModel uploadTemp = new UploadedImageModel(imageData, commonId);
            uploadedImageModels.add(uploadTemp);
        }

        if (uploadedImageModels.size()==3){
            addMoreLayout.setVisibility(View.GONE);
        }
        uploadDocsAdapter = new UploadEditDocsAdapter(getActivity(), uploadedImageModels, EditExecutiveFragment.this);
        docs_RecyclerView.setAdapter(uploadDocsAdapter);



        txtHeader.setText(String.format("Edit %s", dataBean.getName()));


    }

    public void removeImage(int position) {
        if (uploadedImageModels != null && uploadedImageModels.size() > 0) {
            for (int i = 0; i < uploadedImageModels.size(); i++) {
                if (i == position && uploadedImageModels.get(i).getImageId() != null && !uploadedImageModels.get(i).getImageId().equals("")) {
                    if (deleteUploadedImages.length() > 0) {
                        deleteUploadedImages += ",";

                    }else {
                        /*if (uploadedImageModels.get(i).getImageId().equals("drivingLicense")){
                            getIsDrivingLicense="";
                        }else if (uploadedImageModels.get(i).getImageId().equals("uploadId")){
                            getIsUpload="";
                        }*/
                    }
                    deleteUploadedImages += uploadedImageModels.get(i).getImageId();
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
            addMoreLayout.setVisibility(View.VISIBLE);
            llUploadDoc.setVisibility(View.GONE);
        } else {

            addMoreLayout.setVisibility(View.GONE);
            llUploadDoc.setVisibility(View.VISIBLE);
        }
    }

    private void getDriverDetail() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        Session session = new Session(mContext);
        UserInfo.DataBean.ProfileBean profileBean = session.getUserInfo();
        myResponse = apiService.getExecutive(profileBean.getAuth_key(), driverId);
        retrofitController.callRetrofitApi(myResponse, 1);
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
        llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
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
                        addMoreLayout.setVisibility(View.VISIBLE);
                        llUploadDoc.setVisibility(View.GONE);
                    } else {
                        addMoreLayout.setVisibility(View.GONE);
                         llUploadDoc.setVisibility(View.GONE);
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
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            isDrivingLicense = "drivingLicense";
            getIsDrivingLicense = "drivingLicense";
            commonIds = isDrivingLicense;
            isClickDrivingLicense = "true";

        });

        llIdCard.setOnClickListener(v -> {
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            getIsUpload = "uploadId";
            isUploadID = "uploadId";
            commonIds = isUploadID;
            isClickUploadId = "true";

        });

        llOthers.setOnClickListener(v -> {
            llOthers.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
            llDrivingLicense.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            llIdCard.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            txtDrivingLicense.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            txtOther.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            txtUploadId.setTextColor(ContextCompat.getColor(mContext, R.color.blackn));
            isOthers = "otherId";
            commonIds = isOthers;
            getIsOther="otherId";
            isClickOther = "true";

        });


        bottomSheetDialog.show();

    }

    /*Pic Image and Files*/

    private void picDriving() {
        Intent intent = new Intent();
        intent.setType("*/*");

        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PIC_IDCARD);
    }

    @Override
    public void onSuccessBitmap(File imageFile, Bitmap imageBitmap) {
        userProfile.setImageBitmap(imageBitmap);
        profileImage = imageFile;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploadImageHelper.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PIC_IDCARD) {
            commonUri = data.getData();
            String registrationFile;
            assert commonUri != null;

            int sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;

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


    /*Call Api of Update Executive*/

    private void callApiofUpdateExecutive() {
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        MultipartBody.Part lFileBody = null;
        MultipartBody.Part owner_id = null;
        MultipartBody.Part image = null;
        MultipartBody.Part other_document = null;
        for (int i = 0; i < uploadedImageModels.size(); i++) {
            if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase(getIsOther) && isClickOther.equals("true")) {
                if (uploadedImageModels.get(i).getImageFile() != null) {
                    String othern = getMimeType(uploadedImageModels.get(i).getImageFile());
                    RequestBody other = RequestBody.create(MediaType.parse(othern), uploadedImageModels.get(i).getImageFile());
                    other_document = MultipartBody.Part.createFormData("other_document", uploadedImageModels.get(i).getImageFile().getName(), other);
                }
            }

            if (uploadedImageModels.get(i).getImageId().equalsIgnoreCase(getIsDrivingLicense) && isClickDrivingLicense.equals("true")) {
                if (uploadedImageModels.get(i).getImageFile() != null) {
                    String othern = getMimeType(uploadedImageModels.get(i).getImageFile());
                    RequestBody other = RequestBody.create(MediaType.parse(othern), uploadedImageModels.get(i).getImageFile());
                    lFileBody = MultipartBody.Part.createFormData("driving_license", uploadedImageModels.get(i).getImageFile().getName(), other);
                }
            }

            if (getIsUpload.equalsIgnoreCase(uploadedImageModels.get(i).getImageId()) && isClickUploadId.equals("true")) {
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
        RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, edt_name.getText().toString().trim());
        RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, edtEmail.getText().toString().trim());
        RequestBody city = RequestBody.create(okhttp3.MultipartBody.FORM, edtCity.getText().toString().trim());
        RequestBody state = RequestBody.create(okhttp3.MultipartBody.FORM, edtState.getText().toString().trim());
        RequestBody pincode = RequestBody.create(okhttp3.MultipartBody.FORM, edtPinCode.getText().toString().trim());
        RequestBody vehicle_number = RequestBody.create(okhttp3.MultipartBody.FORM, edtVehicle.getText().toString().trim());
        RequestBody vehicle_name = RequestBody.create(okhttp3.MultipartBody.FORM, edtAddress.getText().toString().trim());
        RequestBody driveId = RequestBody.create(okhttp3.MultipartBody.FORM, driverId);
        Call<ResponseBody> myResponse = apiService.updateExecutive(mobile,
                name,
                email,
                city,
                state,
                pincode,
                vehicle_number,
                vehicle_name, driveId,
                lFileBody,
                owner_id,
                other_document,
                image);
        retrofitController.callRetrofitApi(myResponse, 2);
    }

    private String getMimeType(File lFile) {
        Uri selectedUri = Uri.fromFile(lFile);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    /*Check validation*/

    private boolean checkValidation() {
        if (TextUtils.isEmpty(edt_name.getText().toString().trim())) {
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
        } else if (TextUtils.isEmpty(edtAddress.getText().toString().trim())) {
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

        } else if (!(Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches())) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Enter Valid Email");
            return false;

        } else if (edtMobile.getText().toString().length() > 10) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Mobile nu should not be less than 10 digits.");
            return false;

        } else if (!getIsDrivingLicense.equals("drivingLicense")) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Add Driving License");
            return false;

        } else if (!getIsUpload.equals("uploadId")) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please Add Owner Id");
            return false;

        }
        return true;

    }


}
