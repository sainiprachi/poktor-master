package com.procter.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionAll {

    public boolean checkCallingPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Constant.CALLING);
            return false;
        } else {
            return true;
        }
    }

    public boolean checkLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions( context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }


   /* public boolean checkAudioPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context , Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    Constant.RECORD_AUDIO);
            return false;
        } else {
            return true;
        }
    }*/

   public boolean checkWriteStoragePermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions( context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    public boolean chackCameraPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    Constant.MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CAMERA}, Constant.RequestPermissionCode);
            return true;
        }
    } // camera parmission

    public boolean RequestMultiplePermission(Activity context) {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);


        if (FirstPermissionResult != PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult != PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult != PackageManager.PERMISSION_GRANTED &&
            ForthPermissionResult != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            // Creating String Array with Permissions.
            ActivityCompat.requestPermissions( context, new String[]
                    {
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,

                    }, Constant.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        } else {
            return true;
        }
    }


    public boolean RequestMultiplePermission1(Activity context) {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (FirstPermissionResult != PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            // Creating String Array with Permissions.
            ActivityCompat.requestPermissions( context, new String[]
                    {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    }, Constant.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        } else {
            return true;
        }
    }
}
