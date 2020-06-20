package com.procter.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.procter.activity.BaseActivity;
import com.procter.application.ProcterAppController;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;

import java.util.Objects;

import butterknife.ButterKnife;

/**
 * Created by manoj on 13-02-2018.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    View view;
    ProcterAppController procterAppController;
    protected Session session;
    protected CusDialogProg customDialog;

    protected abstract int getLayoutResource();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            procterAppController = (ProcterAppController) (Objects.requireNonNull(getActivity())).getApplication();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        mContext = context;
        if (context instanceof BaseActivity) {
            BaseActivity mActivity = (BaseActivity) context;

        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialog = new CusDialogProg(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);
        session = Session.getInstance(getContext());
        return view;
    }

    public void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment,tag);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }
    public void addFragment(int id, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, fragment,tag);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    public void clearFragmentStack(int id, Fragment fragment) {
        //It will clear all fragment from stack and oepn new specified fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void backPress(int id) {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fm.findFragmentById(id);
        fm.popBackStack();
    }


}