package com.example.friends.Models;

public class GroupMessages {
    String message;
    String senderId;
    String sendername;
    long timestamp;
    String currenttime;

    public GroupMessages(String message, String senderId, String sendername, long timestamp, String currenttime) {
        this.message = message;
        this.senderId = senderId;
        this.sendername = sendername;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
    }

    public GroupMessages() {
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

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}
