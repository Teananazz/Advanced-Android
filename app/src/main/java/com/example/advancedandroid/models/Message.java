package com.example.advancedandroid.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"Sender","Receiver", "message", "messageDate" })


public class Message {


    @SerializedName("senderUserName")
    @NonNull
    private String Sender;

    @NonNull
    @SerializedName("receiverUserName")
    private String Receiver;

    @SerializedName("message")
    @NonNull
    private String message;

    @NonNull
    @SerializedName("creationDate")
    private String messageDate;

    @SerializedName("sentMessage")
    private boolean sent_flag; // true if from contact, false is from user himself.

    public boolean isSent_flag() {
        return sent_flag;
    }

    public Message() {
        message = "";
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public void setSent_flag(boolean sent_flag) {
        this.sent_flag = sent_flag;
    }

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

    public String getMessageDate() { return messageDate;}

    public boolean getFlagSent() { return sent_flag;}

    public String getMessage() { return message;}

    @Override
    public String toString() {
        return "Message{" +
                "Sender='" + Sender + '\'' +
                ", Receiver='" + Receiver + '\'' +
                ", message='" + message + '\'' +
                ", messageDate='" + messageDate + '\'' +
                ", sent_flag=" + sent_flag +
                '}';
    }
}
