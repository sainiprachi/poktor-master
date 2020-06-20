package com.procter.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by ptblr-1195 on 26/2/18.
 */

public abstract class PermissionActivity extends AppCompatActivity {

    String[] perms = {"android.permission.INTERNET",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_CONTACTS",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION","android.permission.CALL_PHONE"};
    int permsRequestCode = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    public void requestPermissionAction(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(perms, permsRequestCode);
            }else {
                onPermissionsGranted(1);
            }
        }else {
            onPermissionsGranted(1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permsRequestCode == 200) {
            boolean getAccount = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (getAccount) {
                onPermissionsGranted(1);
            } else {
                Toast.makeText(this, "We cannot proceed without accepting the permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /* */
    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission){

        if(canMakeSmores()){

            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    public int checkSelfPermission(String permission) {
        return 1;
    }

    public abstract void onPermissionsGranted(int requestCode);
}