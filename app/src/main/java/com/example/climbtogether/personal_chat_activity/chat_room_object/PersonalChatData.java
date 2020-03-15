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
