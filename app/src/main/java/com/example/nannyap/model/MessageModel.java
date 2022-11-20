package com.example.nannyap.model;

public class MessageModel {
    private String message,userImage,userId,messageTime;

    public MessageModel() {
    }

    public MessageModel(String message, String userImage, String userId, String messageTime) {
        this.message = message;
        this.userImage = userImage;
        this.userId = userId;
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
