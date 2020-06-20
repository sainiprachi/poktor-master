package com.procter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.model.ProfileModel;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.ui.authentications.login.LoginFragment;
import com.procter.utils.CusDialogProg;
import com.procter.utils.FilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procter.utils.Constant.PICDIN;
import static com.procter.utils.Constant.PICK_IMAGE_REQUEST;
import static com.procter.utils.Constant.PICOTHERS;
import static com.procter.utils.Constant.PIC_IDCARD;

public class UploadDocumentFragment extends BaseFragment implements View.OnClickListener, RetrofitResponseInterface {
    private File registration, ownerId, din, others;
    private Uri ownerUri;
    private Uri dinUri;
    private Uri otherUri;
    private RelativeLayout rlCancelDin;
    private ImageView ivSelectImage, ivAdd, ivSelectOwner, ivSelectDin, ivSelectOther, ivAddOwner, ivAddDin, ivAddOthers;
    private CardView rlRegistrationPaper, rlIdCard, rlDin, rlOthers;
    private RetrofitController retrofitController;
    private CusDialogProg cusDialogProg;
    private String isSelectRegis = "", isSelectOwnerId = "", isSelectDin = "", isSelectTerms = "";
    private RelativeLayout llParent;
    private ProfileModel profileModel;
    private RelativeLayout rlCancelOwnerId;
    private boolean isChecked = true;
    private RelativeLayout rlOthersClose;
    private ImageView ivCheck;

    private RelativeLayout rlCancelRegistration;

