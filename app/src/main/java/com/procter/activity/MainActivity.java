package com.procter.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.procter.R;
import com.procter.Singleton.MyCustomMessage;
import com.procter.adapter.NavigationModelAdapter;
import com.procter.application.ProcterAppController;
import com.procter.fragment.AboutUsFragment;
import com.procter.fragment.AllOrderFragment;
import com.procter.fragment.BidsFragment;
import com.procter.fragment.EarningsFragment;
import com.procter.fragment.ExecutiveFragment;
import com.procter.fragment.FAQsFragment;
import com.procter.fragment.HomeFragment;
import com.procter.fragment.MyProfileFragment;
import com.procter.fragment.PrivacyPolicyFragment;
import com.procter.fragment.TermsAndConditionsFragment;
import com.procter.interfaces.SocketDataParser;
import com.procter.interfaces.SocketInterface;
import com.procter.model.NavigationModel;
import com.procter.model.UserInfo;
import com.procter.model.token.GetTokenResponse;
import com.procter.retrofit.ApiClient;
import com.procter.retrofit.ApiInterface;
import com.procter.retrofit.RetrofitController;
import com.procter.retrofit.RetrofitResponseInterface;
import com.procter.service.SocketStickyService;
import com.procter.session.Session;
import com.procter.utils.CusDialogProg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        RetrofitResponseInterface, SocketInterface, NavigationModelAdapter.onNavClickListener {

    private static final String TAG = "MainActivity";
    private final static int INTERVAL = 5000; //5 sec
    public LinearLayout cardTabs;
    public CircleImageView ivUserImage;
    public TextView txtUserName, txtAddressNew, txtMobilenu;
    public boolean handlerFlag = false;
    public MainActivity MAIN_ACTIVITY;
    public LinearLayout llLocation;
    public ImageView ivLocation;
    ImageView ivExecutive, ivHome, ivPayment, ivMore;
    TextView txtExecutive, txtHome, txtPayment, txtMore;
    boolean mSlideState;
    boolean isSelect = false;
    Bundle bundle;
    FragmentManager fragmentManagerMain;
    private boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer;
    private ImageView ivClose;
    private NavigationView navigation_view;
    private ArrayList<NavigationModel> navigationModelArrayList = new ArrayList<>();
    private RetrofitController retrofitController;
    private CusDialogProg cusDialogProg;
    private Session session;
    private Intent mSocketStickyIntent;
    private SocketStickyService mSocketStickyService;
    private boolean mBound = false;
    private int connectionFlag = 0;
    private Handler handler = null;
    private SocketDataParser dataParser;
    private int offline_online = 0;
    // variable to track event time
    private long mLastClickTime = 0;
    private LinearLayout llHome;
    Fragment fragment;


    private ServiceConnection mSocketStickyServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.e(TAG, "mSocketStickyServiceConnection onServiceConnected: IBinder " + service);
            SocketStickyService.LocalBinder binder = (SocketStickyService.LocalBinder) service;
            Log.e(TAG, "mSocketStickyServiceConnection: Socket ");

            mSocketStickyService = binder.getStickyService();
            mSocketStickyService.connect_to_port(MainActivity.this);
            mBound = true;
            connectCustomer();
            Log.e(TAG, "true");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.e(TAG, "mSocketStickyServiceConnection: false " + arg0);
        }
    };


    public MainActivity() {
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManagerMain = getSupportFragmentManager();
        bundle = new Bundle();
        handler = new Handler();
        mSocketStickyService = new SocketStickyService(this);
        mSocketStickyIntent = new Intent(this, mSocketStickyService.getClass());
        startStickyService();
        initView();
        this.MAIN_ACTIVITY = MainActivity.this;
    }

    void initView() {
        drawer = findViewById(R.id.relative);
        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setOnClickListener(this);
        LinearLayout llExeutive = findViewById(R.id.llExeutive);
        LinearLayout llPayment = findViewById(R.id.llPayment);
        ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        llHome = findViewById(R.id.llHome);
        LinearLayout llMore = findViewById(R.id.llMore);
        llExeutive.setOnClickListener(this);
        llMore.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        cardTabs = findViewById(R.id.cardTabs);
        ivLocation = findViewById(R.id.ivLocation);
        llHome.setOnClickListener(this);
        TextView txtLogout = findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(this);
        cusDialogProg = new CusDialogProg(this);
        llLocation = findViewById(R.id.llLocation);
        txtExecutive = findViewById(R.id.txtExecutive);
        addFragment(R.id.frame, new HomeFragment(), "HomeFragment");
        ivHome = findViewById(R.id.ivHome);
        ivExecutive = findViewById(R.id.ivExecutive);
        ivMore = findViewById(R.id.ivMore);
        ivUserImage = findViewById(R.id.ivUserImage);
        ivPayment = findViewById(R.id.ivPayment);
        drawer.setOnClickListener(this);
        txtUserName = findViewById(R.id.txtUserName);
        txtAddressNew = findViewById(R.id.txtAddressNew);
        txtMobilenu = findViewById(R.id.txtMobilenu);
        txtPayment = findViewById(R.id.txtPayment);
        txtMore = findViewById(R.id.txtMore);
        txtHome = findViewById(R.id.txtHome);
        ivHome.setColorFilter(Color.parseColor("#F37474"));
        ivPayment.setColorFilter(Color.parseColor("#727C8E"));
        ivExecutive.setColorFilter(Color.parseColor("#727C8E"));
        retrofitController = new RetrofitController(this, this);
        ivMore.setColorFilter(Color.parseColor("#727C8E"));
        txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
        txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
        txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
        txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // mainView.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                //   rlContent.setTranslationX(-slideX);
            }
        });


        ivClose.setOnClickListener(v -> {
            //  ivClose.setVisibility(View.GONE);
            drawer.closeDrawer(GravityCompat.END);
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
            txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            replaceFragment(R.id.frame, new HomeFragment(), "HomeFragment");
            ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
            ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
            ivHome.setColorFilter(getResources().getColor(R.color.light_red));
            ivMore.setColorFilter(getResources().getColor(R.color.grey8));


        });
        session = Session.getInstance(this);


        addItems();
        RecyclerView recyclerMenu = findViewById(R.id.recyclerMenu);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this));
        recyclerMenu.setAdapter(new NavigationModelAdapter(navigationModelArrayList, this));


    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        BidsFragment fragment = new BidsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame, fragment)
                .commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.llExeutive:
                replaceFragment(R.id.frame, new ExecutiveFragment(), "ExecutiveFragment");
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
                ivExecutive.setColorFilter(getResources().getColor(R.color.light_red));
                ivHome.setColorFilter(getResources().getColor(R.color.grey8));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));

                break;

            case R.id.llPayment:
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                replaceFragment(R.id.frame, new EarningsFragment(), "EarningsFragment");
                ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.light_red));
                ivHome.setColorFilter(getResources().getColor(R.color.grey8));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));
                break;


            case R.id.llHome:
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                replaceFragment(R.id.frame, new HomeFragment(), "HomeFragment");
                ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
                ivHome.setColorFilter(getResources().getColor(R.color.light_red));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));
                break;

            case R.id.llMore:
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
                ivHome.setColorFilter(getResources().getColor(R.color.grey8));
                ivMore.setColorFilter(getResources().getColor(R.color.light_red));
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                break;


            case R.id.txtLogout:
                if (isNetworkAvailable()) {
                    callLogout();
                } else {
                    MyCustomMessage.getInstance(this).showCustomAlert("", getResources().getString(R.string.check_internet));
                }

                break;

