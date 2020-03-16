package com.hiking.climbtogether.personal_chat_activity.chat_room_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserTwoDataDTO implements Serializable {
    @SerializedName("email")
    private String email;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("photo_url")
    private String photoUrl;

    public String getEmai() {
        return email;
    }

    public void setEmai(String email) {
        this.email = email;
    }

    public String getDisplayNmae() {
        return displayName;
    }

    public void setDisplayNmae(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
