package com.example.climbtogether.db_modle;

import android.content.ContentValues;
import android.database.Cursor;

public class DataDTO {
    private int sid;

    private String name;

    private String height;

    private String day;

    private String content;

    private byte[] photo;

    private String location;

    private String difficulty;

    private String check;

    private String userPhoto;

    private long time;

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
        photo = cursor.getBlob(cursor.getColumnIndex("photo"));
        location = cursor.getString(cursor.getColumnIndex("location"));
        difficulty = cursor.getString(cursor.getColumnIndex("difficulty"));
        check = cursor.getString(cursor.getColumnIndex("check_top"));
        time = cursor.getLong(cursor.getColumnIndex("time"));
        userPhoto = cursor.getString(cursor.getColumnIndex("user_photo"));
    }

    public ContentValues toContentValues(){
        ContentValues data = new ContentValues();
        data.put("check_top",this.check);
        data.put("time",time);
        data.put("user_photo",userPhoto);
        return data;
    }
}
