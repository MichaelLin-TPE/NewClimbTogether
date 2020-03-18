package com.hiking.climbtogether.chat_activity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatData implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("email")
    private String email;
    @SerializedName("photo_url")
    private String photoUrl;
    @SerializedName("display_name")
    private String disPlayName;

    public String getDisPlayName() {
        return disPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        this.disPlayName = disPlayName;
    }

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
