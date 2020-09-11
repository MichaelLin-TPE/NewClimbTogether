package com.hiking.climbtogether.tool;

import com.google.firebase.firestore.PropertyName;

public class FirestoreUserData {


    @PropertyName("email")
    private String email;
    @PropertyName("displayName")
    private String displayName;
    @PropertyName("photoUrl")
    private String photoUrl;
    @PropertyName("currentTime")
    private long currentTime;
    @PropertyName("token")
    private String token;
    @PropertyName("uid")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
}
