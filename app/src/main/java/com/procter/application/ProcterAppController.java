package com.procter.application;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.procter.receiver.NetworkChangeReceiver;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ProcterAppController extends Application {

    public static ProcterAppController instance = null;
    private static boolean activityVisible;
    private String Authtoken;

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static synchronized ProcterAppController getInstance() {
        if (instance != null) {
            return instance;
        }
        return new ProcterAppController();
    }

    public String getAuthtoken() {
        return Authtoken;
    }

    public void setAuthtoken(String authtoken) {
        Authtoken = authtoken;
    }

    /* public IntentAndFragmentService intentAndFragmentService;
     ApplicationPreference applicationPreference;
     public CommonUtils commonUtils;
     ApiInterface apiInterface;
     ProgressLoader progressLoader;
     NetworkCheck networkCheck;
     MultiCalendarSupport multiCalendarSupport;*/
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fabric.with(this, new Crashlytics());
        instance = this;
       /* applicationPreference =new ApplicationPreference(getBaseContext());
        intentAndFragmentService = new IntentAndFragmentService();
        commonUtils = new CommonUtils();
        apiInterface = ApiClient.getClient(getBaseContext()).create(ApiInterface.class);
        progressLoader=new ProgressLoader();
        networkCheck=new NetworkCheck(getBaseContext());
        multiCalendarSupport=new MultiCalendarSupport();
        FirebaseApp.initializeApp(this);*/
    }

    public void setNetworkStatusListener(NetworkChangeReceiver.NetworkStatusListener networkStatusListener) {
        NetworkChangeReceiver.networkStatusListener = networkStatusListener;
    }

   /* public ApiInterface getApiInterface() {
        return apiInterface;
    }

    public NetworkCheck getNetworkCheck() {
        return networkCheck;
    }

    public ProgressLoader getProgressLoader() {
        return progressLoader;
    }

    public CommonUtils getCommonUtils() {
        return commonUtils;
    }

    public IntentAndFragmentService getIntentAndFragmentService() {
        return intentAndFragmentService;
    }*/

/*
    public ApplicationPreference getApplicationPreference() {
        return applicationPreference;
    }

    public String extractCountryCode(String phone){
        int countryCode=0;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
            countryCode = numberProto.getCountryCode();
            return countryCode+"";
        } catch (NumberParseException e) {
            Log.e("INVITE_CONTACT","NumberParseException was thrown: " + e.toString());
            return "";
        }

    }

    public String extractMobileNumber(String phone){
        long mob=0;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
            mob = numberProto.getNationalNumber();
            return mob+"";
        } catch (NumberParseException e) {
            Log.e("INVITE_CONTACT","NumberParseException was thrown: " + e.toString());
            return phone.replace("-","");
        }

    }

    public MultiCalendarSupport getMultiCalendarSupport() {
        return multiCalendarSupport;
    }
*/

}
