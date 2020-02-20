package com.example.climbtogether.personal_chat_activity.fcm_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FcmObject implements Serializable {
    @SerializedName("to")
    private String to;
    @SerializedName("collapse_key")
    private String collapsekey;
    @SerializedName("notification")
    private FcmNotification notification;

    public String getCollapsekey() {
        return collapsekey;
    }

    public void setCollapsekey(String collapsekey) {
        this.collapsekey = collapsekey;
    }



    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FcmNotification getNotification() {
        return notification;
    }

    public void setNotification(FcmNotification notification) {
        this.notification = notification;
    }
}
