package com.example.climbtogether.personal_chat_activity.chat_room_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonalChatData implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("time")
    private long time;
    @SerializedName("email")
    private String email;

    @SerializedName("display_name")
    private String displayName;
    @SerializedName("photo_url")
    private String photoUrl;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
