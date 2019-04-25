package com.tristate.javafirebaseonetoonechat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String uid;
    public String displayname;
    public String email;

    public User(String uid, String displayname, String email) {
        this.uid = uid;
        this.displayname = displayname;
        this.email = email;
    }

    protected User(Parcel in) {
        uid = in.readString();
        displayname = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(displayname);
        dest.writeString(email);
    }
}
