package com.procter.model.token;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Data implements Parcelable {

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("auth_key")
    private String authKey;

    @SerializedName("fcm_id")
    private String fcmId;

    public Data() {
    }

    protected Data(Parcel in) {
        refreshToken = in.readString();
        userType = in.readString();
        userId = in.readString();
        deviceType = in.readString();
        authKey = in.readString();
        fcmId = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "refresh_token = '" + refreshToken + '\'' +
                        ",user_type = '" + userType + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",device_type = '" + deviceType + '\'' +
                        ",auth_key = '" + authKey + '\'' +
                        ",fcm_id = '" + fcmId + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(refreshToken);
        parcel.writeString(userType);
        parcel.writeString(userId);
        parcel.writeString(deviceType);
        parcel.writeString(authKey);
        parcel.writeString(fcmId);
    }
}