package com.hiking.climbtogether.detail_activity;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MountainFavoriteData implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("height")
    private String height;
    @SerializedName("day")
    private String day;
    @SerializedName("content")
    private String content;
    @SerializedName("photo")
    private String photoUrl;
    @SerializedName("location")
    private String location;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("userPhoto")
    private String userPhoto;
    @SerializedName("time")
    private long time;
    @SerializedName("allTitle")
    private String allTitle;

    public String getAllTitle() {
        return allTitle;
    }

    public void setAllTitle(String allTitle) {
        this.allTitle = allTitle;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photoUrl;
    }

    public void setPhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
