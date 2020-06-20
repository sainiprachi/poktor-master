package com.procter.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.procter.model.UserInfo;

public class Session {

    @SuppressLint("StaticFieldLeak")
    private static Session instance;

    public static final String IS_LOGGEDIN = "isLoggedIn";
    public static final String ISKIP = "isLoggedIn";

    public static final String USER_ID = "userId";
    public static final String FULLNAME = "fullName";
    public static final String PHARMACY_NAME = "pharmacyName";
    public static final String IS_VERIFIED = "isVerified";
    public static final String BUSINESS_NAME = "businessName";
    public static final String EMAIL = "email";
    public static final String USER_TYPE = "userType";
    public static final String AUTHTOKEN = "authToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String STATUS = "status";
    public static final String CRD = "crd";
    public static final String DRIVING_LICENSE = "driving_license";
    public static final String PHARMACYADDRESS = "pharmacyAddress";
    public static final String OWNER_ID = "owner_id";
    public static final String OTHER = "other_id";
    public static final String MOBILE = "mobile";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String PHARMACYNU = "pharmacynu";
    public static final String PHARMACYNAME = "pharmacyname";
    public static final String IS_PROFILE = "isProfile";
    public static final String PROFILE_IMAGE = "profileImage";
    public static final String PASSWORD = "password";
    public static final String IS_NOTIFY = "isNotify";
    public static final String JOB_TITLE = "jobTitleName";
    public static final String RATING = "rating";
    public static final String COMPANY_LOGO = "company_logo";
    public static final String SPECIALIXANTION_NAME = "specializationName";
    public static final String FIRST_DISCOUNT = "first_discount";
    public static final String BID_PERCENTAGE = "bid_percentage";
    public static final String BID_MIN_VALUE = "bid_min_value";




    public static final String EMAIL_R = "email_r";
    public static final String PASSWORD_R = "password_r";
    public static final String USER_TYPE_R = "userType_r";

    private SharedPreferences mypref;
    private SharedPreferences remember_pref;

    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;


    public Session(Context context) {
        String PREF_NAME = "Procter";
        mypref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String REMEMBER_ME = "Procter_Remember";
        remember_pref = context.getSharedPreferences(REMEMBER_ME, Context.MODE_PRIVATE);
        editor = mypref.edit();
        editor2 = remember_pref.edit();
        editor.apply();
        editor2.apply();
    }


    public static Session getInstance(Context context) {
        if ((instance != null)) {
            return instance;
        }
        instance = new Session(context);
        return instance;
    }


    public void createSession(UserInfo.DataBean.ProfileBean userInfo) {
        editor.putString(USER_ID, userInfo.getUser_id());
        editor.putString(FULLNAME, userInfo.getName());
        editor.putString(PHARMACY_NAME, userInfo.getPharmacy_name());
        editor.putString(EMAIL, userInfo.getEmail());
        editor.putString(USER_TYPE, userInfo.getUser_type());
        editor.putString(AUTHTOKEN, userInfo.getAuth_key());
        editor.putString(STATUS, userInfo.getStatus());
        editor.putString(CRD, userInfo.getRegistration_start_date());
        editor.putString(CRD, userInfo.getRegistration_start_date());
        editor.putString(DRIVING_LICENSE, userInfo.getDrug_license());
        editor.putString(OWNER_ID, userInfo.getRegistration_certificate());
        editor.putString(OTHER, userInfo.getOther_document());
        editor.putString(MOBILE,userInfo.getPhone());
        editor.putString(PHARMACYNAME,userInfo.getPharmacy_name());
        editor.putString(PHARMACYADDRESS,userInfo.getAddress());
        editor.putString(PHARMACYNU, userInfo.getPharmacy_id());
        editor.putString(PROFILE_IMAGE, userInfo.getImage());
        editor.putBoolean(IS_LOGGEDIN, true);
        editor.putString(REFRESH_TOKEN,userInfo.getRefresh_token());
        editor.commit();
        editor2.commit();
    }


    public void createSessionSkip(String isSkip) {
        editor2.putString("ISKIP", isSkip);
        editor2.commit();
    }

    public String getStrVal(String key, String DefVal) {
        String lRetVal = "";
        try {
            lRetVal = mypref.getString(key, DefVal);
        } catch (Exception ex) {
        }
        return lRetVal;
    }

    public void setStrVal(String key, String DefVal) {
        SharedPreferences.Editor editor = mypref.edit();
        editor.putString(key, DefVal);
        editor.apply();
    }

    public String getISKIP() {
        return remember_pref.getString("ISKIP", "");
    }

    public UserInfo.DataBean.ProfileBean getUserInfo() {
        UserInfo.DataBean.ProfileBean userInfo = new UserInfo.DataBean.ProfileBean();
        userInfo.setUser_id(mypref.getString(USER_ID, ""));
        userInfo.setName(mypref.getString(FULLNAME, ""));
        userInfo.setPharmacy_name(mypref.getString(PHARMACY_NAME, ""));
        userInfo.setEmail(mypref.getString(EMAIL, ""));
        userInfo.setUser_type(mypref.getString(USER_TYPE, ""));
        userInfo.setAuth_key(mypref.getString(AUTHTOKEN, ""));
        userInfo.setStatus(mypref.getString(STATUS, ""));
        userInfo.setImage(mypref.getString(PROFILE_IMAGE, ""));
        userInfo.setOther_document(mypref.getString(OTHER, ""));
        userInfo.setPhone(mypref.getString(MOBILE, ""));
        userInfo.setPharmacy_id(mypref.getString(PHARMACYNU,""));
        userInfo.setPharmacy_name(mypref.getString(PHARMACYNAME,""));

        userInfo.setDrug_license(mypref.getString(DRIVING_LICENSE, ""));
        userInfo.setRegistration_certificate(mypref.getString(OWNER_ID, ""));
        userInfo.setAddress(mypref.getString(PHARMACYADDRESS, ""));
        userInfo.setRefresh_token(mypref.getString(REFRESH_TOKEN,""));

        return userInfo;
    }

 /*   public UserInfo getRememberMeInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.email=(remember_pref.getString(EMAIL_R, ""));
        userInfo.password=(remember_pref.getString(PASSWORD_R, ""));
        userInfo.userType =(remember_pref.getString(USER_TYPE_R, ""));
        return userInfo;
    }*/

    public boolean isLoggedIn() {
        return mypref.getBoolean(IS_LOGGEDIN, false);
    }

    public void logout(Context activity) {
        editor.putBoolean(IS_LOGGEDIN, false);
        editor.clear();
        editor.commit();
        editor.apply();

        /*Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();*/
    }

    public void logoutMyPre() {
        editor2.clear();
        editor2.apply();
    }

    public void setLogin(boolean value) {
        editor.putBoolean(IS_LOGGEDIN, value);
        editor.commit();
    }
}