package com.procter.model.faq;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FaqResponse implements Parcelable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;


    public FaqResponse() {
    }

    protected FaqResponse(Parcel in) {
        code = in.readInt();
        data = in.createTypedArrayList(DataItem.CREATOR);
        message = in.readString();
        status = in.readByte() != 0;

    }

    public static final Creator<FaqResponse> CREATOR = new Creator<FaqResponse>() {
        @Override
        public FaqResponse createFromParcel(Parcel in) {
            return new FaqResponse(in);
        }

        @Override
        public FaqResponse[] newArray(int size) {
            return new FaqResponse[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
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
                "FaqResponse{" +
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeTypedList(data);
        dest.writeString(message);
        dest.writeByte((byte) (status ? 1 : 0));
    }
}