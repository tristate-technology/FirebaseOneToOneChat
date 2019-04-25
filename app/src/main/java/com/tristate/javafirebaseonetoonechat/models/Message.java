package com.tristate.javafirebaseonetoonechat.models;

public class Message {

    public String message;
    public String senderId;
    public String receiverId;
    public long timestamp;

    public Message(String message, String senderId, String receiverId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
