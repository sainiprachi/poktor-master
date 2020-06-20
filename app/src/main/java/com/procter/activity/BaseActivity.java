package com.procter.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



/**
 * Created by manoj on 09-02-2018.
 */

public abstract class BaseActivity extends PermissionActivity {

    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(getLayoutResource());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(id, fragment,tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void addFragment(int id, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, fragment,tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public Fragment getCurrentFragment() {
        try {
            return getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size()-1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }


}