package com.exjobb.evaluate.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserModel implements Parcelable {

    private String playerId;
    private String name;
    private String phone;
    private String note;
    private Object timeStamp;

    public UserModel(String playerId, String name, String phone, String note, Object timeStamp) {
        this.setPlayerId(playerId);
        this.setName(name);
        this.setPhone(phone);
        this.setNote(note);
        this.setTimeStamp(timeStamp);
    }

    public UserModel() {
        // firestore need this
    }

    protected UserModel(Parcel in) {
        playerId = in.readString();
        name = in.readString();
        phone = in.readString();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Player ID-playerId: " + getPlayerId() +
                "\nPlayer name: " + getName() +
                "\nPlayer e-phone: " + getPhone() +
                "\nNote about player: " + getNote();
    }

}
