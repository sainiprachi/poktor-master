package com.procter.model.token;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class GetTokenResponse implements Parcelable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public GetTokenResponse() {
    }

    protected GetTokenResponse(Parcel in) {
        code = in.readInt();
        message = in.readString();
        status = in.readByte() != 0;
    }

    public static final Creator<GetTokenResponse> CREATOR = new Creator<GetTokenResponse>() {
        @Override
        public GetTokenResponse createFromParcel(Parcel in) {
            return new GetTokenResponse(in);
        }

        @Override
        public GetTokenResponse[] newArray(int size) {
            return new GetTokenResponse[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "GetTokenResponse{" +
                        "code = '" + code + '\'' +
                        ",data = '" + data + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(message);
        parcel.writeByte((byte) (status ? 1 : 0));
    }
}