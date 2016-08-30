package com.tristate.firebasechat.model;

/**
 * Created by tristate-android1 on 15/6/16.
 */
public class UserModel {
    private String userId;
    private String status;
    private String firstName;
    private String lastName;
    private String Platform;
    private String profileImageUri;
    private long badge;
    private String email;



    public UserModel() {
    }

    public UserModel(String userId, String status, String firstName, String lastName, String platform, String profileImageUri,long badge) {
        this.userId = userId;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Platform = platform;
        this.profileImageUri = profileImageUri;
        this.badge=badge;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public long getBadge() {
        return badge;
    }

    public void setBadge(long badge) {
        this.badge = badge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
