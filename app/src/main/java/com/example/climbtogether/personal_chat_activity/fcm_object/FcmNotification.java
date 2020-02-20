package com.example.climbtogether.personal_chat_activity.fcm_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FcmNotification implements Serializable {

    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
