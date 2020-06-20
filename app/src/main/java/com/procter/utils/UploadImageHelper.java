package com.procter.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.procter.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ptblr-1174 on 11/1/19.
 */

public class UploadImageHelper {
    private static final String TAG = "UploadImageHelper";
    private Bitmap thumbnail = null;
    private File imageFile1;
    private String mCurrentPhotoPath = null;
    private String[] perms = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};
    private Context context;
    private Fragment fragment;
    private onImageUploadListener onImageUploadListener;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private int CAMERA_CODE = 1;

    public UploadImageHelper(Context context, Fragment fragment, UploadImageHelper.onImageUploadListener onImageUploadListener) {
        this.context = context;
        this.fragment = fragment;
        this.onImageUploadListener = onImageUploadListener;
    }

    public void selectImage() {

        final Dialog bottomSheetDialog = new Dialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottom_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        LinearLayout layoutforGallery = bottomSheetDialog.findViewById(R.id.layoutforGallery);
        LinearLayout layoutforCamera = bottomSheetDialog.findViewById(R.id.layoutforCamera);
        layoutforCamera.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            cameraIntent();
        });

        layoutforGallery.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            galleryIntent();
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

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        fragment.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_file)), SELECT_FILE);
    }

    public void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "IOException " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.procter.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                fragment.startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE){
                /*try {
                    callCropMethod(MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData()));

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                callCropMethodCamera(data.getData());
            }
            else if (requestCode == REQUEST_CAMERA){
                /*try {
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    callCropMethod(mImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //callCropMethod((Bitmap) data.getExtras().get("data"));

                callCropMethodCamera(Uri.parse(mCurrentPhotoPath));
            }

            else if(requestCode ==  CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();
                    File myFile = new File(resultUri.getPath());
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    thumbnail = BitmapFactory.decodeFile(myFile.getAbsolutePath(),bmOptions);
                    // thumbnail = Bitmap.createScaledBitmap(thumbnail,parent.getWidth(),parent.getHeight(),true);
                    imageFile1=persistImage(thumbnail);
                    onImageUploadListener.onSuccessBitmap( imageFile1, thumbnail);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            }
        }

    }

    public File persistImage(Bitmap bitmap) {

        File imageFile = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {

        }
        return imageFile;
    }

    public boolean permissionAlreadyGranted() {

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;

        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result2 == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    public void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(fragment.getActivity(), perms, CAMERA_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                boolean showRationale = false;
                for (String perm : perms) {
                    showRationale = fragment.shouldShowRequestPermissionRationale(perm);
                }
                if (!showRationale) {
                    openSettingsDialog();
                }
            }
        }

    }

    public void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", fragment.getActivity().getPackageName(), null);
                intent.setData(uri);
                fragment.startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public void callCropMethodCamera(Uri selectedImageUri) {
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(selectedImageUri.getPath()).getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            int aspectRatioX,aspectRatioY;
            aspectRatioX=16;
            aspectRatioY=16;
            CropImage.activity(selectedImageUri)
                    .setAllowFlipping(true)
                    .setAllowRotation(true)
                    .setAutoZoomEnabled(true)
                    /*.setFixAspectRatio(false)
                    .setAllowRotation(true)
                    .setAutoZoomEnabled(true)
                    .setAspectRatio(aspectRatioX,aspectRatioY)*/
                    //  .setRequestedSize(imageWidth, 250, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                    // .setMaxCropResultSize(imageWidth,250)
                    /*.setMinCropWindowSize(imageWidth/6,imageHeight/6)*/
                    .setFlipHorizontally(false)
                    .setFlipVertically(false)
                    .start(fragment.getActivity(), fragment);


            //  Crop.of(selectedImageUri, selectedImageUri).withMaxSize(400,200).start(fragment.getActivity(),fragment);
            CropImage.getPickImageChooserIntent(fragment.getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public interface onImageUploadListener {
        void onSuccessBitmap(File imageFile, Bitmap imageBitmap);
    }
}