    public static UploadDocumentFragment newInstance(ProfileModel profileModel) {
        Bundle args = new Bundle();
        args.putParcelable("response", profileModel);
        UploadDocumentFragment fragment = new UploadDocumentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.upload_document_frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        rlRegistrationPaper = view.findViewById(R.id.rlRegistrationPaper);
        ivSelectImage = view.findViewById(R.id.ivSelectImage);
        rlOthersClose = view.findViewById(R.id.rlOthersClose);
        cusDialogProg = new CusDialogProg(mContext);
        assert getArguments() != null;
        profileModel = getArguments().getParcelable("response");
        ivAdd = view.findViewById(R.id.ivAdd);
        ivSelectOwner = view.findViewById(R.id.ivSelectOwner);
        ivSelectDin = view.findViewById(R.id.ivSelectDin);
        ivSelectOther = view.findViewById(R.id.ivSelectOther);
        ivAddDin = view.findViewById(R.id.ivAddDin);
        rlIdCard = view.findViewById(R.id.rlIdCard);
        rlCancelOwnerId = view.findViewById(R.id.rlCancelOwnerId);
        rlDin = view.findViewById(R.id.rlDin);
        ivSelectOther = view.findViewById(R.id.ivSelectOther);
        rlCancelRegistration = view.findViewById(R.id.rlCancelRegistration);
        rlCancelRegistration.setOnClickListener(this);
        ivAddOwner = view.findViewById(R.id.ivAddOwner);
        ivAddOthers = view.findViewById(R.id.ivAddOthers);
        rlOthers = view.findViewById(R.id.rlOthers);
        retrofitController = new RetrofitController(mContext, this);
        CardView rlIdCard = view.findViewById(R.id.rlIdCard);
        CardView rlDin = view.findViewById(R.id.rlDin);
        rlRegistrationPaper.setOnClickListener(this);
        rlDin.setOnClickListener(this);
        rlIdCard.setOnClickListener(this);
        rlOthers = view.findViewById(R.id.rlOthers);
        rlOthers.setOnClickListener(this);
        Button btnDone = view.findViewById(R.id.btnSave);
        btnDone.setOnClickListener(this);
        llParent = view.findViewById(R.id.llParent);
        ImageView ivImgBack = view.findViewById(R.id.ivImgBack);
        ivImgBack.setOnClickListener(this);
        rlCancelOwnerId.setOnClickListener(this);
        ivCheck = view.findViewById(R.id.ivCheck);
        ivCheck.setOnClickListener(this);
        rlCancelDin = view.findViewById(R.id.rlCancelDin);
        rlCancelDin.setOnClickListener(this);
        rlOthersClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRegistrationPaper:

                showFileChooser();
                break;

            case R.id.rlIdCard:

                showId();

                break;


            case R.id.rlDin:

                pickDin();
                break;

            case R.id.rlOthers:
                pickOthers();
                break;

            case R.id.btnSave:
                if (isNetworkAvailable()) {
                    if (checkValidation()) {
                        callRegistration();
                    }
                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("Alert", getString(R.string.check_internet));
                }

                break;


            case R.id.ivImgBack:
                backPress(R.id.frame);
                break;


            case R.id.rlCancelRegistration:
                isSelectRegis = "";
                ivAdd.setVisibility(View.VISIBLE);
                ivSelectImage.setVisibility(View.GONE);
                rlCancelRegistration.setVisibility(View.GONE);
                rlRegistrationPaper.setVisibility(View.VISIBLE);
                break;

            case R.id.rlCancelOwnerId:
                isSelectOwnerId = "";
                ivAddOwner.setVisibility(View.VISIBLE);
                ivSelectOwner.setVisibility(View.GONE);
                rlCancelOwnerId.setVisibility(View.GONE);
                rlIdCard.setVisibility(View.VISIBLE);


                break;

            case R.id.ivCheck:
                if (isChecked) {
                    isSelectTerms = "isSelectterm";
                    ivCheck.setImageResource(R.drawable.ic_check_box);
                    isChecked = false;
                } else {
                    isSelectTerms = "";
                    ivCheck.setImageResource(R.drawable.ic_basic_square);
                    isChecked = true;

                }

                break;

            case R.id.rlCancelDin:
                isSelectDin = "";
                ivAddDin.setVisibility(View.VISIBLE);
                ivSelectDin.setVisibility(View.GONE);
                rlCancelDin.setVisibility(View.GONE);
                break;


            case R.id.rlOthersClose:

                ivAddOthers.setVisibility(View.VISIBLE);
                ivSelectOther.setVisibility(View.GONE);
                rlOthersClose.setVisibility(View.GONE);

                break;


        }
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(isSelectRegis)) {

            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.add_regis));
            return false;
        } else if (TextUtils.isEmpty(isSelectOwnerId)) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.add_owner));
            return false;

        } else if (TextUtils.isEmpty(isSelectDin)) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", getString(R.string.add_din));
            return false;

        } else if (TextUtils.isEmpty(isSelectTerms)) {
            MyCustomMessage.getInstance(mContext).showCustomAlert("", "Please accept Terms and Conditions");
            return false;

        }
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }

    private void showId() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PIC_IDCARD);
    }

    private void pickDin() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICDIN);
    }

    private void pickOthers() {
        Intent intent = new Intent();
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICOTHERS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            Uri registrationUri = data.getData();

            String registrationFile;
            assert registrationUri != null;

            int sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;

            // final boolean isNougat = Build.VERSION.SDK_INT == Build.VERSION_CODES.N;
            if (sdkVersion == 24 || sdkVersion == 25||sdkVersion == 28) {
                registrationFile = FilePath.getFilePathForN(registrationUri, mContext);
            } else {
                registrationFile = FilePath.getPath(getActivity(), registrationUri);

            }


            if (registrationFile != null) {
                registration = new File(registrationFile);
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only .jpg, .png, .docx, .pdf, .jpeg file");

            }

            // Log.d("File :", "File : " + file.getName());
            String uploadedFileName = registration.getName();
            if (uploadedFileName.contains(".pdf") || uploadedFileName.contains(".png")
                    || uploadedFileName.contains(".docx") || uploadedFileName.contains(".jpeg") || uploadedFileName.contains(".jpg") || uploadedFileName.contains(".doc")) {
                if (uploadedFileName.contains(".pdf")) {
                    rlCancelRegistration.setVisibility(View.VISIBLE);
                    ivAdd.setVisibility(View.GONE);
                    ivSelectImage.setVisibility(View.VISIBLE);
                    isSelectRegis = "Regisration";
                    // rlRegistrationPaper.setBackgroundResource(0);
                    ivSelectImage.setImageResource(R.drawable.ic_pdf);
                } else if (uploadedFileName.contains(".docx")) {
                    rlCancelRegistration.setVisibility(View.VISIBLE);
                    ivAdd.setVisibility(View.GONE);
                    ivSelectImage.setVisibility(View.VISIBLE);
                    isSelectRegis = "Regisration";
                    //    rlRegistrationPaper.setBackgroundResource(0);
                    ivSelectImage.setImageResource(R.drawable.ic_word);

                } else if (uploadedFileName.contains(".doc")) {
                    rlCancelRegistration.setVisibility(View.VISIBLE);
                    ivAdd.setVisibility(View.GONE);
                    ivSelectImage.setVisibility(View.VISIBLE);
                    isSelectRegis = "Regisration";
                    //    rlRegistrationPaper.setBackgroundResource(0);
                    ivSelectImage.setImageResource(R.drawable.ic_word);

                } else {
                    rlCancelRegistration.setVisibility(View.VISIBLE);
                    ivAdd.setVisibility(View.GONE);
                    isSelectRegis = "Regisration";
                    ivSelectImage.setVisibility(View.VISIBLE);
                    ivSelectImage.setImageURI(registrationUri);

                }
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only ,.jpg,.png,.docx,.pdf,.jpeg file");

            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == PIC_IDCARD) {
            ownerUri = data.getData();
            String ownerFile;
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            if (sdkVersion == 25 || sdkVersion == 24||sdkVersion == 28) {
                ownerFile = FilePath.getFilePathForN(ownerUri, mContext);
            } else {
                ownerFile = FilePath.getPath(getActivity(), ownerUri);

            }


            assert ownerUri != null;
            assert ownerFile != null;
            ownerId = new File(ownerFile);
            Log.d("File :", "File : " + ownerId.getName());
            String uploadedFileName = ownerId.getName();
            if (uploadedFileName.contains(".pdf") || uploadedFileName.contains(".png") || uploadedFileName.contains(".docx")
                    || uploadedFileName.contains(".jpeg") || uploadedFileName.contains(".jpg") || uploadedFileName.contains(".doc")) {
                if (uploadedFileName.contains(".pdf")) {
                    ivAddOwner.setVisibility(View.GONE);
                    ivSelectOwner.setVisibility(View.VISIBLE);
                    rlCancelOwnerId.setVisibility(View.VISIBLE);
                    isSelectOwnerId = "Id";
                    ivSelectOwner.setImageResource(R.drawable.ic_pdf);
                } else if (uploadedFileName.contains(".docx")) {
                    ivAddOwner.setVisibility(View.GONE);
                    ivSelectOwner.setVisibility(View.VISIBLE);
                    rlCancelOwnerId.setVisibility(View.VISIBLE);
                    isSelectOwnerId = "Id";
                    ivSelectOwner.setImageResource(R.drawable.ic_word);

                } else if (uploadedFileName.contains(".doc")) {
                    ivAddOwner.setVisibility(View.GONE);
                    ivSelectOwner.setVisibility(View.VISIBLE);
                    rlCancelOwnerId.setVisibility(View.VISIBLE);

                    ivSelectOwner.setImageResource(R.drawable.ic_word);
                    isSelectOwnerId = "Id";

                } else {
                    ivAddOwner.setVisibility(View.GONE);
                    ivSelectOwner.setVisibility(View.VISIBLE);
                    rlCancelOwnerId.setVisibility(View.VISIBLE);
                    rlIdCard.setVisibility(View.VISIBLE);
                    ivSelectOwner.setImageURI(ownerUri);
                    isSelectOwnerId = "Id";

                }
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only .jpg, .png, .docx, .pdf, .jpeg file");

            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == PICDIN) {
            dinUri = data.getData();
            String dinFile;
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            assert dinUri != null;
            if (sdkVersion == 25 || sdkVersion == 24||sdkVersion == 28) {
                dinFile = FilePath.getFilePathForN(dinUri, mContext);
            } else {
                dinFile = FilePath.getPath(getActivity(), dinUri);

            }

            assert dinFile != null;
            din = new File(dinFile);
            Log.d("File :", "File : " + din.getName());
            String uploadedFileName = din.getName();
            if (uploadedFileName.contains(".pdf") || uploadedFileName.contains(".png") || uploadedFileName.contains(".docx") || uploadedFileName.contains(".jpeg") || uploadedFileName.contains(".jpg") || uploadedFileName.contains(".doc")) {
                if (uploadedFileName.contains(".pdf")) {
                    ivAddDin.setVisibility(View.GONE);
                    ivSelectDin.setVisibility(View.VISIBLE);
                    isSelectDin = "din";
                    rlCancelDin.setVisibility(View.VISIBLE);
                    //  rlDin.setBackgroundResource(0);
                    ivSelectDin.setImageResource(R.drawable.ic_pdf);
                } else if (uploadedFileName.contains(".docx")) {
                    ivAddOthers.setVisibility(View.GONE);
                    ivSelectDin.setVisibility(View.VISIBLE);
                    rlCancelDin.setVisibility(View.VISIBLE);
                    isSelectDin = "din";
                    //   rlDin.setBackgroundResource(0);
                    ivSelectDin.setImageResource(R.drawable.ic_word);

                } else if (uploadedFileName.contains(".doc")) {
                    ivAddOthers.setVisibility(View.GONE);
                    ivSelectDin.setVisibility(View.VISIBLE);
                    rlCancelDin.setVisibility(View.VISIBLE);
                    isSelectDin = "din";
                    //   rlDin.setBackgroundResource(0);
                    ivSelectDin.setImageResource(R.drawable.ic_word);

                } else {
                    ivAddOthers.setVisibility(View.GONE);
                    rlCancelDin.setVisibility(View.VISIBLE);
                    ivSelectDin.setVisibility(View.VISIBLE);
                    ivSelectDin.setImageURI(dinUri);
                    isSelectDin = "din";

                }
            } else {
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only .jpg, .png, .docx, .pdf, .jpeg file");

            }


        } else if (requestCode == PICOTHERS && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                otherUri = data.getData();
                assert otherUri != null;
                String pdfFile;
                int sdkVersion = android.os.Build.VERSION.SDK_INT;

                if (sdkVersion == 25 || sdkVersion == 24||sdkVersion == 28) {
                    pdfFile = FilePath.getFilePathForN(otherUri, mContext);
                } else {
                    pdfFile = FilePath.getPath(getActivity(), otherUri);

                }


                assert pdfFile != null;
                others = new File(pdfFile);

                String uploadedFileName = others.getName();
                if (uploadedFileName.contains(".pdf") || uploadedFileName.contains(".png") || uploadedFileName.contains(".docx") || uploadedFileName.contains(".jpeg") || uploadedFileName.contains(".jpg") || uploadedFileName.contains(".doc")) {


                    if (uploadedFileName.contains(".pdf")) {
                        ivAddOthers.setVisibility(View.GONE);
                        ivSelectOther.setVisibility(View.VISIBLE);
                        rlOthersClose.setVisibility(View.VISIBLE);
                        // rlOthers.setBackgroundResource(0);
                        ivSelectOther.setImageResource(R.drawable.ic_pdf);
                    } else if (uploadedFileName.contains(".docx")) {
                        ivAddDin.setVisibility(View.GONE);
                        ivSelectOther.setVisibility(View.VISIBLE);
                        rlOthersClose.setVisibility(View.VISIBLE);
                        // rlOthers.setBackgroundResource(0);
                        ivSelectOther.setImageResource(R.drawable.ic_word);

                    } else if (uploadedFileName.contains(".doc")) {
                        ivAddDin.setVisibility(View.GONE);
                        ivSelectOther.setVisibility(View.VISIBLE);
                        rlOthersClose.setVisibility(View.VISIBLE);
                        // rlOthers.setBackgroundResource(0);
                        ivSelectOther.setImageResource(R.drawable.ic_word);

                    } else {
                        ivAddOthers.setVisibility(View.GONE);
                        ivSelectOther.setVisibility(View.VISIBLE);
                        rlOthersClose.setVisibility(View.VISIBLE);
                        ivSelectOther.setImageURI(otherUri);

                    }

                } else {
                    MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "You need to select only .jpg, .png, .docx, .pdf, .jpeg file");
                }


            }

        }


    }

    private void callRegistration() {
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(mContext).create(ApiInterface.class);
        MultipartBody.Part lFileBody = null;
        MultipartBody.Part owner_id = null;
        MultipartBody.Part drug_license = null;
        MultipartBody.Part other_document = null;
        MultipartBody.Part userImage = null;
        //creating a file
        if (others != null) {
            String othern = getMimeType(others);
            RequestBody other = RequestBody.create(MediaType.parse(othern), others);
            other_document = MultipartBody.Part.createFormData("other_document", others.getName(), other);
        }

        if (registration != null) {
            String lStrMediaType = getMimeType(registration);
            String ownern = getMimeType(ownerId);
            String dinnn = getMimeType(din);
            RequestBody reqFile = RequestBody.create(MediaType.parse(lStrMediaType), registration);
            RequestBody owner = RequestBody.create(MediaType.parse(ownern), ownerId);
            RequestBody dinn = RequestBody.create(MediaType.parse(dinnn), din);
            lFileBody = MultipartBody.Part.createFormData("registration_certificate", registration.getName(), reqFile);
            owner_id = MultipartBody.Part.createFormData("owner_id", ownerId.getName(), owner);
            drug_license = MultipartBody.Part.createFormData("drug_license", din.getName(), dinn);


        }

        if (profileModel.file!=null){
            String profile = getMimeType(profileModel.file);
            RequestBody reqFile = RequestBody.create(MediaType.parse(profile), profileModel.file);
            userImage=MultipartBody.Part.createFormData("image",profileModel.file.getName(),reqFile);



        }


        RequestBody nameData = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.fullName);
        RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.email);
        RequestBody companyName = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.companyName);
        RequestBody companyRegistrationnu = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.companyRegistrationnu);
        RequestBody password = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.password);
        RequestBody confirmpass = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.confirmpass);
        RequestBody address = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.address);
        RequestBody device_id = RequestBody.create(okhttp3.MultipartBody.FORM, "dsgsadfg");
        RequestBody device_type = RequestBody.create(okhttp3.MultipartBody.FORM, "1");
        RequestBody fcm_id = RequestBody.create(okhttp3.MultipartBody.FORM, "asSSSSSSS");
        RequestBody otp_id = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.otpId);
        RequestBody latitude = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.latitude);
        RequestBody longitude = RequestBody.create(okhttp3.MultipartBody.FORM, profileModel.longitude);

        Call<ResponseBody> myResponse = apiService.register(nameData, email, companyName, companyRegistrationnu,
                password, confirmpass, device_id, device_type, fcm_id, otp_id, address,latitude,longitude, lFileBody, owner_id, drug_license,
                other_document,userImage);
        retrofitController.callRetrofitApi(myResponse, 1);
    }

    public String getMimeType(File lFile) {
        Uri selectedUri = Uri.fromFile(lFile);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }


    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("true")) {
                replaceFragment(R.id.frame, new LoginFragment(),"LoginFragment");
                MyCustomMessage.getInstance(mContext).showCustomAlert("Alert!", "Registered Successfully Please login.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(String response, int flag) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            Toast.makeText(procterAppController, "" + message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cusDialogProg.dismiss();
    }

    @Override
    public void onFailureResponse(String response, int flag) {

        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            Toast.makeText(procterAppController, "" + message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
