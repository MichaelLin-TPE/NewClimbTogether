package com.hiking.climbtogether.mountain_fragment;


import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class MtTopData implements Serializable {

    @PropertyName("name")
    private String name;

    @PropertyName("photoUrl")
    private String photoUrl;

    @PropertyName("sid")
    private int sid;

    @PropertyName("topTime")
    private long topTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getTopTime() {
        return topTime;
    }

    public void setTopTime(long topTime) {
        this.topTime = topTime;
    }
}