/*
         case R.id.ivClose:
             if (isSelect){
                 ivClose.setVisibility(View.GONE);
                 isSelect=true;
             }
             if (drawer.isDrawerOpen(GravityCompat.START)) {
                 drawer.closeDrawer(GravityCompat.START);
                 ivClose.setVisibility(View.GONE);

             } *//*else {
                 super.onBackPressed();
                 ivClose.setVisibility(View.GONE);
             }*//*

             break;*/


        }
    }


    public void startStickyService() {

        if (mSocketStickyService != null && !isMyServiceRunning(mSocketStickyService.getClass())) {
            Log.e(TAG, "onCreate: isServiceRunning*** ");
            //   startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(mSocketStickyIntent);
            bindService(mSocketStickyIntent, mSocketStickyServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.e(TAG, "onCreate: else***********");
            if (mSocketStickyIntent != null && mSocketStickyServiceConnection != null) {
                Log.e(TAG, "onCreate: *******bind ******");
                bindService(mSocketStickyIntent, mSocketStickyServiceConnection, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e(TAG, "isMyServiceRunning: " + service.service.getClassName());
                Log.d("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public void start_socket_connection_Service() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (handlerFlag == true) {
                    handler.removeCallbacks(this);
                    Log.e("ConnectionHandler", "stoped service");

                } else {
                    if (mBound) {
                        //  mService.connectWebSocket();
                        mSocketStickyService.connectWebSocket();
                        Toast.makeText(MAIN_ACTIVITY, "Connecting, Please wait...", Toast.LENGTH_SHORT).show();
                    }

                    handler.postDelayed(this, INTERVAL);
                }

            }
        }, 0);

    }

    public void sendData_from_MainActivity_with_progress(String data) {
        if (mBound) {
            //mService.sendDataToServer(data, 1);
            mSocketStickyService.sendDataToServer(data, 1);
//            Log.e(TAG, "sendData_from_MainActivity_with_progress: " + data);
        }
    }

    public void sendData_from_MainActivity_without_progress(String data) {
        if (mBound) {
            // mService.sendDataToServer(data, 0);
            mSocketStickyService.sendDataToServer(data, 0);
            Log.e(TAG, "sendData_from_MainActivity_without_progress: " + data);
        }
    }

    public void connectCustomer() {

        try {
            JSONObject customerConnection = new JSONObject();
            customerConnection.put("case_value", "201");
            customerConnection.put("user_id", session.getUserInfo().getUser_id());
//            sendData_from_MainActivity_without_progress(customerConnection.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: *********MainActivity");
        ProcterAppController.activityResumed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: MainActivity**");
        try {
            if (mSocketStickyServiceConnection != null)
                unbindService(mSocketStickyServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ***********SupportActivity");
    }

    public void offDuty() {
        removeCallBacks();
        offline_online = 1;
        if (mBound) {
            mSocketStickyService.disconnectSocket();
            if (mSocketStickyIntent != null && mSocketStickyServiceConnection != null) {
                unbindService(mSocketStickyServiceConnection);
                stopService(mSocketStickyIntent);
            }
            mBound = false;
        }
    }

    public void removeCallBacks() {
        try {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                Log.e("handler", "handler callback removed");
            } else {
                Log.e("handler", "handler already removed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Handler handler = new Handler();
        Runnable runnable;
        Fragment bac = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (bac instanceof ExecutiveFragment) {
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
            txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
            ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
            ivHome.setColorFilter(getResources().getColor(R.color.light_red));
            ivMore.setColorFilter(getResources().getColor(R.color.grey8));


        } else if (bac instanceof HomeFragment) {
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
            txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
            ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
            ivHome.setColorFilter(getResources().getColor(R.color.light_red));
            ivMore.setColorFilter(getResources().getColor(R.color.grey8));

        } else if (bac instanceof EarningsFragment) {
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
            txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
            ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
            ivHome.setColorFilter(getResources().getColor(R.color.light_red));
            ivMore.setColorFilter(getResources().getColor(R.color.grey8));
        } else {
            txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
            txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
            ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
            ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
            ivHome.setColorFilter(getResources().getColor(R.color.light_red));
            ivMore.setColorFilter(getResources().getColor(R.color.grey8));

        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
            cardTabs.setVisibility(View.VISIBLE);
        } else {
            handler.postDelayed(runnable = () -> doubleBackToExitPressedOnce = false, 1000);
            if (doubleBackToExitPressedOnce) {
                handler.removeCallbacks(runnable);
                finishAffinity();
            } else {

                if (bac instanceof HomeFragment) {
                    finishAffinity();

                }
                // MyCustomMessage.getInstance(this).snackbar(findViewById(R.id.relative), getResources().getString(R.string.for_exit));
                doubleBackToExitPressedOnce = true;
            }
        }

    }


    private void addItems() {
        NavigationModel item;
        for (int i = 0; i < 10; i++) {
            item = new NavigationModel();
            switch (i) {
                case 0:
                    item.itemName = "My Profile";
                    item.itemImg = R.drawable.ic_avatar;
                    item.isSelect = true;

                    break;
                case 1:
                    item.itemName = getString(R.string.home);
                    item.isSelect = false;
                    item.itemImg = R.drawable.ic_home;
                    item.itemImgActive = R.drawable.ic_home;

                    break;

                case 2:
                    item.itemName = "Order History";
                    item.isSelect = false;
                    item.itemImg = R.drawable.order_history;
                    item.itemImgActive = R.drawable.order_history;

                    break;

                case 3:
                    item.itemName = getString(R.string.payments);
                    item.isSelect = false;
                    item.itemImg = R.drawable.paymentnpng;
                    item.itemImgActive = R.drawable.paymentnpng;
                    break;

                case 4:
                    item.itemName = "Executives";
                    item.isSelect = false;
                    item.itemImg = R.drawable.ic_delivery_man;
                    item.itemImgActive = R.drawable.ic_delivery_man;
                    break;

                case 5:
                    item.itemName = "Settings";
                    item.isSelect = false;
                    item.itemImg = R.drawable.ic_settings;
                    item.itemImgActive = R.drawable.ic_settings;
                    break;

                case 6:
                    item.itemName = "About Us";
                    item.itemImg = R.drawable.ic_company;
                    item.itemImgActive = R.drawable.ic_company;

                    item.isSelect = false;


                    break;
                case 7:
                    item.itemName = "Privacy Policy";
                    item.itemImg = R.drawable.ic_do_not_disturb;
                    item.itemImgActive = R.drawable.ic_do_not_disturb;

                    item.isSelect = false;


                    break;
                case 8:
                    item.itemName = "Terms & Conditions ";
                    item.itemImg = R.drawable.faq;
                    item.itemImgActive = R.drawable.faq;

                    item.isSelect = false;


                    break;
                case 9:
                    item.itemName = "FAQ's";
                    item.itemImg = R.drawable.termsandcon;
                    item.itemImgActive = R.drawable.termsandcon;

                    item.isSelect = false;
                    break;


            }
            navigationModelArrayList.add(item);
        }
    }

    @Override
    public void onSuccessResponse(String response, int flag) {
        cusDialogProg.dismiss();
        if (flag==2) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                if (status.equals("true")) {
                    session.logout(this);
                    startActivity(new Intent(this, SplashActivity.class));
                } else {
                    MyCustomMessage.getInstance(this).showCustomAlert("", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (flag==12345){
            cusDialogProg.dismiss();
            fragment =getCurrentFragment();

            Gson gson = new Gson();
            GetTokenResponse getTokenResponse = gson.fromJson(response,GetTokenResponse.class);
            session.setStrVal(Session.AUTHTOKEN,getTokenResponse.getData().getAuthKey());
            getSupportFragmentManager().beginTransaction().remove(getCurrentFragment()).add(R.id.frame,fragment).commit();

        }

    }

    @Override
    public void onErrorResponse(String response, int flag) {
        cusDialogProg.dismiss();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            if (fragment==null){
            MyCustomMessage.getInstance(this).showCustomAlert("", message);}
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
            MyCustomMessage.getInstance(this).showCustomAlert("", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void callLogout() {
        Call<ResponseBody> myResponse;
        ApiInterface apiService;
        cusDialogProg.show();
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        UserInfo.DataBean.ProfileBean userinfo = session.getUserInfo();
        myResponse = apiService.logout(userinfo.getAuth_key());
        retrofitController.callRetrofitApi(myResponse, 2);
    }

    @Override
    public void socketResponse(String response) {
        try {
            Log.e(TAG, response);

            JSONObject rootObj = new JSONObject(response);
            switch (rootObj.getString("key")) {

                case "socket_disconnected_1006":
                    handlerFlag = false;
                    //pushAppToForground();
                    start_socket_connection_Service();
                case "socket_disconnected": {
                    if (connectionFlag == 0) {
                        handlerFlag = false;
                        connectionFlag = 1;
                    } else {
                        Log.e("ConnectionHandler", "Already started  connection service");
                    }
                    start_socket_connection_Service();
                    return;
                }
                case "Connection timed out":
                    handlerFlag = false;
                    //   pushAppToForground();
                    start_socket_connection_Service();
                    return;
                case "pharmacy_connected": {
                    handlerFlag = true;
                    connectionFlag = 0;
                    offline_online = 0;
                    removeCallBacks();
                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
                    assert fragment != null;
                    fragment.getAllBidsList();
                    break;
                }

                case "bids":

                case "open_bids":
                    if (getCurrentFragment() != null && getCurrentFragment().getTag().equals("BidsFragment")) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("BidsFragment");
                        if (fragment == null) {
                            callFragWithDelay(response, "BidsFragment");
                        } else {
                            dataParser = (SocketDataParser) fragment;
                            dataParser.parseResponse(response);
                            return;
                        }
                    } else if (getCurrentFragment() != null && getCurrentFragment().getTag().equals("HomeFragment")) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
                        if (fragment == null) {
                            callFragWithDelay(response, "HomeFragment");
                        } else {
                            dataParser = (SocketDataParser) fragment;
                            dataParser.parseResponse(response);
                            return;
                        }
                    }
                    break;
                case "pending_bids":
                case "closed_bids":
                    if (getCurrentFragment() != null && getCurrentFragment().getTag().equals("BidsFragment")) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("BidsFragment");
                        if (fragment == null) {
                            callFragWithDelay(response, "BidsFragment");
                        } else {
                            dataParser = (SocketDataParser) fragment;
                            dataParser.parseResponse(response);
                            return;
                        }
                    }
                    break;
                case "driver_location":
                case "order_not_found":
                case "order_rejected":
                case "order_not_assigned": {
                    //JSONObject locationResponse = rootObj.getJSONObject("response");
                    break;
                }// JSONObject json3 = rootObj.getJSONObject("response");
// JSONObject json5 = rootObj.getJSONObject("response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void replaceFragmentNoStack(Fragment fragment, String tag) {
        dataParser = (SocketDataParser) fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();

    }


    public void replaceFragmentba(int id, Fragment fragment, String tag) {
        dataParser = (SocketDataParser) fragment;
        FragmentTransaction fragmentTransaction = fragmentManagerMain.beginTransaction();
        fragmentTransaction.replace(id, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addFragmentback(int id, Fragment fragment, String tag) {
        dataParser = (SocketDataParser) fragment;
        FragmentTransaction fragmentTransaction = fragmentManagerMain.beginTransaction();
        fragmentTransaction.add(id, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public String getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            Log.e("Visisble fragment is :", fragment.isVisible() + "");
            if (fragment != null && fragment.isVisible())
                return fragment.getTag();

        }
        return " ";
    }

    private void callFragWithDelay(final String request, String req) {

        Fragment frag = null;
        if (req.equals("BidsFragment")) {
            frag = new BidsFragment();
        }
        if (frag != null) {
            replaceFragmentNoStack(frag, req);
        }


        new Handler().postDelayed(() -> dataParser.parseResponse(request), 15000);

    }

    public Fragment getCurrentFragmentMain() {
        return getSupportFragmentManager()
                .findFragmentById(R.id.frame);
    }


    @Override
    public void onNavClick(int position) {
        switch (position) {
            case 2:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new AllOrderFragment(), "AllOrderFragment");
                break;

            case 3:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                replaceFragment(R.id.frame, new EarningsFragment(), "EarningsFragment");
                ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.light_red));
                ivHome.setColorFilter(getResources().getColor(R.color.grey8));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));

                break;

            case 0:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new MyProfileFragment(), "AllOrderFragment");
                break;

            case 6:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new AboutUsFragment(), AboutUsFragment.class.getSimpleName());
                break;

            case 7:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new PrivacyPolicyFragment(), PrivacyPolicyFragment.class.getSimpleName());
                break;

            case 8:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new TermsAndConditionsFragment(), TermsAndConditionsFragment.class.getSimpleName());
                break;

            case 9:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new FAQsFragment(), FAQsFragment.class.getSimpleName());
                break;

            case 1:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }

                txtHome.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                replaceFragment(R.id.frame, new HomeFragment(), "HomeFragment");
                ivExecutive.setColorFilter(getResources().getColor(R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
                ivHome.setColorFilter(getResources().getColor(R.color.light_red));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));

                break;

            case 4:
                if (drawer.isDrawerOpen(navigation_view)) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(navigation_view);
                }
                replaceFragment(R.id.frame, new ExecutiveFragment(), "ExecutiveFragment");
                txtPayment.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtHome.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                txtExecutive.setTextColor(ContextCompat.getColor(this, R.color.light_red));
                txtMore.setTextColor(ContextCompat.getColor(this, R.color.grey8));
                ivPayment.setColorFilter(getResources().getColor(R.color.grey8));
                ivExecutive.setColorFilter(getResources().getColor(R.color.light_red));
                ivHome.setColorFilter(getResources().getColor(R.color.grey8));
                ivMore.setColorFilter(getResources().getColor(R.color.grey8));
                break;



        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void refreshToken(Call<ResponseBody> call, int flag) {
        cusDialogProg.dismiss();
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ResponseBody> myResponse = apiInterface.getToken(session.getUserInfo().getRefresh_token(),session.getUserInfo().getUser_id());
        retrofitController.callRetrofitApi(myResponse,12345);

    }

    public void logout() {
        MyCustomMessage.getInstance(this).showCustomAlert("", "Session Expired! Please login again to continue.");
        cusDialogProg.dismiss();
        session.logout(this);
        startActivity(new Intent(this, SplashActivity.class));

    }
}
