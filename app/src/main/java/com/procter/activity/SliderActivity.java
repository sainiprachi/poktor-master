package com.procter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.procter.R;
import com.procter.adapter.ViewPagerAdapter;
import com.procter.fragment.CompleteProfileFragment;
import com.procter.fragment.UploadDocumentFragment;
import com.procter.session.Session;
import com.procter.ui.authentications.login.LoginFragment;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class SliderActivity extends PermissionActivity implements View.OnClickListener {

    private ViewPager view_pager;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        session = new Session(SliderActivity.this);
        session.createSessionSkip("true");
        initView();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            //replaceFragment(R.id.frame, new LoginFragment());

        } else {
            startActivity(new Intent(this, SplashActivity.class));


        }

    }

    public void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    public void addFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    private void initView() {
        view_pager = findViewById(R.id.view_pager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        view_pager.setAdapter(viewPagerAdapter);

        Button btnNext = findViewById(R.id.btnNext);
        final TextView txtSkip = findViewById(R.id.txtSkip);
        btnNext.setOnClickListener(this);

        txtSkip.setOnClickListener(this);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean lastPageChange = false;

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

                if (arg0 == 3) {
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lastIdx = viewPagerAdapter.getCount() - 1;

                if (lastPageChange && position == lastIdx) {
                    lastPageChange = false;
                    // next
                    Log.e("tag", "callledddddd");

                }
                lastPosition = position;
            }

            private int lastPosition = 0;


            @Override
            public void onPageScrollStateChanged(int state) {
                int lastIdx;
                if (state > 0) {
//                    lastPageChange = false;
                } else if (state <= 0) {
                    lastIdx = viewPagerAdapter.getCount() - 1;
                    int curItem = view_pager.getCurrentItem();

                    if (curItem == lastIdx && lastPosition == lastIdx && state == 0 && lastPageChange) {
                        lastPageChange = true;
                        Log.e("tag", "page scroll state >>>> ");
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finish();
                    } else if (curItem == lastIdx && lastPosition == lastIdx && state == 0) {
                        lastPageChange = true;
                        /*Log.e("tag", "page scroll state >>>> ");
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finish();*/
                    } else {
                        lastPageChange = false;
                    }
                }
               /* if (state == 0 && lastPosition == 2){
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                }*/
                lastPosition = state;


            }
        });




    /*    view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {



               *//* if (position== Objects.requireNonNull(view_pager.getAdapter()).getCount()-1) {
                    if (session.isLoggedIn()) {
                        startActivity(new Intent(SliderActivity.this,MainActivity.class));
                        //replaceFragment(R.id.frame, new LoginFragment());

                    } else {
                        startActivity(new Intent(SliderActivity.this,SplashActivity.class));


                    }
                    txtSkip.setVisibility(View.GONE);
                    txtSkip.setText(getString(R.string.next));
                } else {
                    txtSkip.setVisibility(View.VISIBLE);
                    txtSkip.setText(getString(R.string.skip));
                }*//*
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        SpringDotsIndicator dots_indicator = findViewById(R.id.dots_indicator);
        dots_indicator.setViewPager(view_pager);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (view_pager.getCurrentItem() == 2) {

                    if (session.isLoggedIn()) {
                        startActivity(new Intent(this, MainActivity.class));
                        //replaceFragment(R.id.frame, new LoginFragment());

                    } else {
                        startActivity(new Intent(this, SplashActivity.class));


                    }
                } else {
                    view_pager.setCurrentItem(view_pager.getCurrentItem() + 1, true);

                }

                break;


            case R.id.txtSkip:
                if (view_pager.getCurrentItem() == 2) {
                    if (session.isLoggedIn()) {
                        startActivity(new Intent(this, MainActivity.class));
                        //replaceFragment(R.id.frame, new LoginFragment());

                    } else {
                        startActivity(new Intent(this, SplashActivity.class));


                    }

                } else {
                    if (session.isLoggedIn()) {
                        startActivity(new Intent(this, MainActivity.class));
                        //replaceFragment(R.id.frame, new LoginFragment());

                    } else {
                        startActivity(new Intent(this, SplashActivity.class));


                    }

                }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f instanceof UploadDocumentFragment) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (f instanceof CompleteProfileFragment) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 98) {
            replaceFragment(R.id.frame, new LoginFragment());
        }

    }

}