package com.example.owner.fitpro.Chat.model;

/**
 * FitPro - Group #05
 * Author: Dan Epstein
 */

public class Chat  {

    /**
     * Model class this will hold all the data for Chat
     */

    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public String CurrentTime;
    public long timestamp;

    public Chat(){}

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message,String currentTime, long timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.CurrentTime = currentTime;
        this.timestamp = timestamp;
    }
}
