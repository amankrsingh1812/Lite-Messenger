package com.example.wa_client;

public class Message {
    private String data;
    private long timeStamp;
    private String senderId;
    private String senderName;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Message(String data, long timeStamp, String senderId, String senderName) {
        this.data = data;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
