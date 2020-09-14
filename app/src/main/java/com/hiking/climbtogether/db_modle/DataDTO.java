package com.hiking.climbtogether.db_modle;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataDTO implements Serializable {
    @SerializedName("sid")
    private int sid;
    @SerializedName("name")
    private String name;
    @SerializedName("height")
    private String height;
    @SerializedName("day")
    private String day;
    @SerializedName("content")
    private String content;
    @SerializedName("location")
    private String location;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("check")
    private String check;
    @SerializedName("photo")
    private String photo;
    @SerializedName("time")
    private long time;

    private String allTitle;

    private String weatherUrl;

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getAllTitle() {
        return allTitle;
    }

    public void setAllTitle(String allTitle) {
        this.allTitle = allTitle;
    }

    public String getphoto() {
        return photo;
    }

    public void setphoto(String photo) {
        this.photo = photo;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
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

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void fromCursor(Cursor cursor){
        sid = cursor.getInt(cursor.getColumnIndex("sid"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        height = cursor.getString(cursor.getColumnIndex("height"));
        day = cursor.getString(cursor.getColumnIndex("day"));
        content = cursor.getString(cursor.getColumnIndex("content"));
        location = cursor.getString(cursor.getColumnIndex("location"));
        difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
        check = cursor.getString(cursor.getColumnIndex("check_top"));
        time = cursor.getLong(cursor.getColumnIndex("time"));
        photo = cursor.getString(cursor.getColumnIndex("user_photo"));
        allTitle = cursor.getString(cursor.getColumnIndex("all_title"));
        weatherUrl = cursor.getString(cursor.getColumnIndex("weather_url"));
    }

    public ContentValues toContentValues(){
        ContentValues data = new ContentValues();
        data.put("check_top",this.check);
        data.put("time",time);
        data.put("user_photo",photo);
        return data;
    }
}
