package com.procter.model.faq;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DataItem implements Parcelable {

    @SerializedName("question")
    private String question;

    @SerializedName("answers")
    private List<String> answers;

    boolean click =false;

    public DataItem() {
    }

    protected DataItem(Parcel in) {
        question = in.readString();
        answers = in.createStringArrayList();
        click = in.readByte() != 0;
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "question = '" + question + '\'' +
                        ",answers = '" + answers + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(answers);
        dest.writeByte((byte) (click ? 1 : 0));
    }
}