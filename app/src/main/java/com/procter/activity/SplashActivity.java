package com.procter.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.procter.R;
import com.procter.fragment.CompleteProfileFragment;
import com.procter.fragment.UploadDocumentFragment;
import com.procter.session.Session;
import com.procter.ui.authentications.login.LoginFragment;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestPermissionAction();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    Session session = new Session(SplashActivity.this);
                    if (session.getISKIP().equals("")) {
                        startActivity(new Intent(SplashActivity.this, SliderActivity.class));
                    } else if (session.isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        replaceFragment(R.id.frame, new LoginFragment(),"LoginFragment");
                    }
                }
            }
        };
        timer.start();

        /*if (session.isLoggedIn()){
            replaceFragment(R.id.frame,new LoginFragment());

        }else {
            replaceFragment(R.id.frame,new LoginFragment());


        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f instanceof UploadDocumentFragment) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (f instanceof CompleteProfileFragment) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

       Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
      if (f instanceof LoginFragment) {
          finishAffinity();
      }else {
          super.onBackPressed();
      }


    }
}
