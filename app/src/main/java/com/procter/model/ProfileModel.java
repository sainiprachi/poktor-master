package com.procter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class ProfileModel implements Parcelable {

    public static final Creator<ProfileModel> CREATOR = new Creator<ProfileModel>() {
        @Override
        public ProfileModel createFromParcel(Parcel in) {
            return new ProfileModel(in);
        }

        @Override
        public ProfileModel[] newArray(int size) {
            return new ProfileModel[size];
        }
    };

    public String fullName = "";
    public String email = "";
    public String companyName = "";
    public String companyRegistrationnu = "";
    public String password = "";
    public String confirmpass = "";
    public String address = "";
    public String otpId = "";
    public String latitude="";
    public String longitude="";
    public File file;

    public ProfileModel() {
    }

    protected ProfileModel(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        companyName = in.readString();
        companyRegistrationnu = in.readString();
        password = in.readString();
        confirmpass = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(companyName);
        dest.writeString(companyRegistrationnu);
        dest.writeString(address);
        dest.writeString(password);
        dest.writeString(confirmpass);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
