package com.tristate.firebasechat.model;

/**
 * Created by tristate-android1 on 15/6/16.
 */
public class ChatUserModel {
    private long badge;
    private String chat_id;
    private String isDelete;
    private String latestactivity;
    private double timestamp;
    private String user_id;
    private String profilePic;
    private String displayName;
    private boolean isGroup;
    String groupId;
    private String creatorId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }


    public ChatUserModel() {

    }

    public long getBadge() {
        return badge;
    }

    public void setBadge(long badge) {
        this.badge = badge;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getLatestactivity() {
        return latestactivity;
    }

    public void setLatestactivity(String latestactivity) {
        this.latestactivity = latestactivity;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
