package com.example.advancedandroid.models;

import com.google.gson.annotations.SerializedName;

public class Message {


    @SerializedName("senderUserName")
    private String Sender;


    @SerializedName("receiverUserName")
    private String Receiver;

    @SerializedName("message")
    private String message;

    @SerializedName("creationDate")
    private String messageDate;

    @SerializedName("sentMessage")
    private boolean sent_flag; // true if from contact, false is from user himself.

    public Message(String Sender, String Receiver, String message, String CreationDate, boolean SentMessage) {
        this.Sender= Sender;
        this.Receiver = Receiver;
        this.message = message;
        messageDate = CreationDate;
        sent_flag = SentMessage;

    }

    public String getSender() {
        return Sender;
    }

    public String getReceiver() { return Receiver;}

    public String getDateCreated() { return messageDate;}

    public boolean getFlagSent() { return sent_flag;}

    public String getMessage() { return message;}








}
